package com.itheima.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    // 创建 ObjectMapper 对象，用于序列化 Result 对象, 生成 JSON 响应
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);

            String redisToken = stringRedisTemplate.opsForValue().get(claims.get("id").toString());
            if (redisToken == null || !redisToken.equals(token)) {
                throw new RuntimeException("Invalid token or token expired");
            }
            return true;
        } catch (Exception e) {
            // 设置响应状态码为 401 Unauthorized
            response.setStatus(401);
            // 设置响应内容类型为 JSON
            response.setContentType("application/json");
            // 设置响应字符编码为 UTF-8
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                // 使用 Result 类生成错误响应
                Result errorResult = Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "Invalid token");
                // 将 Result 对象转换为 JSON 字符串
                String jsonResponse = objectMapper.writeValueAsString(errorResult);
                out.print(jsonResponse);
                out.flush();
                return false;
            } catch (IOException ioException) {
                // 处理可能的 IO 异常
                ioException.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
