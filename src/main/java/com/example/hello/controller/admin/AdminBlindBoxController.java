package com.example.hello.controller.admin;

import com.example.hello.common.Result;
import com.example.hello.dto.BlindBoxCreateDTO;
import com.example.hello.dto.BlindBoxUpdateDTO;
import com.example.hello.entity.BlindBox;
import com.example.hello.service.BlindBoxService;
import com.example.hello.vo.BlindBoxFormVO;
import com.example.hello.vo.BlindBoxStatsVO;
import com.example.hello.vo.BlindBoxUpdateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/blind-boxes")
public class AdminBlindBoxController {
    private static final Logger logger = LoggerFactory.getLogger(AdminBlindBoxController.class);
    private final BlindBoxService blindBoxService;

    public AdminBlindBoxController(BlindBoxService blindBoxService) {
        this.blindBoxService = blindBoxService;
    }

    /**
     * 创建盲盒
     */
    @PostMapping
    public Result<BlindBox> create(@Valid @RequestBody BlindBoxCreateDTO createDTO) {
        try {
            logger.info("创建盲盒请求参数: {}", createDTO);
            BlindBox blindBox = blindBoxService.createBlindBox(createDTO);
            return Result.success(blindBox);
        } catch (Exception e) {
            logger.error("创建盲盒失败", e);
            return Result.error("创建盲盒失败: " + e.getMessage());
        }
    }

    /**
     * 更新盲盒信息
     */
    @PutMapping("/{boxId}")
    public Result<BlindBoxUpdateVO> update(@PathVariable Long boxId, @Valid @RequestBody BlindBoxUpdateDTO updateDTO) {
        try {
            BlindBoxUpdateVO updateVO = blindBoxService.updateBlindBox(boxId, updateDTO);
            return Result.success("更新成功", updateVO);
        } catch (Exception e) {
            logger.error("更新盲盒失败", e);
            return Result.error("更新盲盒失败: " + e.getMessage());
        }
    }

    /**
     * 删除盲盒
     */
    @DeleteMapping("/{boxId}")
    public Result<Void> delete(@PathVariable Long boxId) {
        try {
            blindBoxService.deleteBlindBox(boxId);
            return Result.success(null);
        } catch (Exception e) {
            logger.error("删除盲盒失败", e);
            return Result.error("删除盲盒失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新盲盒状态
     */
    @PutMapping("/batch-status")
    public Result<Map<String, Integer>> batchUpdateStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> boxIdInts = (List<Integer>) params.get("box_ids");
            Integer status = (Integer) params.get("status");
            
            // 将 Integer 类型的 ID 转换为 Long 类型
            List<Long> boxIds = boxIdInts.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            
            blindBoxService.batchUpdateStatus(boxIds, status);
            
            Map<String, Integer> result = Map.of(
                "success_count", boxIds.size(),
                "fail_count", 0
            );
            return Result.success(result);
        } catch (Exception e) {
            logger.error("批量更新状态失败", e);
            return Result.error("批量更新状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取表单选项
     */
    @GetMapping("/form-options")
    public Result<BlindBoxFormVO> getFormOptions() {
        try {
            BlindBoxFormVO options = blindBoxService.getFormOptions();
            return Result.success(options);
        } catch (Exception e) {
            logger.error("获取表单选项失败", e);
            return Result.error("获取表单选项失败: " + e.getMessage());
        }
    }

    /**
     * 获取销售统计
     */
    @GetMapping("/{boxId}/stats")
    public Result<BlindBoxStatsVO> getStats(
            @PathVariable Long boxId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            BlindBoxStatsVO stats = blindBoxService.getStats(boxId, startDate, endDate);
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取销售统计失败", e);
            return Result.error("获取销售统计失败: " + e.getMessage());
        }
    }
} 