package com.example.hello.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BlindBoxDetailVO {
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
    private String imageUrl;
    private List<BoxContentVO> contents;
    private MarketInfoVO marketInfo;
} 