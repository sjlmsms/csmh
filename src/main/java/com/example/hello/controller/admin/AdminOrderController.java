package com.example.hello.controller.admin;

import com.example.hello.common.Result;
import com.example.hello.dto.OrderStatusUpdateDTO;
import com.example.hello.dto.OrderRefundDTO;
import com.example.hello.service.OrderService;
import com.example.hello.vo.OrderStatusUpdateVO;
import com.example.hello.vo.OrderStatsVO;
import com.example.hello.vo.OrderRefundVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @DeleteMapping("/{orderId}")
    public Result<Void> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return Result.success("订单删除成功", null);
        } catch (Exception e) {
            logger.error("删除订单失败", e);
            return Result.error("删除订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public Result<OrderStatusUpdateVO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDTO updateDTO) {
        try {
            OrderStatusUpdateVO updateVO = orderService.updateOrderStatus(orderId, updateDTO);
            return Result.success("订单状态更新成功", updateVO);
        } catch (Exception e) {
            logger.error("更新订单状态失败", e);
            return Result.error("更新订单状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public Result<OrderStatsVO> getOrderStats(
            @RequestParam(required = false, defaultValue = "today") String timeRange,
            @RequestParam(required = false) Long marketId) {
        try {
            OrderStatsVO stats = orderService.getOrderStats(timeRange, marketId);
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取订单统计失败", e);
            return Result.error("获取订单统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/{orderId}/refund")
    public Result<OrderRefundVO> handleRefund(
            @PathVariable Long orderId,
            @RequestBody OrderRefundDTO refundDTO) {
        try {
            OrderRefundVO refundVO = orderService.handleRefund(orderId, refundDTO);
            String msg = "approve".equals(refundDTO.getAction()) ? "退款处理成功" : "退款已拒绝";
            return Result.success(msg, refundVO);
        } catch (Exception e) {
            logger.error("退款处理失败", e);
            return Result.error("退款处理失败: " + e.getMessage());
        }
    }
} 