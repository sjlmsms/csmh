package com.example.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.hello.dto.BlindBoxCreateDTO;
import com.example.hello.dto.BlindBoxQueryDTO;
import com.example.hello.dto.BoxContentDTO;
import com.example.hello.dto.BlindBoxUpdateDTO;
import com.example.hello.entity.BlindBox;
import com.example.hello.entity.BoxContent;
import com.example.hello.entity.Product;
import com.example.hello.entity.Supermarket;
import com.example.hello.mapper.BlindBoxMapper;
import com.example.hello.mapper.BoxContentMapper;
import com.example.hello.mapper.ProductMapper;
import com.example.hello.mapper.SupermarketMapper;
import com.example.hello.service.BlindBoxService;
import com.example.hello.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@Service
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxMapper, BlindBox> implements BlindBoxService {
    
    private final BoxContentMapper boxContentMapper;
    private final SupermarketMapper supermarketMapper;
    private final ProductMapper productMapper;
    private static final Logger log = LoggerFactory.getLogger(BlindBoxServiceImpl.class);
    
    public BlindBoxServiceImpl(BoxContentMapper boxContentMapper, 
                              SupermarketMapper supermarketMapper,
                              ProductMapper productMapper) {
        this.boxContentMapper = boxContentMapper;
        this.supermarketMapper = supermarketMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Page<BlindBox> queryBlindBoxes(BlindBoxQueryDTO queryDTO) {
        LambdaQueryWrapper<BlindBox> wrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (queryDTO.getMarketId() != null) {
            wrapper.eq(BlindBox::getMarketId, queryDTO.getMarketId());
        }
        if (queryDTO.getCategory() != null) {
            wrapper.eq(BlindBox::getCategory, queryDTO.getCategory());
        }
        if (queryDTO.getMinPrice() != null) {
            wrapper.ge(BlindBox::getDiscountPrice, queryDTO.getMinPrice());
        }
        if (queryDTO.getMaxPrice() != null) {
            wrapper.le(BlindBox::getDiscountPrice, queryDTO.getMaxPrice());
        }
        
        // 分页查询
        Page<BlindBox> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        return this.page(page, wrapper);
    }

    @Override
    public BlindBoxDetailVO getBlindBoxDetail(Long boxId) {
        // 1. 获取盲盒基本信息
        BlindBox blindBox = this.getById(boxId);
        if (blindBox == null) {
            throw new RuntimeException("盲盒不存在");
        }

        // 2. 获取盲盒内容
        LambdaQueryWrapper<BoxContent> contentWrapper = new LambdaQueryWrapper<>();
        contentWrapper.eq(BoxContent::getBoxId, boxId);
        List<BoxContent> contents = boxContentMapper.selectList(contentWrapper);

        // 3. 获取超市信息
        Supermarket supermarket = supermarketMapper.selectById(blindBox.getMarketId());
        if (supermarket == null) {
            throw new RuntimeException("超市信息不存在");
        }

        // 4. 组装返回数据
        BlindBoxDetailVO detailVO = new BlindBoxDetailVO();
        BeanUtils.copyProperties(blindBox, detailVO);

        // 5. 设置盲盒内容
        List<BoxContentVO> contentVOList = contents.stream()
                .map(content -> {
                    BoxContentVO vo = new BoxContentVO();
                    Product product = productMapper.selectById(content.getProductId());
                    if (product == null) {
                        // 记录错误日志
                        log.error("商品不存在, productId: {}", content.getProductId());
                        // 跳过这个商品
                        return null;
                    }
                    vo.setProductId(content.getProductId());
                    vo.setName(product.getName());
                    vo.setQuantity(content.getQuantity());
                    vo.setWeight(product.getWeight());
                    vo.setExpiryDate(product.getExpiryDate());
                    return vo;
                })
                .filter(vo -> vo != null)  // 过滤掉空值
                .collect(Collectors.toList());
        detailVO.setContents(contentVOList);

        // 6. 设置超市信息
        MarketInfoVO marketInfoVO = new MarketInfoVO();
        BeanUtils.copyProperties(supermarket, marketInfoVO);
        detailVO.setMarketInfo(marketInfoVO);

        return detailVO;
    }

    @Override
    @Transactional
    public BlindBox createBlindBox(BlindBoxCreateDTO createDTO) {
        // 1. 参数校验
        if (createDTO.getMarketId() == null) {
            throw new RuntimeException("超市ID不能为空");
        }
        if (createDTO.getName() == null || createDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("盲盒名称不能为空");
        }
        if (createDTO.getOriginalPrice() == null) {
            throw new RuntimeException("原价不能为空");
        }
        if (createDTO.getDiscountPrice() == null) {
            throw new RuntimeException("折扣价格不能为空");
        }
        if (createDTO.getCategory() == null || createDTO.getCategory().trim().isEmpty()) {
            throw new RuntimeException("商品分类不能为空");
        }
        if (createDTO.getStock() == null || createDTO.getStock() < 0) {
            throw new RuntimeException("库存数量不能为空且不能小于0");
        }
        if (createDTO.getExpiryDate() == null) {
            throw new RuntimeException("过期日期不能为空");
        }
        if (createDTO.getContents() == null || createDTO.getContents().isEmpty()) {
            throw new RuntimeException("盲盒内容不能为空");
        }

        // 2. 创建盲盒基本信息
        BlindBox blindBox = new BlindBox();
        BeanUtils.copyProperties(createDTO, blindBox);
        
        // 3. 设置其他字段
        blindBox.setCreateTime(LocalDateTime.now());
        blindBox.setSales(0);
        blindBox.setStatus(1); // 默认上架

        // 4. 计算折扣率
        try {
            BigDecimal discountRate = createDTO.getDiscountPrice()
                    .divide(createDTO.getOriginalPrice(), 2, RoundingMode.HALF_UP);
            // 验证折扣率是否在合理范围内 (0-1)
            if (discountRate.compareTo(BigDecimal.ZERO) <= 0 || discountRate.compareTo(BigDecimal.ONE) > 0) {
                throw new RuntimeException("折扣率必须在0-1之间");
            }
            blindBox.setDiscountRate(discountRate);
        } catch (ArithmeticException e) {
            throw new RuntimeException("计算折扣率时发生错误：" + e.getMessage());
        }
        
        // 5. 保存盲盒
        this.save(blindBox);
        
        // 6. 验证商品是否存在并保存盲盒内容
        for (BoxContentDTO contentDTO : createDTO.getContents()) {
            if (contentDTO.getProductId() == null) {
                throw new RuntimeException("商品ID不能为空");
            }
            if (contentDTO.getQuantity() == null || contentDTO.getQuantity() <= 0) {
                throw new RuntimeException("商品数量必须大于0");
            }
            // 验证商品是否存在
            Product product = productMapper.selectById(contentDTO.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在，ID: " + contentDTO.getProductId());
            }
            
            BoxContent content = new BoxContent();
            content.setBoxId(blindBox.getBoxId());
            content.setProductId(contentDTO.getProductId());
            content.setQuantity(contentDTO.getQuantity());
            boxContentMapper.insert(content);
        }
        
        return blindBox;
    }

    @Override
    @Transactional
    public BlindBoxUpdateVO updateBlindBox(Long boxId, BlindBoxUpdateDTO updateDTO) {
        // 1. 检查盲盒是否存在
        BlindBox blindBox = this.getById(boxId);
        if (blindBox == null) {
            throw new RuntimeException("盲盒不存在");
        }

        // 2. 更新非空字段
        if (updateDTO.getName() != null) {
            blindBox.setName(updateDTO.getName());
        }
        if (updateDTO.getStock() != null) {
            blindBox.setStock(updateDTO.getStock());
        }

        // 设置更新时间
        LocalDateTime now = LocalDateTime.now();
        blindBox.setUpdateTime(now);

        // 3. 更新盲盒基本信息
        this.updateById(blindBox);

        // 4. 如果有内容更新，则更新盲盒内容
        if (updateDTO.getContents() != null && !updateDTO.getContents().isEmpty()) {
            // 先删除原有内容
            LambdaQueryWrapper<BoxContent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BoxContent::getBoxId, boxId);
            boxContentMapper.delete(wrapper);

            // 添加新内容
            for (BoxContentDTO contentDTO : updateDTO.getContents()) {
                if (contentDTO.getProductId() == null) {
                    throw new RuntimeException("商品ID不能为空");
                }
                if (contentDTO.getQuantity() == null || contentDTO.getQuantity() <= 0) {
                    throw new RuntimeException("商品数量必须大于0");
                }
                
                BoxContent content = new BoxContent();
                content.setBoxId(boxId);
                content.setProductId(contentDTO.getProductId());
                content.setQuantity(contentDTO.getQuantity());
                boxContentMapper.insert(content);
            }
        }

        // 5. 返回更新结果
        BlindBoxUpdateVO updateVO = new BlindBoxUpdateVO();
        updateVO.setBoxId(boxId);
        updateVO.setUpdateTime(now);
        return updateVO;
    }

    @Override
    @Transactional
    public void deleteBlindBox(Long boxId) {
        // 1. 检查盲盒是否存在
        BlindBox blindBox = this.getById(boxId);
        if (blindBox == null) {
            throw new RuntimeException("盲盒不存在");
        }

        // 2. 删除盲盒内容
        LambdaQueryWrapper<BoxContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BoxContent::getBoxId, boxId);
        boxContentMapper.delete(wrapper);

        // 3. 删除盲盒
        this.removeById(boxId);
    }

    @Override
    @Transactional
    public void batchUpdateStatus(List<Long> boxIds, Integer status) {
        if (boxIds == null || boxIds.isEmpty()) {
            throw new RuntimeException("盲盒ID列表不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("状态值无效");
        }

        // 批量更新状态
        List<BlindBox> boxes = boxIds.stream()
                .map(id -> {
                    BlindBox box = new BlindBox();
                    box.setBoxId(id);
                    box.setStatus(status);
                    return box;
                })
                .collect(Collectors.toList());

        this.updateBatchById(boxes);
    }

    @Override
    public BlindBoxFormVO getFormOptions() {
        BlindBoxFormVO formVO = new BlindBoxFormVO();

        // 1. 获取所有分类
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.select(Product::getCategory)
                .groupBy(Product::getCategory);
        List<String> categories = productMapper.selectList(productWrapper)
                .stream()
                .map(Product::getCategory)
                .collect(Collectors.toList());
        formVO.setCategories(categories);

        // 2. 获取所有超市
        LambdaQueryWrapper<Supermarket> marketWrapper = new LambdaQueryWrapper<>();
        marketWrapper.eq(Supermarket::getStatus, 1); // 只获取营业中的超市
        List<BlindBoxFormVO.MarketOption> markets = supermarketMapper.selectList(marketWrapper)
                .stream()
                .map(market -> {
                    BlindBoxFormVO.MarketOption option = new BlindBoxFormVO.MarketOption();
                    option.setMarketId(market.getMarketId());
                    option.setName(market.getName());
                    return option;
                })
                .collect(Collectors.toList());
        formVO.setMarkets(markets);

        // 3. 获取所有商品
        List<BlindBoxFormVO.ProductOption> products = productMapper.selectList(null)
                .stream()
                .map(product -> {
                    BlindBoxFormVO.ProductOption option = new BlindBoxFormVO.ProductOption();
                    option.setProductId(product.getProductId());
                    option.setName(product.getName());
                    option.setCategory(product.getCategory());
                    return option;
                })
                .collect(Collectors.toList());
        formVO.setProducts(products);

        return formVO;
    }

    @Override
    public BlindBoxStatsVO getStats(Long boxId, LocalDate startDate, LocalDate endDate) {
        // 1. 获取盲盒基本信息
        BlindBox blindBox = this.getById(boxId);
        if (blindBox == null) {
            throw new RuntimeException("盲盒不存在");
        }

        // 2. 查询销售统计数据
        // 注意：这里需要关联订单表和订单明细表，建议使用自定义SQL
        List<BlindBoxStatsVO.DailyStat> dailyStats = baseMapper.getDailyStats(boxId, startDate, endDate);

        // 3. 计算总销量和总金额
        int totalSales = dailyStats.stream()
                .mapToInt(BlindBoxStatsVO.DailyStat::getSales)
                .sum();
        BigDecimal totalAmount = dailyStats.stream()
                .map(BlindBoxStatsVO.DailyStat::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 获取市场排名
        Integer marketRank = baseMapper.getMarketRank(boxId, blindBox.getMarketId());

        // 5. 组装返回数据
        BlindBoxStatsVO statsVO = new BlindBoxStatsVO();
        statsVO.setBoxId(boxId);
        statsVO.setName(blindBox.getName());
        statsVO.setTotalSales(totalSales);
        statsVO.setTotalAmount(totalAmount);
        statsVO.setDailyStats(dailyStats);
        statsVO.setMarketRank(marketRank);
 
        return statsVO;
    }
} 