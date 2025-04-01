package com.example.hello.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hello.entity.Review;
import com.example.hello.mapper.ReviewMapper;
import com.example.hello.service.ReviewService;
import com.example.hello.dto.ReviewDTO;
import com.example.hello.vo.ReviewVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    
    private final ObjectMapper objectMapper;
    
    public ReviewServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Map<String, Object> submitReview(ReviewDTO reviewDTO) {
        // 创建评价
        Review review = new Review();
        review.setUserId(reviewDTO.getUserId());
        review.setOrderId(reviewDTO.getOrderId());
        review.setBoxId(reviewDTO.getBoxId());
        review.setStarRating(reviewDTO.getStarRating());
        review.setContent(reviewDTO.getContent());
        review.setCreateTime(LocalDateTime.now());
        review.setLikeCount(0);
        
        try {
            review.setImages(objectMapper.writeValueAsString(reviewDTO.getImages()));
        } catch (Exception e) {
            throw new RuntimeException("图片处理失败", e);
        }
        
        this.save(review);
        
        Map<String, Object> result = new HashMap<>();
        result.put("review_id", review.getReviewId());
        result.put("create_time", review.getCreateTime());
        
        return result;
    }

    @Override
    public Map<String, Object> getReviewList(Long boxId, Integer page, Integer pageSize) {
        // 查询评价列表
        Page<ReviewVO> pageInfo = new Page<>(page, pageSize);
        Page<ReviewVO> reviewPage = baseMapper.getReviewPage(pageInfo, boxId);
        
        // 处理每条评价的图片字段，将JSON字符串转换为List
        for (ReviewVO review : reviewPage.getRecords()) {
            try {
                if (review.getImages() != null) {
                    List<String> imageList = objectMapper.readValue(review.getImages().toString(), 
                            new TypeReference<List<String>>() {});
                    review.setImages(imageList);
                }
            } catch (Exception ignored) {
                review.setImages(null);
            }
        }
        
        // 查询平均评分
        Double avgRating = baseMapper.getAverageRating(boxId);
        
        // 组装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", reviewPage.getTotal());
        result.put("average_rating", avgRating != null ? avgRating : 0.0);
        result.put("rows", reviewPage.getRecords());
        
        return result;
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        // 检查评价是否存在
        Review review = this.getById(reviewId);
        if (review == null) {
            throw new RuntimeException("评价不存在");
        }
        
        // 直接从数据库删除
        this.removeById(reviewId);
    }
} 