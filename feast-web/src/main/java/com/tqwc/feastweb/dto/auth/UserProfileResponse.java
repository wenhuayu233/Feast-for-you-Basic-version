package com.tqwc.feastweb.dto.auth;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String gender;
    private String avatar;
    private String bio;
    private Integer status;
}
