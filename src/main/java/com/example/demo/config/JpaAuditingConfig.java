package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // @WebMvcTest(MatchingController.class)와 @EnableJpaAuditing를 같이 쓸 경우 발생하는 문제 해결하기 위한 클래스
    // Config로 EnableJpaAuditing를 분리
}