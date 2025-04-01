package com.example.hello.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.util.StringUtils;
import com.example.hello.dto.UserDTO;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.hello.utils.RedisConstants.LOGIN_USER_KEY;
import static com.example.hello.utils.RedisConstants.LOGIN_USER_TTL;

public class RefleshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public RefleshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //实现token刷新逻辑
        String token = request.getHeader("Authorization");
        if(StrUtil.isBlank(token)) {
            // 如果token为空, 放行, 让下一个拦截器处理
            return true;
        }
        // 将token变成redis的tokenKey方便查询
        String tokenKey = LOGIN_USER_KEY + token;

        //从redis中获取token查询用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
        if(userMap == null) {
            //等于空, 放行, 让下一个拦截器处理
            return true;
        }
        
        //将Map转换为UserDTO对象
        UserDTO userDTO = BeanUtils.mapToBean(userMap, UserDTO.class);
        UserHolder.setUser(userDTO);

        //刷新token
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        //放行
        return true;
    }
}