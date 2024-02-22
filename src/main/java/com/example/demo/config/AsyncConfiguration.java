package com.example.demo.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    @Bean(name = "taskExecutor") // @Async 어노테이션이 붙은 메서드를 사용할 때, 이 빈을 자동으로 찾아서 실행함.
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 쓰레드 풀 기본 크기
        executor.setMaxPoolSize(5); // 쓰레드 풀 최대 크기
        executor.setQueueCapacity(500); // 작업 큐 용량(500개의 작업 대기 가능)
        executor.setThreadNamePrefix("Async-"); // 쓰레드 생성할 때 앞에 접두어 붙임.
        executor.initialize(); // 쓰레드 초기화 해줘야 설정한 쓰레드 풀이 실제로 사용 가능
        return executor;
    }
}