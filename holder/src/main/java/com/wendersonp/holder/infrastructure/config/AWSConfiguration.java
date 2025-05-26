package com.wendersonp.holder.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
public class AWSConfiguration {

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.create();
    }
}
