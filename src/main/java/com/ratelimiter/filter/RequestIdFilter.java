package com.ratelimiter.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestIdFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String requestId = request.getHeader("X-Request-Id");
                if (requestId == null || requestId.isEmpty()){
                    requestId = "req-" + UUID.randomUUID().toString();
                } 
                request.setAttribute("requestId", requestId);
                response.setHeader("X-Request-Id", requestId);
                filterChain.doFilter(request, response);
    }
    
}
