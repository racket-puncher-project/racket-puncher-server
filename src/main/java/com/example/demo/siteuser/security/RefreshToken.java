package com.example.demo.siteuser.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;


    public RefreshToken(final String email, final String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmail() {
        return email;
    }
}
