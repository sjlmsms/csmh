package com.example.hello.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderStatusUpdateVO {
    // 订单ID   
    @JsonProperty("order_id")
    private Long orderId;
    // 新状态
    @JsonProperty("new_status")
    private Integer newStatus;
    // 更新时间 
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
} 