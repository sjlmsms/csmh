package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfoVO {
    private Long id;
    
    @JsonProperty("user_name")
    private String userName;
    
    private String phone;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("eco_points")
    private Integer ecoPoints;
} 