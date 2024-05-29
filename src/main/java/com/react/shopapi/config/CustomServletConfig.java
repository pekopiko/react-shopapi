package com.react.shopapi.config;

import com.react.shopapi.controller.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    // 메서드 오버라이딩하기 : alt + Insert (mac:cmd+N) > OverrideMethod > addFormatter 검색
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // registry를 통해 우리가 만든 포매터 추가
        registry.addFormatter(new LocalDateFormatter());
    }

    /* CORS 설정 : 메서드 오버라이딩 -> 시큐리티로 이전
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //CORS 적용할 URL 패턴
                .allowedOrigins("*") // origin 지정 "http://localhost:3000"
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(300) // 지정한 시간만큼 pre-flight 캐싱
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
    }*/


}
