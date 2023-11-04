package com.imokfine.cubedrive.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imokfine.cubedrive.utils.JwtUtil;
import com.imokfine.cubedrive.utils.Result;
import com.imokfine.cubedrive.utils.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录保护拦截器，检查请求头中是否包含有效token
 * 有效，放行
 * 无效，返回504
 */

@Component
public class LoginProtectedInterceptor implements HandlerInterceptor {

    @Resource
    JwtUtil jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从请求头中获取token
        String token = request.getHeader("token");

        // 检查是否有效
        boolean expiration = jwtHelper.isExpiration(token);

        // 有效，放行
        if(!expiration) return true;

        // 无效，返回504的状态json
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);

        return false;

    }
}
