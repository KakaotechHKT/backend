package com.babpat.server.config.jwt.filter;

import com.babpat.server.common.enums.CustomResponseStatus;
import com.babpat.server.common.exception.CustomException;
import com.babpat.server.util.jwt.JwtUtil;
import com.babpat.server.util.redis.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String EXCEPTION = "exception";
    private static final String AUTHORIZATION = "Authorization";
    private static final String LOGOUT = "LOGOUT";

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String resolveToken = jwtUtil.resolveToken(request.getHeader(AUTHORIZATION));

        if (resolveToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            handleBlacklistedToken(resolveToken);
            Authentication authentication = jwtUtil.getAuthentication(resolveToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (CustomException e) {
            handleException(request, CustomResponseStatus.LOGOUT_MEMBER);
        } catch (ExpiredJwtException e) {
            handleException(request, CustomResponseStatus.EXPIRED_JWT);
        } catch (JwtException | IllegalArgumentException e) {
            handleException(request, CustomResponseStatus.BAD_JWT);
        }

        filterChain.doFilter(request, response);
    }

    // 로그아웃한 사용자가 접근하는지 파악. -> 접근할경우 예외발생
    private void handleBlacklistedToken(String resolveToken) throws CustomException {
        String redisLogoutValue = redisUtil.getData(resolveToken);
        if (redisLogoutValue != null && redisLogoutValue.equals(LOGOUT)) {
            throw new CustomException(CustomResponseStatus.LOGOUT_MEMBER);
        }
    }

    private void handleException(HttpServletRequest request, CustomResponseStatus status) {
        request.setAttribute(EXCEPTION, status.getMessage());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/api/v1/auth/reissue",
                "/api/v1/auth/register",
                "/api/v1/auth/login",
                "/api/v1/auth/duplicate",
                "/api/v1/babpat/v2/post",
                "/favicon.ico"
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
