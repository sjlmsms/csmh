package com.example.hello.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReviewDTO {
    private Long userId;
    private Long orderId;
    private Long boxId;
    private Integer starRating;
    private String content;
    private List<String> images;
} 