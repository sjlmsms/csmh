package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewVO {
    @JsonProperty("review_id")
    private Long reviewId;
    
    @JsonProperty("user_id")
    private Long userId;
    
    private String username;
    
    @JsonProperty("user_icon")
    private String userIcon;
    
    @JsonProperty("star_rating")
    private Integer starRating;
    
    private String content;
    private List<String> images;
    
    @JsonProperty("like_count")
    private Integer likeCount;
    
    @JsonProperty("create_time")
    private LocalDateTime createTime;
} 