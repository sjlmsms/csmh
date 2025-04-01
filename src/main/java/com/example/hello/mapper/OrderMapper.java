package com.example.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hello.entity.Order;
import com.example.hello.vo.OrderStatsVO.DailyStat;
import com.example.hello.vo.OrderStatsVO.TopProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {
    
    @Select("SELECT DATE(create_time) as date, " +
            "COUNT(*) as orders, " +
            "SUM(total_amount) as amount " +
            "FROM tb_order " +
            "WHERE create_time >= #{startDate} " +
            "AND create_time < #{endDate} " +
            "AND market_id = #{marketId} " +
            "GROUP BY DATE(create_time)")
    List<DailyStat> getDailyStats(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 @Param("marketId") Long marketId);

    @Select("SELECT b.box_id, b.name, " +
            "COUNT(*) as sales, " +
            "SUM(od.price * od.quantity) as amount " +
            "FROM tb_order_detail od " +
            "JOIN tb_blind_box b ON od.box_id = b.box_id " +
            "JOIN tb_order o ON od.order_id = o.order_id " +
            "WHERE o.create_time >= #{startDate} " +
            "AND o.create_time < #{endDate} " +
            "AND o.market_id = #{marketId} " +
            "GROUP BY b.box_id, b.name " +
            "ORDER BY sales DESC " +
            "LIMIT 10")
    List<TopProduct> getTopProducts(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("marketId") Long marketId);

    @Select("SELECT status, COUNT(*) as count " +
            "FROM tb_order " +
            "WHERE create_time >= #{startDate} " +
            "AND create_time < #{endDate} " +
            "AND market_id = #{marketId} " +
            "GROUP BY status")
    List<Map<String, Object>> getStatusDistribution(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  @Param("marketId") Long marketId);
} 