package com.example.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hello.entity.Review;
import com.example.hello.vo.ReviewVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReviewMapper extends BaseMapper<Review> {
    @Select("<script>" +
            "SELECT r.review_id, r.user_id, r.star_rating, r.content, " +
            "r.images, r.like_count, r.create_time, " +
            "u.username, u.icon as user_icon " +
            "FROM tb_review r " +
            "LEFT JOIN tb_user u ON r.user_id = u.user_id " +
            "WHERE 1=1 " +
            "<if test='boxId != null'> AND r.box_id = #{boxId} </if> " +
            "ORDER BY r.create_time DESC" +
            "</script>")
    Page<ReviewVO> getReviewPage(Page<ReviewVO> page, @Param("boxId") Long boxId);

    @Select("<script>" +
            "SELECT AVG(star_rating) " +
            "FROM tb_review " +
            "WHERE 1=1 " +
            "<if test='boxId != null'> AND box_id = #{boxId} </if>" +
            "</script>")
    Double getAverageRating(@Param("boxId") Long boxId);
} 