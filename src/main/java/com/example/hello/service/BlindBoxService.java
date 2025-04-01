package com.example.hello.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.hello.dto.BlindBoxCreateDTO;
import com.example.hello.dto.BlindBoxQueryDTO;
import com.example.hello.dto.BlindBoxUpdateDTO;
import com.example.hello.entity.BlindBox;
import com.example.hello.vo.BlindBoxDetailVO;
import com.example.hello.vo.BlindBoxFormVO;
import com.example.hello.vo.BlindBoxStatsVO;
import com.example.hello.vo.BlindBoxUpdateVO;

import java.time.LocalDate;
import java.util.List;

public interface BlindBoxService {
    // 分页查询盲盒列表
    Page<BlindBox> queryBlindBoxes(BlindBoxQueryDTO queryDTO);
    
    // 获取盲盒详情
    BlindBoxDetailVO getBlindBoxDetail(Long boxId);
    
    // 创建盲盒
    BlindBox createBlindBox(BlindBoxCreateDTO createDTO);
    
    // 更新盲盒
    BlindBoxUpdateVO updateBlindBox(Long boxId, BlindBoxUpdateDTO updateDTO);
    
    // 删除盲盒
    void deleteBlindBox(Long boxId);
    
    // 批量更新盲盒状态
    void batchUpdateStatus(List<Long> boxIds, Integer status);
    
    // 获取表单选项
    BlindBoxFormVO getFormOptions();
    
    // 获取销售统计
    BlindBoxStatsVO getStats(Long boxId, LocalDate startDate, LocalDate endDate);
} 