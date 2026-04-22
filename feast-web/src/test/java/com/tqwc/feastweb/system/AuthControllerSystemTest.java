package com.tqwc.feastweb.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqwc.feastcommon.entity.User;
import com.tqwc.feast.jwt.JwtTokenProvider;
import com.tqwc.feast.jwt.JwtAuthenticationFilter;
import com.tqwc.feastweb.dto.auth.LoginRequest;
import com.tqwc.feastweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @MockBean
    private AccessDeniedHandler accessDeniedHandler;

    @Test
    void login_shouldReturnSuccessInSystemContext() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("system-user");
        request.setPassword("123456");

        User user = new User();
        user.setId(1001L);
        user.setUsername("system-user");
        user.setEmail("system@example.com");
        user.setStatus(1);
        when(userService.login("system-user", "123456")).thenReturn(user);
        when(jwtTokenProvider.generateToken(1001L, "system-user")).thenReturn("system-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conde").value(20000))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data.token").value("system-token"))
                .andExpect(jsonPath("$.data.user.id").value(1001))
                .andExpect(jsonPath("$.data.user.username").value("system-user"))
                .andExpect(jsonPath("$.data.user.email").value("system@example.com"));
    }
}
