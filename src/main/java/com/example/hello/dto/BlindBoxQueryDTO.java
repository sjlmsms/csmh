package com.example.hello.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BlindBoxQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Long marketId;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
} 