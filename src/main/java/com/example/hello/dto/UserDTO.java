package com.example.hello.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserDTO {
    private Long id;
    
    @JsonProperty("user_name")
    private String userName;
    
    private String phone;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("eco_points")
    private Integer ecoPoints;
}