package com.example.addmusic.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}") // 상대 업로드 경로
    private String uploadPath;

    @Value("${display-path}") // 클라이언트 가상 경로
    private String displayPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드 경로를 절대 경로로 변환
        String absoluteUploadPath = Paths.get(uploadPath).toAbsolutePath().toUri().toString();

        // 리소스 핸들러 등록
        registry.addResourceHandler(displayPath + "/**")
                .addResourceLocations(absoluteUploadPath)
                .setCachePeriod(3600); // 캐시 시간 설정 (초 단위)
    }
}
