package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OrderLogisticsVO {
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("shipping_company")
    private String shippingCompany;
    
    @JsonProperty("shipping_no")
    private String shippingNo;
    
    @JsonProperty("logistics_info")
    private List<LogisticsInfo> logisticsInfo;
    
    @JsonProperty("estimated_delivery")
    private String estimatedDelivery;
    
    @Data
    public static class LogisticsInfo {
        private String time;
        private String status;
        private String location;
    }
} 