package com.wendersonp.holder.infrastructure.adapters;

import com.wendersonp.holder.core.ports.driven.CryptographyServiceDrivenPort;
import com.wendersonp.holder.infrastructure.exception.InfrastructureException;
import com.wendersonp.holder.infrastructure.service.AWSSecretManagerService;
import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Service
@RequiredArgsConstructor
public class CryptographyServiceAdapter implements CryptographyServiceDrivenPort {

    private final AWSSecretManagerService awsSecretManagerService;

    @Override
    public byte[] hashDocumentNumber(String documentNumber) {
        log.info("Hashing document number of initial: {}", documentNumber.substring(0, 4));
        byte[] salt = awsSecretManagerService.retrieveSalt().getBytes(StandardCharsets.UTF_8);

        try {
           MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
           messageDigest.update(salt);
           return messageDigest.digest(documentNumber.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException exception) {
            throw new InfrastructureException(ExceptionMessageEnum.COULD_NOT_HASH_DOCUMENT_NUMBER, exception);
        }
    }

    @Override
    public String maskDocumentNumber(String documentNumber) {
        log.info("Masking document number of initial: {}", documentNumber.substring(0, 4));
        return documentNumber.replace(documentNumber.substring(3, 9), "XXXXXX");
    }
}
