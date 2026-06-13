package com.tqwc.feastweb.controller;

import com.tqwc.feast.jwt.JwtUserPrincipal;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.dto.ai.AiChatRequest;
import com.tqwc.feastweb.service.AiService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * AI 功能控制器。
 */
@RestController
@RequestMapping("/ai")
public class AiController {
    private final AiService aiService;

    /**
     * 创建 AI 功能控制器。
     *
     * @param aiService AI 服务
     */
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * 处理 AI 聊天推荐请求。
     *
     * @param request AI 聊天请求参数
     * @param authentication 当前登录认证信息
     * @return AI 回答内容
     */
    @PostMapping("/chat")
    public Result chat(@Valid @RequestBody AiChatRequest request, Authentication authentication) {
        JwtUserPrincipal principal = currentPrincipal(authentication);
        if (Objects.isNull(principal)) {
            return new Result(StatusCode.ACCESSERROR, "请先登录后再使用 AI 功能");
        }

        String content = aiService.chat(request.getMessage(), principal.getUserId());
        return new Result(StatusCode.OK, "生成成功", Map.of("content", content));
    }

    /**
     * 从认证信息中获取当前登录用户。
     *
     * @param authentication 当前登录认证信息
     * @return 当前登录用户，未登录时返回 null
     */
    private static JwtUserPrincipal currentPrincipal(Authentication authentication) {
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            return null;
        }
        return principal;
    }
}
