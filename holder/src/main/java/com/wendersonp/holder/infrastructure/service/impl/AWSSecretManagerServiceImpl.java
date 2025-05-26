package com.wendersonp.holder.infrastructure.service.impl;


import com.wendersonp.holder.infrastructure.exception.AWSException;
import com.wendersonp.holder.infrastructure.service.AWSSecretManagerService;
import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@Service
@RequiredArgsConstructor
public class AWSSecretManagerServiceImpl implements AWSSecretManagerService {

    @Value("${aws.salt.secretId}")
    private String secretId;

    private final SecretsManagerClient secretsManagerClient;
    @Override
    public String retrieveSalt() {
        try {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretId)
                    .build();

            GetSecretValueResponse response = secretsManagerClient.getSecretValue(getSecretValueRequest);
            return response.secretString();
        } catch (SecretsManagerException exception) {
            throw new AWSException(ExceptionMessageEnum.COULD_NOT_RETRIEVE_SECRET, exception);
        }
    }
}
