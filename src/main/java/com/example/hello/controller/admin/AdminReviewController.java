package com.example.hello.controller.admin;

import com.example.hello.common.Result;
import com.example.hello.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/reviews")
public class AdminReviewController {
    
    private final ReviewService reviewService;
    
    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 删除评价
     */
    @DeleteMapping("/{reviewId}")
    public Result<Void> deleteReview(@PathVariable Long reviewId) {
        try {
            log.info("删除评价请求: reviewId={}", reviewId);
            reviewService.deleteReview(reviewId);
            return Result.success(null);
        } catch (Exception e) {
            log.error("删除评价失败", e);
            return Result.error("删除评价失败: " + e.getMessage());
        }
    }
} 