package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRefundVO {
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("refund_status")
    private String refundStatus;
    
    @JsonProperty("refund_amount")
    private BigDecimal refundAmount;
    
    @JsonProperty("refund_time")
    private LocalDateTime refundTime;
} 