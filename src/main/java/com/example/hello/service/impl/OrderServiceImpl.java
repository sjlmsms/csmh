package com.example.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hello.entity.Order;
import com.example.hello.dto.OrderStatusUpdateDTO;
import com.example.hello.dto.OrderRefundDTO;
import com.example.hello.dto.OrderCreateDTO;
import com.example.hello.mapper.OrderMapper;
import com.example.hello.service.OrderService;
import com.example.hello.vo.OrderStatusUpdateVO;
import com.example.hello.vo.OrderStatsVO;
import com.example.hello.vo.OrderRefundVO;
import com.example.hello.vo.OrderLogisticsVO;
import com.example.hello.vo.OrderCreateVO;
import com.example.hello.vo.OrderListVO;
import com.example.hello.vo.OrderDetailVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 只能删除未支付或已取消的订单
        if (order.getStatus() != 1 && order.getStatus() != 8) {
            throw new RuntimeException("只能删除未支付或已取消的订单");
        }
        
        this.removeById(orderId);
    }

    @Override
    @Transactional
    public OrderStatusUpdateVO updateOrderStatus(Long orderId, OrderStatusUpdateDTO updateDTO) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 验证状态流转是否合法
        validateStatusTransition(order.getStatus(), updateDTO.getStatus());
        
        // 更新状态
        order.setStatus(updateDTO.getStatus());
        this.updateById(order);
        
        // 返回更新结果
        OrderStatusUpdateVO updateVO = new OrderStatusUpdateVO();
        updateVO.setOrderId(orderId);
        updateVO.setNewStatus(updateDTO.getStatus());
        updateVO.setUpdateTime(LocalDateTime.now());
        return updateVO;
    }

    private void validateStatusTransition(Integer currentStatus, Integer newStatus) {
        // 状态流转验证逻辑
        Map<Integer, List<Integer>> allowedTransitions = new HashMap<>();
        allowedTransitions.put(1, List.of(2, 8));  // 待支付 -> 已支付,已取消
        allowedTransitions.put(2, List.of(3, 8));  // 已支付 -> 已发货,已取消
        allowedTransitions.put(3, List.of(4));      // 已发货 -> 待收货
        allowedTransitions.put(4, List.of(5));      // 待收货 -> 已收货
        allowedTransitions.put(5, List.of(6));      // 已收货 -> 待评价
        allowedTransitions.put(6, List.of(7));      // 待评价 -> 已评价
        
        List<Integer> allowed = allowedTransitions.get(currentStatus);
        if (allowed == null || !allowed.contains(newStatus)) {
            throw new RuntimeException("非法的状态变更");
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 只能取消待支付或已支付的订单
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new RuntimeException("只能取消待支付或已支付的订单");
        }
        
        // 更新订单状态为已取消
        order.setStatus(8);
        this.updateById(order);
    }

    @Override
    @Transactional
    public void confirmReceipt(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 只能确认待收货的订单
        if (order.getStatus() != 4) {
            throw new RuntimeException("只能确认待收货状态的订单");
        }
        
        // 更新订单状态为已收货
        order.setStatus(5);
        this.updateById(order);
    }

    @Override
    public OrderStatsVO getOrderStats(String timeRange, Long marketId) {
        // 创建返回对象
        OrderStatsVO statsVO = new OrderStatsVO();
        
        // 设置总订单数和总金额
        statsVO.setTotalOrders(150);
        statsVO.setTotalAmount(new BigDecimal("7500.00"));
        
        // 设置状态分布
        Map<String, Integer> statusMap = new HashMap<>();
        statusMap.put("pending_payment", 10);
        statusMap.put("paid", 30);
        statusMap.put("shipped", 50);
        statusMap.put("completed", 60);
        statsVO.setStatusDistribution(statusMap);
        
        // 设置每日统计数据
        List<OrderStatsVO.DailyStat> dailyStats = new ArrayList<>();
        OrderStatsVO.DailyStat dailyStat = new OrderStatsVO.DailyStat();
        dailyStat.setDate("2023-01-15");
        dailyStat.setOrders(15);
        dailyStat.setAmount(new BigDecimal("750.00"));
        dailyStats.add(dailyStat);
        statsVO.setDailyStats(dailyStats);
        
        // 设置热销商品数据
        List<OrderStatsVO.TopProduct> topProducts = new ArrayList<>();
        OrderStatsVO.TopProduct topProduct = new OrderStatsVO.TopProduct();
        topProduct.setBoxId(1L);
        topProduct.setName("周末特惠盲盒");
        topProduct.setSales(50);
        topProduct.setAmount(new BigDecimal("2500.00"));
        topProducts.add(topProduct);
        statsVO.setTopProducts(topProducts);
        
        return statsVO;
    }

    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    @Transactional
    @Override
    public void handleTimeoutOrders() {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(30);
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, 1)  // 待支付状态
                .lt(Order::getCreateTime, timeout);  // 创建时间超过30分钟
        
        List<Order> timeoutOrders = this.list(wrapper);
        
        if (!timeoutOrders.isEmpty()) {
            timeoutOrders.forEach(order -> {
                order.setStatus(8);  // 更新为已取消状态
            });
            this.updateBatchById(timeoutOrders);
        }
    }

    @Override
    @Transactional
    public OrderRefundVO handleRefund(Long orderId, OrderRefundDTO refundDTO) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 验证订单状态
        if (order.getStatus() != 8) {
            throw new RuntimeException("只能处理已取消订单的退款");
        }
        
        OrderRefundVO refundVO = new OrderRefundVO();
        refundVO.setOrderId(orderId);
        refundVO.setRefundTime(LocalDateTime.now());
        
        if ("approve".equals(refundDTO.getAction())) {
            if (refundDTO.getRefundAmount() == null) {
                throw new RuntimeException("退款金额不能为空");
            }
            if (refundDTO.getRefundAmount().compareTo(order.getTotalAmount()) > 0) {
                throw new RuntimeException("退款金额不能大于订单金额");
            }
            
            // TODO: 调用支付系统进行退款
            refundVO.setRefundStatus("completed");
            refundVO.setRefundAmount(refundDTO.getRefundAmount());
            
        } else if ("reject".equals(refundDTO.getAction())) {
            if (refundDTO.getReason() == null || refundDTO.getReason().trim().isEmpty()) {
                throw new RuntimeException("拒绝退款时必须提供原因");
            }
            
            refundVO.setRefundStatus("rejected");
            refundVO.setRefundAmount(BigDecimal.ZERO);
            
        } else {
            throw new RuntimeException("无效的退款处理动作");
        }
        
        return refundVO;
    }

    @Override
    public OrderLogisticsVO getOrderLogistics(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 创建一个模拟的物流信息响应
        OrderLogisticsVO logisticsVO = new OrderLogisticsVO();
        logisticsVO.setOrderId(orderId);
        logisticsVO.setShippingCompany("顺丰速运");
        logisticsVO.setShippingNo("SF" + orderId + "CN");
        logisticsVO.setEstimatedDelivery(LocalDate.now().plusDays(3).toString());
        
        // 添加一些模拟的物流轨迹
        OrderLogisticsVO.LogisticsInfo info = new OrderLogisticsVO.LogisticsInfo();
        info.setTime(LocalDateTime.now().toString());
        info.setStatus("已发货");
        info.setLocation("北京市朝阳区");
        
        logisticsVO.setLogisticsInfo(List.of(info));
        
        return logisticsVO;
    }

    @Override
    @Transactional
    public OrderCreateVO createOrder(OrderCreateDTO orderCreateDTO) {
        // 1. 验证请求参数
        if (orderCreateDTO.getUserId() == null || orderCreateDTO.getMarketId() == null) {
            throw new IllegalArgumentException("用户ID和超市ID不能为空");
        }
        
        // 2. 创建订单
        Order order = new Order();
        order.setUserId(orderCreateDTO.getUserId());
        order.setMarketId(orderCreateDTO.getMarketId());
        order.setPaymentMethod(orderCreateDTO.getPaymentMethod());
        order.setStatus(1); // 待支付状态
        order.setCreateTime(LocalDateTime.now());
        
        // 3. 设置订单金额相关信息
        BigDecimal totalAmount = new BigDecimal("118.00");  // 总金额
        BigDecimal voucherDiscount = new BigDecimal("10.00");  // 优惠券折扣
        BigDecimal shippingFee = BigDecimal.ZERO;  // 运费
        BigDecimal ecoDiscount = new BigDecimal("5.00");  // 环保折扣
        
        order.setTotalAmount(totalAmount);
        order.setVoucherDiscount(voucherDiscount);
        order.setShippingFee(shippingFee);
        order.setEcoDiscount(ecoDiscount);
        order.setTaxAmount(new BigDecimal("0.00"));  // 税费
        order.setEcoPointsEarned(50);  // 环保积分
        
        // 4. 保存订单
        this.save(order);
        
        // 5. 返回创建结果
        OrderCreateVO orderCreateVO = new OrderCreateVO();
        orderCreateVO.setOrderId(order.getOrderId());
        orderCreateVO.setTotalAmount(order.getTotalAmount());
        orderCreateVO.setVoucherDiscount(order.getVoucherDiscount());
        orderCreateVO.setShippingFee(order.getShippingFee());
        orderCreateVO.setEcoDiscount(order.getEcoDiscount());
        orderCreateVO.setFinalAmount(calculateFinalAmount(order));
        orderCreateVO.setQrCode("https://example.com/qr/" + order.getOrderId());
        orderCreateVO.setCreateTime(order.getCreateTime());
        
        return orderCreateVO;
    }
    
    private BigDecimal calculateFinalAmount(Order order) {
        return order.getTotalAmount()
                .subtract(order.getVoucherDiscount())
                .subtract(order.getEcoDiscount())
                .add(order.getShippingFee());
    }

    @Override
    public OrderListVO getOrderList(Long userId) {
        // 构建查询条件
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }
        
        // 查询订单列表
        List<Order> orders = this.list(wrapper);
        
        // 构建返回数据
        OrderListVO orderListVO = new OrderListVO();
        orderListVO.setTotal(orders.size());
        
        List<OrderListVO.OrderItemVO> rows = orders.stream().map(order -> {
            OrderListVO.OrderItemVO itemVO = new OrderListVO.OrderItemVO();
            itemVO.setOrderId(order.getOrderId());
            itemVO.setMarketId(order.getMarketId());
            itemVO.setTotalAmount(order.getTotalAmount());
            itemVO.setFinalAmount(calculateFinalAmount(order));
            itemVO.setStatus(order.getStatus());
            itemVO.setCreateTime(order.getCreateTime());
            
            // 模拟盲盒数据
            OrderListVO.BoxItemVO boxItem = new OrderListVO.BoxItemVO();
            boxItem.setBoxId(1L);
            boxItem.setName("周末特惠盲盒");
            boxItem.setQuantity(2);
            boxItem.setPrice(new BigDecimal("59.00"));
            boxItem.setImageUrl("https://example.com/box1.jpg");
            
            itemVO.setItems(List.of(boxItem));
            return itemVO;
        }).collect(Collectors.toList());
        
        orderListVO.setRows(rows);
        return orderListVO;
    }

    @Override
    public OrderDetailVO getOrderDetail(Long orderId) {
        // 获取订单信息
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 构建返回数据
        OrderDetailVO detailVO = new OrderDetailVO();
        detailVO.setOrderId(order.getOrderId());
        detailVO.setUserId(order.getUserId());
        detailVO.setMarketId(order.getMarketId());
        detailVO.setTotalAmount(order.getTotalAmount());
        detailVO.setVoucherDiscount(order.getVoucherDiscount());
        detailVO.setShippingFee(order.getShippingFee());
        detailVO.setEcoDiscount(order.getEcoDiscount());
        detailVO.setFinalAmount(calculateFinalAmount(order));
        detailVO.setPaymentMethod(order.getPaymentMethod());
        detailVO.setStatus(order.getStatus());
        detailVO.setCreateTime(order.getCreateTime());
        detailVO.setQrCode("https://example.com/qr/" + orderId);
        
        // 设置盲盒数据
        OrderDetailVO.BoxItemVO boxItem = new OrderDetailVO.BoxItemVO();
        boxItem.setBoxId(1L);
        boxItem.setName("周末特惠盲盒");
        boxItem.setQuantity(2);
        boxItem.setPrice(new BigDecimal("59.00"));
        boxItem.setImageUrl("https://example.com/box1.jpg");
        detailVO.setItems(List.of(boxItem));
        
        // 设置超市信息
        OrderDetailVO.MarketInfo marketInfo = new OrderDetailVO.MarketInfo();
        marketInfo.setName("沃尔玛");
        marketInfo.setAddress("北京市朝阳区");
        marketInfo.setBusinessHours("08:00-22:00");
        detailVO.setMarketInfo(marketInfo);
        
        return detailVO;
    }
} 