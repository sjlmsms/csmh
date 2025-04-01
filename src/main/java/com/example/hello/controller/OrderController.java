package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.OrderCancelDTO;
import com.example.hello.dto.OrderCreateDTO;
import com.example.hello.service.OrderService;
import com.example.hello.vo.OrderCancelVO;
import com.example.hello.vo.OrderConfirmReceiptVO;
import com.example.hello.vo.OrderCreateVO;
import com.example.hello.vo.OrderDetailVO;
import com.example.hello.vo.OrderLogisticsVO;
import com.example.hello.vo.OrderListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Result<OrderCreateVO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        log.debug("接收到创建订单请求: {}", orderCreateDTO);
        try {
            OrderCreateVO orderCreateVO = orderService.createOrder(orderCreateDTO);
            return Result.success("订单创建成功", orderCreateVO);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/cancel")
    public Result<OrderCancelVO> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody(required = false) OrderCancelDTO cancelDTO) {
        try {
            String reason = cancelDTO != null ? cancelDTO.getReason() : null;
            orderService.cancelOrder(orderId, reason);
            
            // 构建取消响应
            OrderCancelVO cancelVO = new OrderCancelVO();
            cancelVO.setOrderId(orderId);
            cancelVO.setRefundStatus("processing");
            // TODO: 计算退款金额
            return Result.success("订单取消成功", cancelVO);
        } catch (Exception e) {
            log.error("取消订单失败", e);
            return Result.error("取消订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/confirm-receipt")
    public Result<OrderConfirmReceiptVO> confirmReceipt(@PathVariable Long orderId) {
        try {
            orderService.confirmReceipt(orderId);
            
            // 构建确认收货响应
            OrderConfirmReceiptVO receiptVO = new OrderConfirmReceiptVO();
            receiptVO.setOrderId(orderId);
            receiptVO.setStatus(5);  // 已收货状态
            receiptVO.setConfirmTime(LocalDateTime.now());
            return Result.success("收货确认成功", receiptVO);
        } catch (Exception e) {
            log.error("确认收货失败", e);
            return Result.error("确认收货失败: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}/logistics")
    public Result<OrderLogisticsVO> getLogistics(@PathVariable Long orderId) {
        try {
            // TODO: 实现物流信息查询逻辑
            OrderLogisticsVO logisticsVO = new OrderLogisticsVO();
            logisticsVO.setOrderId(orderId);
            logisticsVO.setShippingCompany("顺丰速运");
            logisticsVO.setShippingNo("SF123456789");
            // ... 设置其他物流信息
            return Result.success(logisticsVO);
        } catch (Exception e) {
            log.error("获取物流信息失败", e);
            return Result.error("获取物流信息失败: " + e.getMessage());
        }
    }

    @GetMapping
    public Result<OrderListVO> getOrderList(@RequestParam(value = "user_id", required = false) Long userId) {
        try {
            OrderListVO orderListVO = orderService.getOrderList(userId);
            return Result.success("success", orderListVO);
        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            return Result.error("获取订单列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable Long orderId) {
        try {
            OrderDetailVO orderDetail = orderService.getOrderDetail(orderId);
            return Result.success("success", orderDetail);
        } catch (Exception e) {
            log.error("获取订单详情失败", e);
            return Result.error("获取订单详情失败: " + e.getMessage());
        }
    }
} 