package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderListVO {
    private Integer total;
    private List<OrderItemVO> rows;

    @Data
    public static class OrderItemVO {
        @JsonProperty("order_id")
        private Long orderId;
        
        @JsonProperty("market_id")
        private Long marketId;
        
        @JsonProperty("total_amount")
        private BigDecimal totalAmount;
        
        @JsonProperty("final_amount")
        private BigDecimal finalAmount;
        
        private Integer status;
        
        @JsonProperty("create_time")
        private LocalDateTime createTime;
        
        private List<BoxItemVO> items;
    }

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
} 