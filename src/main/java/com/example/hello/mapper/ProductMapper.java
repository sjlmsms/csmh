package com.example.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hello.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
} 