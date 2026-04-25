package com.tqwc.feastweb.controller;

import com.tqwc.feastcommon.entity.User;
import com.tqwc.feastcommon.utils.Result;
import com.tqwc.feastcommon.utils.StatusCode;
import com.tqwc.feastweb.dto.auth.LoginRequest;
import com.tqwc.feastweb.dto.auth.RegisterRequest;
import com.tqwc.feastweb.dto.auth.UpdateProfileRequest;
import com.tqwc.feastweb.dto.auth.UserProfileResponse;
import com.tqwc.feast.jwt.JwtTokenProvider;
import com.tqwc.feast.jwt.JwtUserPrincipal;
import com.tqwc.feastweb.service.UserService;
import com.tqwc.feastweb.utils.MinioUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MinioUtil minioUtil;
    private final String cookieName;
    private final long cookieMaxAge;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          MinioUtil minioUtil,
                          @Value("${security.jwt.cookie-name:FFY_TOKEN}") String cookieName,
                          @Value("${security.jwt.expiration-seconds:259200}") long cookieMaxAge) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.minioUtil = minioUtil;
        this.cookieName = cookieName;
        this.cookieMaxAge = cookieMaxAge;
    }

    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        try {
            User user = userService.register(request);
            String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
            writeTokenCookie(response, token);
            return new Result(StatusCode.OK, "注册成功", buildAuthData(user, token));
        } catch (IllegalArgumentException e) {
            return new Result(StatusCode.ERROR, e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            User user = userService.login(request.getAccount(), request.getPassword());
            String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
            writeTokenCookie(response, token);
            return new Result(StatusCode.OK, "登录成功", buildAuthData(user, token));
        } catch (IllegalArgumentException e) {
            return new Result(StatusCode.LOGINERROR, e.getMessage());
        } catch (IllegalStateException e) {
            return new Result(StatusCode.ACCESSERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            return new Result(StatusCode.ERROR, "头像文件不能为空");
        }
        try {
            String avatarUrl = minioUtil.uploadFile(file);
            return new Result(StatusCode.OK, "上传成功", avatarUrl);
        } catch (Exception e) {
            return new Result(StatusCode.ERROR, "头像上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return new Result(StatusCode.OK, "退出成功");
    }

    @GetMapping("/me")
    public Result currentUser(Authentication authentication) {
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        User user = userService.getById(principal.getUserId());
        if (Objects.isNull(user)) {
            return new Result(StatusCode.ERROR, "用户不存在");
        }
        return new Result(StatusCode.OK, "获取成功", toProfile(user));
    }

    @PutMapping("/me")
    public Result updateProfile(@Valid @RequestBody UpdateProfileRequest request, Authentication authentication) {
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof JwtUserPrincipal principal)) {
            return new Result(StatusCode.ACCESSERROR, "未登录");
        }
        try {
            User user = userService.updateProfile(principal.getUserId(), request);
            return new Result(StatusCode.OK, "更新成功", toProfile(user));
        } catch (IllegalArgumentException e) {
            return new Result(StatusCode.ERROR, e.getMessage());
        } catch (Exception e) {
            return new Result(StatusCode.ERROR, "更新失败: " + e.getMessage());
        }
    }

    private void writeTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(cookieMaxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private Map<String, Object> buildAuthData(User user, String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", toProfile(user));
        return data;
    }

    private UserProfileResponse toProfile(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());
        response.setGender(user.getGender());
        response.setAvatar(user.getAvatar());
        response.setBio(user.getBio());
        response.setStatus(user.getStatus());
        return response;
    }
}
