package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_box_content")
public class BoxContent {
    @TableId(value = "content_id", type = IdType.AUTO)
    private Long contentId;
    private Long boxId;
    private Long productId;
    private Integer quantity;
} 