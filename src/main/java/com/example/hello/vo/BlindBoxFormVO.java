package com.example.hello.vo;

import lombok.Data;

import java.util.List;

@Data
public class BlindBoxFormVO {
    private List<String> categories;
    private List<MarketOption> markets;
    private List<ProductOption> products;
    
    @Data
    public static class MarketOption {
        private Long marketId;
        private String name;
    }
    
    @Data
    public static class ProductOption {
        private Long productId;
        private String name;
        private String category;
    }
} 