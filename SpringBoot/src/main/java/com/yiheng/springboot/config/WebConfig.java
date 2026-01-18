package com.yiheng.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将所有以 /uploads/ 开头的请求，都映射到 D:/Project/uploads/ 这个物理路径下
        // 例如，请求 /uploads/solar.jpg 就会去 D:/Project/uploads/solar.jpg 文件
        // 注意路径格式和末尾的斜杠
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/Project/uploads/");
    }
} 