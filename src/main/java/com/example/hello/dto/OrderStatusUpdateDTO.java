package com.example.hello.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private Integer status;
    private String remark;
} 