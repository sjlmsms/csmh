package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlindBoxUpdateVO {
    @JsonProperty("box_id")
    private Long boxId;
    
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
} 