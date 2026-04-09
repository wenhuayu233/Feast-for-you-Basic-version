package com.tqwc.feastweb.config;

import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityExceptionHandlerConfig {
    private final ObjectMapper objectMapper;

    public SecurityExceptionHandlerConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) ->
                write(response, HttpServletResponse.SC_UNAUTHORIZED, new Result(StatusCode.ACCESSERROR, "未登录或登录已过期"));
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                write(response, HttpServletResponse.SC_FORBIDDEN, new Result(StatusCode.ACCESSERROR, "无权访问该接口"));
    }

    private void write(HttpServletResponse response, int status, Result result) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
