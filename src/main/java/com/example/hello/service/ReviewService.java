package com.example.hello.service;

import com.example.hello.dto.ReviewDTO;
import java.util.Map;

public interface ReviewService {
    Map<String, Object> submitReview(ReviewDTO reviewDTO);
    Map<String, Object> getReviewList(Long boxId, Integer page, Integer pageSize);
    void deleteReview(Long reviewId);
} 