package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tb_order")
public class Order {
    @TableId(value = "order_id", type = IdType.AUTO)
    // 订单ID
    private Long orderId;
    // 用户ID
    private Long userId;
    // 市场ID
    private Long marketId;
    // 优惠券ID
    private Long voucherId;
    // 总金额
    private BigDecimal totalAmount;
    // 优惠券折扣
    private BigDecimal voucherDiscount;
    // 运费
    private BigDecimal shippingFee;
    // 环保折扣
    private BigDecimal ecoDiscount;
    // 税费
    private BigDecimal taxAmount;
    // 环保积分
    private Integer ecoPointsEarned;
    // 支付方式
    private Integer paymentMethod;
    // 状态
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 二维码
    private String qrCode;
} 