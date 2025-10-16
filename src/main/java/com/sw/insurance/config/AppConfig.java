package com.sw.insurance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import java.util.Base64;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class AppConfig {
    
    @Value("${vehicle.service.username}")
    private String username;
    
    @Value("${vehicle.service.password}")
    private String password;
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Add Basic Auth interceptor
        restTemplate.setInterceptors(Collections.singletonList(
            (ClientHttpRequestInterceptor) (request, body, execution) -> {
                String auth = username + ":" + password;
                String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.US_ASCII));
                String authHeader = "Basic " + encodedAuth;
                request.getHeaders().add("Authorization", authHeader);
                return execution.execute(request, body);
            }
        ));
        
        return restTemplate;
    }
}
