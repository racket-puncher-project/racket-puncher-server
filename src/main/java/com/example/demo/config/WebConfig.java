package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000", "http://localhost:8080", "http://localhost:8081",
                        "https://racket-puncher.store", "https://racket-puncher-project.github.io")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
