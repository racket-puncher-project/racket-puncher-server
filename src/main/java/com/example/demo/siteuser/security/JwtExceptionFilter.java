package com.example.demo.siteuser.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("유효하지 않은 토큰입니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        } catch (NoSuchElementException ex) {
            log.error("사용자를 찾을 수 없습니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.error("토큰을 추출할 수 없습니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        } catch (NullPointerException ex) {
            filterChain.doFilter(request, response);
        }

    }
    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");
        JwtAuthenticationFilter.ErrorResponse errorResponse = new JwtAuthenticationFilter.ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
