package com.itheima.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
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
                Result errorResult = Result.error("Invalid token");
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
