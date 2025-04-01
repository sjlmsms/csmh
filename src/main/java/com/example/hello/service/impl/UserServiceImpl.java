package com.example.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hello.dto.UserLoginDTO;
import com.example.hello.dto.UserRegisterDTO;
import com.example.hello.dto.UserUpdateDTO;
import com.example.hello.entity.User;
import com.example.hello.mapper.UserMapper;
import com.example.hello.service.UserService;
import com.example.hello.vo.UserLoginVO;
import com.example.hello.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User register(UserRegisterDTO registerDTO) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword()); // 实际应用中应该对密码进行加密
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setCreateTime(LocalDateTime.now());

        // 3. 保存用户
        this.save(user);
        
        // 4. 清空密码后返回
        user.setPassword(null);
        return user;
    }

    @Override
    public UserLoginVO login(UserLoginDTO loginDTO) {
        // 1. 验证用户名和密码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(queryWrapper);
        
        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 2. 更新最后登录时间
        user.setLastLogin(LocalDateTime.now());
        this.updateById(user);

        // 3. 生成登录返回信息
        UserLoginVO loginVO = new UserLoginVO();
        // TODO: 这里应该使用真实的 JWT token 生成逻辑
        loginVO.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        loginVO.setUserInfo(userInfoVO);

        return loginVO;
    }

    @Override
    public User getUserById(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public void updateUser(Long userId, UserUpdateDTO updateDTO) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 只更新非空字段
        if (updateDTO.getPhone() != null) {
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getEmail() != null) {
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getIcon() != null) {
            user.setIcon(updateDTO.getIcon());
        }

        this.updateById(user);
    }
} 