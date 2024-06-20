package com.example.backend0.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName config
 * @Description
 **/
@Configuration
public class config implements WebMvcConfigurer {
    @Override
   /* public void addCorsMappings(CorsRegistry registry){
        // 允许访问路径
        registry.addMapping("/**").allowedOriginPatterns("http://localhost:*").allowedMethods("GET","POST","DELETE","PUT","OPTION").allowCredentials(true).maxAge(3600);

    }*/
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}
