package com.cornedu.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int WINDOW_MS = 60_000;
    private static final int MAX_REQUESTS = 10;

    private final Map<String, RequestWindow> windows = new ConcurrentHashMap<>();

    @Value("${app.rate-limit-enabled:true}")
    private boolean enabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (!enabled || !isAuthPath(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        String key = getClientKey(request);
        RequestWindow w = windows.computeIfAbsent(key, k -> new RequestWindow());
        if (!w.allow()) {
            response.setStatus(429);
            response.getWriter().write("{\"message\":\"요청이 너무 많습니다. 잠시 후 다시 시도하세요.\"}");
            response.setContentType("application/json;charset=UTF-8");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthPath(String uri) {
        return uri != null && (uri.contains("/api/auth/login") || uri.contains("/api/auth/join"));
    }

    private String getClientKey(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        return request.getRemoteAddr();
    }

    private static class RequestWindow {
        private int count;
        private long windowStart = System.currentTimeMillis();

        synchronized boolean allow() {
            long now = System.currentTimeMillis();
            if (now - windowStart > WINDOW_MS) {
                windowStart = now;
                count = 0;
            }
            if (count >= MAX_REQUESTS) return false;
            count++;
            return true;
        }
    }
}
