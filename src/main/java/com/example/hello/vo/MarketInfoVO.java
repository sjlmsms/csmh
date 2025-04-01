package com.example.hello.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarketInfoVO {
    private String name;
    private String address;
    private BigDecimal distance;
    private String businessHours;
} 