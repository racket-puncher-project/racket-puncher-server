package com.example.demo.notification.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        emitterMap.put(getKey(userId), sseEmitter);
        return sseEmitter;
    }

    private String getKey(Long userId) {
        return "Emitter:UID:" + userId;
    }

    public Optional<SseEmitter> get(Long userId) {
        return Optional.ofNullable(emitterMap.get(getKey(userId)));
    }

    public void delete(Long userId) {
        emitterMap.remove(getKey(userId));
    }

}
