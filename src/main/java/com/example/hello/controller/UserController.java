package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.*;
import com.example.hello.dto.*;
import com.example.hello.entity.User;
import com.example.hello.service.UserService;
import com.example.hello.utils.UserHolder;
import com.example.hello.vo.UserLoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

/**
 * 用户注册
 * @param registerDTO 注册信息
 * @return 注册结果
 * @author sulele
 */
    @PostMapping("/register")
    public Result<User> register(@RequestBody UserRegisterDTO registerDTO) {
        try {
            logger.info("收到注册请求: {}", registerDTO);
            if (registerDTO.getUsername() == null || registerDTO.getPassword() == null) {
                return Result.error("用户名和密码不能为空");
            }
            User user = userService.register(registerDTO);
            return Result.success(user);
        } catch (Exception e) {
            logger.error("注册失败", e);
            return Result.error("注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录
     * @author sulele
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO loginDTO) {
        try {
            logger.info("收到登录请求: {}", loginDTO.getUsername());
            if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
                return Result.error("用户名和密码不能为空");
            }
            UserLoginVO loginVO = userService.login(loginDTO);
            return Result.success(loginVO);
        } catch (Exception e) {
            logger.error("登录失败", e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }

 /*   @PostMapping("/login")
   public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserDTO user = UserHolder.getUser();
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
   } */

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     * @author sulele
     */
    @GetMapping("/{userId}")
    public Result<User> getUserInfo(@PathVariable Long userId) {
        try {
            logger.info("获取用户信息: {}", userId);
            User user = userService.getUserById(userId);
            user.setPassword(null); // 清除密码
            return Result.success(user);
        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param updateDTO 更新信息
     * @return 更新结果
     * @author sulele
     */
    @PutMapping("/{userId}")
    public Result<Void> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO updateDTO) {
        try {
            logger.info("更新用户信息: {}, {}", userId, updateDTO);
            userService.updateUser(userId, updateDTO);
            return Result.success(null);
        } catch (Exception e) {
            logger.error("更新用户信息失败", e);
            return Result.error("更新用户信息失败: " + e.getMessage());
        }
    }
} 