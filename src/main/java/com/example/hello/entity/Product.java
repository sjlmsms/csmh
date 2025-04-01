package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("tb_product")
public class Product {
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;
    private String name;
    private String category;
    private String brand;
    private String weight;
    private LocalDate expiryDate;
    private BigDecimal originalPrice;
    private String imageUrl;
} 