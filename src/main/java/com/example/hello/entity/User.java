package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tb_user")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    // 用户ID
    private Long userId;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 头像
    private String icon;
    // 环保积分
    private Integer ecoPoints;
    // 节省金额
    private BigDecimal savedAmount;
    // 减少垃圾
    private BigDecimal reducedWaste;
    // 徽章数量
    private Integer badgeCount;
    // 优惠券数量
    private Integer voucherCount;
    // 创建时间
    private LocalDateTime createTime;
    // 最后登录时间
    private LocalDateTime lastLogin;
} 