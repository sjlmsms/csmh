package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("tb_blind_box")
public class BlindBox {
    @TableId(value = "box_id", type = IdType.AUTO)
    private Long boxId;
    private Long marketId;
    private String name;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private BigDecimal discountRate;
    private String category;
    private Integer stock;
    private Integer sales;
    private String carbonFootprint;
    private BigDecimal weeklyPrice;
    private String uniqueCode;
    private Boolean windowCategoryAlert;
    private LocalDateTime createTime;
    private LocalDate expiryDate;
    private Integer status;
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
} 