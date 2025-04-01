package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrderStatsVO {
    @JsonProperty("total_orders")
    private Integer totalOrders;
    
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    
    @JsonProperty("status_distribution")
    private Map<String, Integer> statusDistribution;
    
    @JsonProperty("daily_stats")
    private List<DailyStat> dailyStats;
    
    @JsonProperty("top_products")
    private List<TopProduct> topProducts;
    
    @Data
    public static class DailyStat {
        private String date;
        private Integer orders;
        private BigDecimal amount;
    }
    
    @Data
    public static class TopProduct {
        @JsonProperty("box_id")
        private Long boxId;
        private String name;
        private Integer sales;
        private BigDecimal amount;
    }
} 