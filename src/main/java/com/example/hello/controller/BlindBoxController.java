package com.example.hello.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hello.common.Result;
import com.example.hello.dto.BlindBoxQueryDTO;
import com.example.hello.entity.BlindBox;
import com.example.hello.service.BlindBoxService;
import com.example.hello.vo.BlindBoxDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/blind-boxes")
public class BlindBoxController {
    private static final Logger logger = LoggerFactory.getLogger(BlindBoxController.class);
    private final BlindBoxService blindBoxService;

    public BlindBoxController(BlindBoxService blindBoxService) {
        this.blindBoxService = blindBoxService;
    }

    /**
     * 获取盲盒列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param marketId 超市ID
     * @param category 商品分类
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     */
    @GetMapping
    public Result<Page<BlindBox>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long marketId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        try {
            BlindBoxQueryDTO queryDTO = new BlindBoxQueryDTO();
            queryDTO.setPage(page);
            queryDTO.setPageSize(pageSize);
            queryDTO.setMarketId(marketId);
            queryDTO.setCategory(category);
            queryDTO.setMinPrice(minPrice);
            queryDTO.setMaxPrice(maxPrice);

            Page<BlindBox> result = blindBoxService.queryBlindBoxes(queryDTO);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取盲盒列表失败", e);
            return Result.error("获取盲盒列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取盲盒详情
     * @param boxId 盲盒ID
     */
    @GetMapping("/{boxId}")
    public Result<BlindBoxDetailVO> getDetail(@PathVariable Long boxId) {
        try {
            BlindBoxDetailVO detail = blindBoxService.getBlindBoxDetail(boxId);
            return Result.success(detail);
        } catch (Exception e) {
            logger.error("获取盲盒详情失败", e);
            return Result.error("获取盲盒详情失败: " + e.getMessage());
        }
    }
} 