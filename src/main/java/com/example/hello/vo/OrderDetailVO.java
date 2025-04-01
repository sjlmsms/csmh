package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("market_id")
    private Long marketId;
    
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
    
    @JsonProperty("payment_method")
    private Integer paymentMethod;
    
    private Integer status;
    
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    
    @JsonProperty("qr_code")
    private String qrCode;
    
    private List<BoxItemVO> items;
    
    @JsonProperty("market_info")
    private MarketInfo marketInfo;
    
    @Data
    public static class BoxItemVO {
        @JsonProperty("box_id")
        private Long boxId;
        private String name;
        private Integer quantity;
        private BigDecimal price;
        
        @JsonProperty("image_url")
        private String imageUrl;
    }
    
    @Data
    public static class MarketInfo {
        private String name;
        private String address;
        
        @JsonProperty("business_hours")
        private String businessHours;
    }
} 