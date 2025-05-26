package com.wendersonp.holder.infrastructure.adapters;

import com.wendersonp.holder.infrastructure.service.AWSSecretManagerService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptographyServiceAdapterTest {

    @Mock
    private AWSSecretManagerService awsSecretManagerService;

    @InjectMocks
    private CryptographyServiceAdapter cryptographyServiceAdapter;

    void setUpHashDocumentNumber(String salt) {
        when(awsSecretManagerService.retrieveSalt()).thenReturn(salt);
    }

    @ParameterizedTest
    @CsvSource({
        "03394281040, asoduh5019028, e6e00a6578b18deb10a1078994a5d67cca6464300332babf7f5ce75483a29c9489fe13c6ba355ab9e41cb794def0021c7993385a02d2f0c3ce8ccefbc3e539e2",
        "39643547086, aiopsdjsoaj314, 1e0c811dadf405bf095d96f2d1fcab5f7cea3306c4db20f9bcf05ebd2b6d08c10c8f13d6925e1212e000bc4c8380e5df8d07ae192dd091f6d65b4d6ea0801200",
        "97883665030, 10823oudshasd, 381cdd3e7133f4bfe197d5590c55b14b5da916c9f6a2b466cc44637f0a92ae99c0358a0cae1aa20a34220936cc822fd67c4dd474f8d0b2f0bff918cc2f6b9b72"
    })
    void hashDocumentNumber(String documentNumber, String salt, String expectedHash) {
        setUpHashDocumentNumber(salt);
        byte[] hashInBytes = cryptographyServiceAdapter.hashDocumentNumber(documentNumber);
        String actualHash = HexFormat.of().formatHex(hashInBytes);
        assertEquals(expectedHash, actualHash);
    }
}