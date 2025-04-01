package com.example.hello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderRefundDTO {
    private String action;  // approve-同意,reject-拒绝
    
    @JsonProperty("refund_amount")
    private BigDecimal refundAmount;
    
    private String reason;
} 