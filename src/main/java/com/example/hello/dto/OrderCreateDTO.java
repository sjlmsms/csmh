package com.example.hello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OrderCreateDTO {
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("market_id")
    private Long marketId;
    
    private List<OrderItemDTO> items;
    
    @JsonProperty("payment_method")
    private Integer paymentMethod;
    
    @Data
    public static class OrderItemDTO {
        @JsonProperty("box_id")
        private Long boxId;
        private Integer quantity;
    }
} 