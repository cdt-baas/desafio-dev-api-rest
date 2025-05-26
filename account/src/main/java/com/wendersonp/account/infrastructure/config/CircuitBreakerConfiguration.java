package com.wendersonp.account.infrastructure.config;

import com.wendersonp.account.core.exceptions.BusinessException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .ignoreExceptions(BusinessException.class)
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

}
