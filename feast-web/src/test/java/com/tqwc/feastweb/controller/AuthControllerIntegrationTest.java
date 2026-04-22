package com.tqwc.feastweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqwc.feastcommon.entity.User;
import com.tqwc.feast.jwt.JwtTokenProvider;
import com.tqwc.feastweb.dto.auth.LoginRequest;
import com.tqwc.feastweb.dto.auth.RegisterRequest;
import com.tqwc.feastweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
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
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void register_shouldReturnSuccessResultAndCookie() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("tang");
        request.setEmail("tang@example.com");
        request.setPassword("123456");

        User user = new User();
        user.setId(101L);
        user.setUsername("tang");
        user.setEmail("tang@example.com");
        user.setStatus(1);

        when(userService.register(anyString(), anyString(), anyString())).thenReturn(user);
        when(jwtTokenProvider.generateToken(101L, "tang")).thenReturn("mock-token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("FFY_TOKEN"))
                .andExpect(jsonPath("$.conde").value(20000))
                .andExpect(jsonPath("$.message").value("注册成功"))
                .andExpect(jsonPath("$.data.token").value("mock-token"))
                .andExpect(jsonPath("$.data.user.id").value(101));
    }

    @Test
    void login_shouldReturnLoginErrorWhenCredentialInvalid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("tang");
        request.setPassword("wrong");

        when(userService.login("tang", "wrong")).thenThrow(new IllegalArgumentException("账号或密码错误"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conde").value(20002))
                .andExpect(jsonPath("$.message").value("账号或密码错误"));
    }

    @Test
    void login_shouldReturnAccessErrorWhenAccountDisabled() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("tang");
        request.setPassword("123456");

        when(userService.login("tang", "123456")).thenThrow(new IllegalStateException("账号被禁用"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conde").value(20003))
                .andExpect(jsonPath("$.message").value("账号被禁用"));
    }
}
