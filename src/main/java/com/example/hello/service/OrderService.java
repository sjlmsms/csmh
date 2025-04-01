package com.example.hello.service;

import com.example.hello.dto.OrderStatusUpdateDTO;
import com.example.hello.dto.OrderRefundDTO;
import com.example.hello.dto.OrderCreateDTO;
import com.example.hello.vo.OrderStatusUpdateVO;
import com.example.hello.vo.OrderStatsVO;
import com.example.hello.vo.OrderLogisticsVO;
import com.example.hello.vo.OrderRefundVO;
import com.example.hello.vo.OrderCreateVO;
import com.example.hello.vo.OrderListVO;
import com.example.hello.vo.OrderDetailVO;

public interface OrderService {
    
    /**
     * 删除订单
     */
    void deleteOrder(Long orderId);
    
    /**
     * 更新订单状态
     */
    OrderStatusUpdateVO updateOrderStatus(Long orderId, OrderStatusUpdateDTO updateDTO);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long orderId, String reason);
    
    /**
     * 确认收货
     */
    void confirmReceipt(Long orderId);
    
    /**
     * 获取订单统计数据
     */
    OrderStatsVO getOrderStats(String timeRange, Long marketId);
    
    /**
     * 处理超时未支付订单
     */
    void handleTimeoutOrders();
    
    /**
     * 获取订单物流信息
     */
    OrderLogisticsVO getOrderLogistics(Long orderId);
    
    /**
     * 处理订单退款
     */
    OrderRefundVO handleRefund(Long orderId, OrderRefundDTO refundDTO);

    OrderCreateVO createOrder(OrderCreateDTO orderCreateDTO);

    OrderListVO getOrderList(Long userId);

    OrderDetailVO getOrderDetail(Long orderId);
} 