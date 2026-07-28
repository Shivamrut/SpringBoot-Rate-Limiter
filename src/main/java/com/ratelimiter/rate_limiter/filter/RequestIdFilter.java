package com.ratelimiter.rate_limiter.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ratelimiter.rate_limiter.web.ApiConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String requestId = request.getHeader(ApiConstants.Headers.REQUEST_ID);
                if (!StringUtils.hasText(requestId)){
                    requestId = "req-" + UUID.randomUUID().toString();
                } 
                request.setAttribute(ApiConstants.Attributes.REQUEST_ID, requestId);
                response.setHeader(ApiConstants.Headers.REQUEST_ID, requestId);
                filterChain.doFilter(request, response);
    }
    
}
