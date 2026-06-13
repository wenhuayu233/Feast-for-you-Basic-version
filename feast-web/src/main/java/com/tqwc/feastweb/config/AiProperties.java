package com.tqwc.feastweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI 配置属性类。
 */
@Component
@ConfigurationProperties(prefix = "ai.wenxin")
public class AiProperties {
    private boolean enabled;
    private String baseUrl;
    private String appId;
    private String secretKey;
    private int timeoutSeconds = 30;

    /**
     * 获取 AI 功能是否开启。
     *
     * @return true 表示开启，false 表示关闭
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置 AI 功能是否开启。
     *
     * @param enabled true 表示开启，false 表示关闭
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取文心智能体接口地址。
     *
     * @return 文心智能体接口地址
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 设置文心智能体接口地址。
     *
     * @param baseUrl 文心智能体接口地址
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 获取文心智能体应用 ID。
     *
     * @return 文心智能体应用 ID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置文心智能体应用 ID。
     *
     * @param appId 文心智能体应用 ID
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取文心智能体密钥。
     *
     * @return 文心智能体密钥
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 设置文心智能体密钥。
     *
     * @param secretKey 文心智能体密钥
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 获取 AI 请求超时时间。
     *
     * @return 超时时间，单位秒
     */
    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    /**
     * 设置 AI 请求超时时间。
     *
     * @param timeoutSeconds 超时时间，单位秒
     */
    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
