package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderConfirmReceiptVO {
    @JsonProperty("order_id")
    private Long orderId;
    
    private Integer status;
    
    @JsonProperty("confirm_time")
    private LocalDateTime confirmTime;
} 