package com.tqwc.feastweb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqwc.feastweb.config.AiProperties;
import com.tqwc.feastweb.service.AiService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AI 服务实现类。
 */
@Service
public class AiServiceImpl implements AiService {
    private static final String SOURCE = "feast-for-you";
    private static final String FROM = "openapi";

    private final AiProperties aiProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    /**
     * 创建 AI 服务实现类。
     *
     * @param aiProperties AI 配置属性
     */
    public AiServiceImpl(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * 根据用户输入调用文心智能体生成回答。
     *
     * @param message 用户输入内容
     * @param currentUserId 当前用户 ID
     * @return AI 回答内容
     */
    @Override
    public String chat(String message, Long currentUserId) {
        return sendMessage(buildDinnerPrompt(message), currentUserId);
    }

    /**
     * 向文心智能体发送消息。
     *
     * @param message 发送给智能体的完整提示词
     * @param currentUserId 当前用户 ID
     * @return 智能体回答内容
     */
    private String sendMessage(String message, Long currentUserId) {
        validateAiConfig();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildRequestUrl()))
                    .timeout(Duration.ofSeconds(aiProperties.getTimeoutSeconds()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(buildPayload(message, currentUserId))))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("AI 服务调用失败，HTTP 状态码：" + response.statusCode());
            }

            String content = extractContent(response.body());
            if (isBlank(content)) {
                throw new IllegalStateException("AI 服务未返回可用内容");
            }
            return content;
        } catch (IOException e) {
            throw new IllegalStateException("AI 服务请求失败：" + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("AI 服务请求被中断", e);
        }
    }

    /**
     * 校验 AI 配置是否完整。
     */
    private void validateAiConfig() {
        if (!aiProperties.isEnabled()) {
            throw new IllegalStateException("AI 功能未开启");
        }
        if (isBlank(aiProperties.getBaseUrl())) {
            throw new IllegalStateException("AI 服务地址未配置");
        }
        if (isBlank(aiProperties.getAppId()) || isBlank(aiProperties.getSecretKey())) {
            throw new IllegalStateException("文心智能体 appId 或 secretKey 未配置");
        }
        if (aiProperties.getTimeoutSeconds() <= 0) {
            throw new IllegalStateException("AI 请求超时时间必须大于 0");
        }
    }

    /**
     * 构建文心智能体请求地址。
     *
     * @return 带鉴权参数的请求地址
     */
    private String buildRequestUrl() {
        String separator = aiProperties.getBaseUrl().contains("?") ? "&" : "?";
        return aiProperties.getBaseUrl()
                + separator
                + "appId=" + encode(aiProperties.getAppId())
                + "&secretKey=" + encode(aiProperties.getSecretKey());
    }

    /**
     * 构建文心智能体请求体。
     *
     * @param message 发送给智能体的完整提示词
     * @param currentUserId 当前用户 ID
     * @return 文心智能体请求体
     */
    private Map<String, Object> buildPayload(String message, Long currentUserId) {
        Map<String, Object> value = new HashMap<>();
        value.put("showText", message);

        Map<String, Object> content = new HashMap<>();
        content.put("type", "text");
        content.put("value", value);

        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("content", content);

        Map<String, Object> payload = new HashMap<>();
        payload.put("message", messageBody);
        payload.put("source", SOURCE);
        payload.put("from", FROM);
        payload.put("openId", buildOpenId(currentUserId));
        return payload;
    }

    /**
     * 构建文心智能体用户标识。
     *
     * @param currentUserId 当前用户 ID
     * @return 文心智能体用户标识
     */
    private String buildOpenId(Long currentUserId) {
        return currentUserId == null ? "guest" : "user-" + currentUserId;
    }

    /**
     * 构建晚餐推荐提示词。
     *
     * @param message 用户输入内容
     * @return 晚餐推荐提示词
     */
    private String buildDinnerPrompt(String message) {
        return "你是 Feast For You 的晚餐推荐助手。"
                + "请根据用户的口味、食材、人数、时间和氛围，推荐 2 到 4 个适合今晚吃的菜。"
                + "回答要中文、简洁、可执行，每个推荐说明一句理由。"
                + "用户输入：" + message;
    }

    /**
     * 从文心智能体响应中提取文本内容。
     *
     * @param body 响应原文
     * @return 提取出的文本内容
     * @throws IOException JSON 解析失败时抛出
     */
    private String extractContent(String body) throws IOException {
        JsonNode root = objectMapper.readTree(body);
        ensureAiSuccess(root);

        String dataText = extractDataContent(root.path("data"));
        if (!isBlank(dataText)) {
            return dataText;
        }

        String text = findText(root, "content", "answer", "result", "output", "text");
        if (!isBlank(text)) {
            return text;
        }

        if (root.isTextual()) {
            return root.asText();
        }
        return null;
    }

    /**
     * 校验文心智能体业务响应是否成功。
     *
     * @param root 响应 JSON 根节点
     */
    private void ensureAiSuccess(JsonNode root) {
        JsonNode statusNode = firstExisting(root, "status", "code", "errCode", "errorCode");
        if (statusNode == null || statusNode.isMissingNode() || statusNode.isNull()) {
            return;
        }
        if ((statusNode.isInt() || statusNode.isLong())
                && (statusNode.asLong() == 0L || statusNode.asLong() == 200L || statusNode.asLong() == 20000L)) {
            return;
        }
        if (statusNode.isTextual()
                && ("0".equals(statusNode.asText())
                || "200".equals(statusNode.asText())
                || "20000".equals(statusNode.asText())
                || "success".equalsIgnoreCase(statusNode.asText()))) {
            return;
        }

        String errorMessage = findText(root, "msg", "message", "errMsg", "errorMsg", "error");
        throw new IllegalStateException("AI 服务返回失败：" + (isBlank(errorMessage) ? statusNode.asText() : errorMessage));
    }

    /**
     * 从 data 节点中提取文本内容。
     *
     * @param dataNode data 节点
     * @return 提取出的文本内容
     */
    private String extractDataContent(JsonNode dataNode) {
        if (dataNode == null || dataNode.isMissingNode() || dataNode.isNull()) {
            return null;
        }

        String text = findText(dataNode, "content", "answer", "result", "output", "text", "message");
        if (!isBlank(text)) {
            return text;
        }

        JsonNode contentNode = dataNode.path("content");
        if (contentNode.isArray()) {
            StringBuilder builder = new StringBuilder();
            for (JsonNode item : contentNode) {
                String itemText = findText(item, "data", "content", "answer", "text", "message");
                if (isBlank(itemText)) {
                    itemText = findNestedShowText(item);
                }
                if (!isBlank(itemText)) {
                    builder.append(itemText);
                }
            }
            return builder.isEmpty() ? null : builder.toString();
        }

        return findNestedShowText(dataNode);
    }

    /**
     * 从指定节点中按候选字段查找文本。
     *
     * @param node 待查找节点
     * @param fields 候选字段名
     * @return 查找到的文本内容
     */
    private String findText(JsonNode node, String... fields) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }
        for (String field : fields) {
            JsonNode value = node.get(field);
            if (value != null && value.isTextual() && !value.asText().isBlank()) {
                return value.asText();
            }
        }
        return null;
    }

    /**
     * 获取第一个存在的响应字段。
     *
     * @param node 待查找节点
     * @param fields 候选字段名
     * @return 第一个存在的字段节点
     */
    private JsonNode firstExisting(JsonNode node, String... fields) {
        for (String field : fields) {
            JsonNode value = node.get(field);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 查找嵌套的 showText 文本。
     *
     * @param node 待查找节点
     * @return showText 文本
     */
    private String findNestedShowText(JsonNode node) {
        JsonNode showText = node.at("/value/showText");
        if (showText.isTextual() && !showText.asText().isBlank()) {
            return showText.asText();
        }
        JsonNode contentShowText = node.at("/content/value/showText");
        if (contentShowText.isTextual() && !contentShowText.asText().isBlank()) {
            return contentShowText.asText();
        }
        return null;
    }

    /**
     * 判断字符串是否为空。
     *
     * @param value 待判断字符串
     * @return true 表示为空，false 表示非空
     */
    private static boolean isBlank(String value) {
        return Objects.isNull(value) || value.isBlank();
    }

    /**
     * URL 编码参数值。
     *
     * @param value 参数值
     * @return 编码后的参数值
     */
    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
