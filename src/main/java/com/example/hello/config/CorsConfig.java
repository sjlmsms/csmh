/* package com.example.hello.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
 */
/**
 * 跨域配置
 */
/* @Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1. 设置访问源地址
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        // 2. 设置访问源请求头
        corsConfiguration.addAllowedHeader("*");
        // 3. 设置访问源请求方法
        corsConfiguration.addAllowedMethod("*");
        // 4. 是否允许凭据
        corsConfiguration.setAllowCredentials(true);
        
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
} */