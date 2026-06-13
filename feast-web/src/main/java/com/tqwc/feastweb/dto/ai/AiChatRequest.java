package com.tqwc.feastweb.dto.ai;

import jakarta.validation.constraints.NotBlank;

/**
 * AI 聊天请求参数类。
 */
public class AiChatRequest {
    @NotBlank(message = "消息内容不能为空")
    private String message;

    /**
     * 获取用户输入的消息内容。
     *
     * @return 用户输入的消息内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置用户输入的消息内容。
     *
     * @param message 用户输入的消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
