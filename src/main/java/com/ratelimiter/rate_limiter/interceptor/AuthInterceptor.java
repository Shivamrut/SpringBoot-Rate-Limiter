package com.ratelimiter.rate_limiter.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ratelimiter.rate_limiter.domain.Client;
import com.ratelimiter.rate_limiter.exception.UnauthorizedException;
import com.ratelimiter.rate_limiter.repository.ClientRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements  HandlerInterceptor{

    private final ClientRepository clientRepository;

    public AuthInterceptor(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                // logic for auth
                String apiKey = request.getHeader("X-API-Key");
                if(!StringUtils.hasText(apiKey)){
                    throw new UnauthorizedException("Missing or invalid API key");
                }
                Client client = clientRepository.getClient(apiKey);
                if (client == null) {
                    throw new UnauthorizedException("Missing or invalid API key");
                }
                request.setAttribute("client", client);
        return true;
    }
}
