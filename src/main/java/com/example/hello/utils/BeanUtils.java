package com.example.hello.utils;

import java.util.Map;

public class BeanUtils {
    public static <T> T mapToBean(Map<Object, Object> map, Class<T> beanClass) {
        try {
            T instance = beanClass.getDeclaredConstructor().newInstance();
            org.springframework.beans.BeanUtils.copyProperties(map, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to bean", e);
        }
    }
} 