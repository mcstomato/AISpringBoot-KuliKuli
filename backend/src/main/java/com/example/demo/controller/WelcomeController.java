package com.example.demo.controller;

import com.example.demo.service.WelcomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping
public class WelcomeController {

    private final WelcomeService welcomeService;

    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        String clientIP = getClientIP(request);
        System.out.println("🌐 访问IP: " + clientIP + " | 时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return welcomeService.getWelcomeMessage();
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIP(HttpServletRequest request) {
        // 优先获取X-Forwarded-For头（代理服务器转发）
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        // 获取X-Real-IP头（Nginx等代理服务器设置）
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        // 获取X-Forwarded-For-Original头
        String xForwardedForOriginal = request.getHeader("X-Forwarded-For-Original");
        if (xForwardedForOriginal != null && !xForwardedForOriginal.isEmpty()) {
            return xForwardedForOriginal;
        }
        
        // 最后获取直接连接的IP地址
        return request.getRemoteAddr();
    }
}


