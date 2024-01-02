package com.example.demo.siteuser.security;

import static com.example.demo.exception.type.ErrorCode.LOGIN_REQUIRED;

import com.example.demo.exception.RacketPuncherException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = this.resolveTokenFromRequest(request); // 웹 요청에서 토큰 추출
            if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) { // 토큰이 실제 텍스트를 가지고 있으며, 유효한지 검증
                Authentication auth = this.tokenProvider.getAuthentication(token); // 토큰에서 인증 정보 가져옴.
                SecurityContextHolder.getContext().setAuthentication(auth);// 인증 정보를 보안 컨텍스트에 설정
            } else {
                throw new RacketPuncherException(LOGIN_REQUIRED); // 토큰이 없다면 로그인 요청
            }
        } catch (RacketPuncherException e) { // permitAll() 적용을 위해, throw하지 않고 catch를 사용해 다음 필터로 넘긴다.
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청과 응답 전달
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) { // 토큰이 비어 있지 않고, Bearer로 시작한다면
            return token.substring(TOKEN_PREFIX.length()); // Bearer 제외한 실제 토큰 부분 반환
        }
        return null;
    }
}