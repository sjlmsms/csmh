package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderCreateVO {
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    
    @JsonProperty("voucher_discount")
    private BigDecimal voucherDiscount;
    
    @JsonProperty("shipping_fee")
    private BigDecimal shippingFee;
    
    @JsonProperty("eco_discount")
    private BigDecimal ecoDiscount;
    
    @JsonProperty("final_amount")
    private BigDecimal finalAmount;
    
    @JsonProperty("qr_code")
    private String qrCode;
    
    @JsonProperty("create_time")
    private LocalDateTime createTime;
} 