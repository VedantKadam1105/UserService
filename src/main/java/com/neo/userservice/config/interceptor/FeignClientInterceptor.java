package com.neo.userservice.config.interceptor;

import com.neo.userservice.security.ServiceTokenProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final ServiceTokenProvider serviceTokenProvider;

    public FeignClientInterceptor(ServiceTokenProvider serviceTokenProvider) {
        this.serviceTokenProvider = serviceTokenProvider;
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = serviceTokenProvider.generateServiceToken();
        template.header("Authorization", "Bearer " + token);
    }
}