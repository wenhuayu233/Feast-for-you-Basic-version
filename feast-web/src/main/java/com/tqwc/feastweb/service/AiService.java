package com.tqwc.feastweb.service;

/**
 * AI 服务接口。
 */
public interface AiService {
    /**
     * 根据用户输入生成 AI 回答。
     *
     * @param message 用户输入内容
     * @param currentUserId 当前用户 ID
     * @return AI 回答内容
     */
    String chat(String message, Long currentUserId);
}
