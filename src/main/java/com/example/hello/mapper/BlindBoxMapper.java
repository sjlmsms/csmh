package com.example.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hello.entity.BlindBox;
import com.example.hello.vo.BlindBoxStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BlindBoxMapper extends BaseMapper<BlindBox> {
    
    @Select("SELECT DATE(o.create_time) as date, " +
            "COUNT(od.box_id) as sales, " +
            "SUM(od.price * od.quantity) as amount " +
            "FROM tb_order o " +
            "JOIN tb_order_detail od ON o.order_id = od.order_id " +
            "WHERE od.box_id = #{boxId} " +
            "AND o.create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE(o.create_time)")
    List<BlindBoxStatsVO.DailyStat> getDailyStats(@Param("boxId") Long boxId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Select("SELECT COUNT(*) + 1 FROM tb_blind_box b1 " +
            "WHERE b1.market_id = #{marketId} " +
            "AND b1.sales > (SELECT sales FROM tb_blind_box b2 WHERE b2.box_id = #{boxId})")
    Integer getMarketRank(@Param("boxId") Long boxId, @Param("marketId") Long marketId);
} 