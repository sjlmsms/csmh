package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tb_supermarket")
public class Supermarket {
    @TableId(value = "market_id", type = IdType.AUTO)
    private Long marketId;
    private String name;
    private String address;
    private BigDecimal distance;
    private String businessHours;
    private BigDecimal rating;
    private Integer status;
    private BigDecimal latitude;
    private BigDecimal longitude;
} 