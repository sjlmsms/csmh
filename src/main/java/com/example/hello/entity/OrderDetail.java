package com.example.hello.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("tb_order_detail")
public class OrderDetail {
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long detailId;
    private Long orderId;
    private Long boxId;
    private Integer quantity;
    private BigDecimal price;
} 