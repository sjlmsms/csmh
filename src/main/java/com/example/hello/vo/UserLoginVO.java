package com.example.hello.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private String token;
    private UserInfoVO userInfo;
} 