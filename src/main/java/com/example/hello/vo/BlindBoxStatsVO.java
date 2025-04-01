package com.example.hello.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BlindBoxStatsVO {
    private Long boxId;
    private String name;
    private Integer totalSales;
    private BigDecimal totalAmount;
    private List<DailyStat> dailyStats;
    private Integer marketRank;
    
    @Data
    public static class DailyStat {
        private LocalDate date;
        private Integer sales;
        private BigDecimal amount;
    }
} 