package com.example.demo.notification.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        emitters.put(getKey(userId), sseEmitter);
        return sseEmitter;
    }

    private String getKey(Long userId) {
        return "Emitter:UID:" + userId;
    }

    public Optional<SseEmitter> get(Long userId) {
        return Optional.ofNullable(emitters.get(getKey(userId)));
    }

    public void delete(Long userId) {
        emitters.remove(getKey(userId));
    }
}
