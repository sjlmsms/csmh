package com.example.hello.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BoxContentDTO {
    @JsonProperty("content_id")
    private Long productId;
    
    private Integer quantity;
} 