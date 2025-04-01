package com.example.hello.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    // 用户名
    private String username;
    // 密码
    private String password;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
} 