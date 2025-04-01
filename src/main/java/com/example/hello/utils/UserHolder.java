package com.example.hello.utils;

import com.example.hello.dto.UserDTO; 

public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    // 设置用户
    public static void setUser(UserDTO user) {
        tl.set(user);
    }

    public static UserDTO getUser() {
        return tl.get();
    }
    // 移除用户
    public static void removeUser() {
        tl.remove();
    }
}