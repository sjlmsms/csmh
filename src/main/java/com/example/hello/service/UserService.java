package com.example.hello.service;

import com.example.hello.dto.UserLoginDTO;
import com.example.hello.dto.UserRegisterDTO;
import com.example.hello.dto.UserUpdateDTO;
import com.example.hello.entity.User;
import com.example.hello.vo.UserLoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User register(UserRegisterDTO registerDTO);
    
    UserLoginVO login(UserLoginDTO loginDTO);
    
    User getUserById(Long userId);
    
    void updateUser(Long userId, UserUpdateDTO updateDTO);
} 