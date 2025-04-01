package com.example.hello.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BoxContentVO {
    private Long productId;
    private String name;
    private Integer quantity;
    private String weight;
    private LocalDate expiryDate;
} 