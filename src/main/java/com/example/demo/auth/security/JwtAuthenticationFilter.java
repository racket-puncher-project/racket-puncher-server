package com.example.demo.auth.security;

import com.example.demo.exception.RacketPuncherException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String endpoint : getPermitAllEndpoints()) {
            if (pathMatcher.match(endpoint, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
            String token = this.resolveTokenFromRequest(request); // 웹 요청에서 토큰 추출
            if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) { // 토큰이 실제 텍스트를 가지고 있으며, 유효한지 검증
                Authentication auth = this.tokenProvider.getAuthentication(token); // 토큰에서 인증 정보 가져옴.
                SecurityContextHolder.getContext().setAuthentication(auth);// 인증 정보를 보안 컨텍스트에 설정
            } else {
                throw new AuthenticationException("Invalid token") {}; // 토큰이 없거나 유효하지 않다면 에러 발생
            }
            filterChain.doFilter(request, response); // 다음 필터로 요청과 응답 전달
        } catch (AuthenticationException e) {
            jwtAuthenticationEntryPoint.commence(request, response, e);
        } catch (RacketPuncherException e) {
            jwtAuthenticationEntryPoint.customCommence(response);
        }
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) { // 토큰이 비어 있지 않고, Bearer로 시작한다면
            return token.substring(TOKEN_PREFIX.length()); // Bearer 제외한 실제 토큰 부분 반환
        }
        return null;
    }

    private HashSet<String> getPermitAllEndpoints() {
        var permitAllEndpoints = new HashSet<String>();

        permitAllEndpoints.add("/api/auth/redis");
        permitAllEndpoints.add("/api/auth/sign-up");
        permitAllEndpoints.add("/api/auth/sign-in/**");
        permitAllEndpoints.add("/api/auth/reissue");
        permitAllEndpoints.add("/api/auth/upload-profile-image");
        permitAllEndpoints.add("/api/notifications/connect/**");
        permitAllEndpoints.add("/api/matches/list/**");
        permitAllEndpoints.add("/api/matches/detail/**");
        permitAllEndpoints.add("/api/matches/address");
        permitAllEndpoints.add("/api/users/profile/**");
        permitAllEndpoints.add("/api/aws/**");
        permitAllEndpoints.add("/api/auth/check-nickname");
        permitAllEndpoints.add("/api/auth/check-email");
        permitAllEndpoints.add("/api/auth/kakao");
        permitAllEndpoints.add("/api/auth/phone/send-code");
        permitAllEndpoints.add("/api/auth/phone/verify-code");
        permitAllEndpoints.add("/api/auth/find-id");
        permitAllEndpoints.add("/api/auth/password/verify-user");
        permitAllEndpoints.add("/api/auth/password/reset");
        permitAllEndpoints.add("/ws");
        permitAllEndpoints.add("/ws/**");
        //TODO: 아래 리스트는 채팅방 완성 후 삭제
        permitAllEndpoints.add("/chat-room.html");
        permitAllEndpoints.add("/chat-room.js");
        permitAllEndpoints.add("/chat-list.html");
        permitAllEndpoints.add("/chat-list.js");
        permitAllEndpoints.add("/favicon.ico");

        return permitAllEndpoints;
    }
}