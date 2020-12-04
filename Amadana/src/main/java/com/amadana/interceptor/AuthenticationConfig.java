package com.amadana.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class AuthenticationConfig implements WebMvcConfigurer {

    AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/login")
                .order(1);
    }
}
