package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.ReviewDTO;
import com.example.hello.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    
    private final ReviewService reviewService;
    
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 提交评价
     */
    @PostMapping
    public Result<Map<String, Object>> submitReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            log.info("提交评价请求: {}", reviewDTO);
            Map<String, Object> result = reviewService.submitReview(reviewDTO);
            return Result.success(result);
        } catch (Exception e) {
            log.error("提交评价失败", e);
            return Result.error("提交评价失败: " + e.getMessage());
        }
    }

    /**
     * 获取评价列表
     */
    @GetMapping
    public Result<Map<String, Object>> getReviewList(
            @RequestParam(required = false) Long boxId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            log.info("获取评价列表请求: boxId={}, page={}, pageSize={}", boxId, page, pageSize);
            Map<String, Object> result = reviewService.getReviewList(boxId, page, pageSize);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取评价列表失败", e);
            return Result.error("获取评价列表失败: " + e.getMessage());
        }
    }
} 