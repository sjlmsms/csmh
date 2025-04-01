package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderCancelVO {
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("refund_amount")
    private BigDecimal refundAmount;
    
    @JsonProperty("refund_status")
    private String refundStatus;
} 