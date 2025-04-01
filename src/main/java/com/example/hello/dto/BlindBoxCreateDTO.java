package com.example.hello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BlindBoxCreateDTO {
    @NotNull(message = "超市ID不能为空")
    @JsonProperty("market_id")
    private Long marketId;

    @NotBlank(message = "盲盒名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "原价不能为空")
    @Min(value = 0, message = "原价必须大于等于0")
    @JsonProperty("original_price")
    private BigDecimal originalPrice;

    @NotNull(message = "折扣价格不能为空")
    @Min(value = 0, message = "折扣价格必须大于等于0")
    @JsonProperty("discount_price")
    private BigDecimal discountPrice;

    @NotBlank(message = "商品分类不能为空")
    private String category;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量必须大于等于0")
    private Integer stock;

    @JsonProperty("carbon_footprint")
    private String carbonFootprint;

    @NotNull(message = "过期日期不能为空")
    @JsonProperty("expiry_date")
    private LocalDate expiryDate;

    @JsonProperty("window_category_alert")
    private Boolean windowCategoryAlert;

    @NotNull(message = "盲盒内容不能为空")
    private List<BoxContentDTO> contents;
} 