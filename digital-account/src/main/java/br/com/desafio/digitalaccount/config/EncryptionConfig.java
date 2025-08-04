package br.com.desafio.digitalaccount.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;

@Configuration
public class EncryptionConfig {

    @Bean
    public BytesEncryptor aesBytesEncryptor(@Value("${secret-key}") String encryptionSecret,
                                            @Value("${salt}") String salt) {
        return Encryptors.standard(encryptionSecret, salt);
    }
}
