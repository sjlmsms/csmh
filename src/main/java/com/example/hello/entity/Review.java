package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long reviewId;
    private Long userId;
    private Long orderId;
    private Long boxId;
    private String coverStatus;
    private Integer starRating;
    private String content;
    private Integer likeCount;
    private String images;  // JSON字符串
    private LocalDateTime createTime;
} 