package com.example.hello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BlindBoxUpdateDTO {
    @JsonProperty("market_id")
    private Long marketId;

    private String name;

    private String description;

    @JsonProperty("original_price")
    @Min(value = 0, message = "原价必须大于等于0")
    private BigDecimal originalPrice;

    @JsonProperty("discount_price")
    @Min(value = 0, message = "折扣价格必须大于等于0")
    private BigDecimal discountPrice;

    private String category;

    @Min(value = 0, message = "库存数量必须大于等于0")
    private Integer stock;

    @JsonProperty("carbon_footprint")
    private String carbonFootprint;

    @JsonProperty("expiry_date")
    private LocalDate expiryDate;

    @JsonProperty("window_category_alert")
    private Boolean windowCategoryAlert;

    private List<BoxContentDTO> contents;
} 