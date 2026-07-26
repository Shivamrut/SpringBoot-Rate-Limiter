package com.ratelimiter.rate_limiter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ratelimiter.rate_limiter.interceptor.AuthInterceptor;

@Configuration
public class Config implements WebMvcConfigurer{

    private final AuthInterceptor authInterceptor;

    public Config(AuthInterceptor authInterceptor){
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
        .addPathPatterns("/v1/**");
    }
    
}
