package com.tqwc.feastweb.controller;

import com.tqwc.feastweb.utils.MinioUtil;
import tools.jackson.databind.json.JsonMapper;
import com.tqwc.feast.jwt.JwtTokenProvider;
import com.tqwc.feastcommon.entity.User;
import com.tqwc.feastweb.dto.auth.LoginRequest;
import com.tqwc.feastweb.dto.auth.RegisterRequest;
import com.tqwc.feastweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private MinioUtil minioUtil;

    @Test
    void register_shouldReturnSuccessResultAndCookie() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Test3");
        request.setEmail("test3@example.com");
        request.setPassword("123456");

        User user = new User();
        user.setId(101L);
        user.setUsername("test3");
        user.setEmail("test3@example.com");
        user.setStatus(1);

        when(userService.register(any(RegisterRequest.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(101L, "test3")).thenReturn("mock-token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("FFY_TOKEN"))
                .andExpect(jsonPath("$.code").value(20000))
                .andExpect(jsonPath("$.message").value("注册成功"))
                .andExpect(jsonPath("$.data.token").value("mock-token"))
                .andExpect(jsonPath("$.data.user.id").value(101));
    }

    @Test
    void login_shouldReturnLoginErrorWhenCredentialInvalid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("tang");
        request.setPassword("wrong");

        when(userService.login("tang", "wrong"))
                .thenThrow(new IllegalArgumentException("账号或密码错误"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(20002))
                .andExpect(jsonPath("$.message").value("账号或密码错误"));
    }

    @Test
    void login_shouldReturnAccessErrorWhenAccountDisabled() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("tang");
        request.setPassword("123456");

        when(userService.login("tang", "123456"))
                .thenThrow(new IllegalStateException("账号被禁用"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(20003))
                .andExpect(jsonPath("$.message").value("账号被禁用"));
    }
}