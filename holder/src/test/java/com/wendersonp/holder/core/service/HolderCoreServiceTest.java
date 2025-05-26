package com.wendersonp.holder.core.service;

import com.wendersonp.holder.core.exceptions.BusinessException;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.HolderRequestModel;
import com.wendersonp.holder.core.ports.driven.CryptographyServiceDrivenPort;
import com.wendersonp.holder.core.ports.driven.HolderRepositoryDrivenPort;
import com.wendersonp.holder.core.fixture.HolderFixture;
import com.wendersonp.holder.core.fixture.HolderRequestFixture;
import com.wendersonp.holder.util.ExceptionMessageEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolderCoreServiceTest {

    @Mock
    private CryptographyServiceDrivenPort cryptographyServiceDrivenPort;

    @Mock
    private HolderRepositoryDrivenPort holderRepositoryDrivenPort;

    @InjectMocks
    private HolderCoreService holderService;

    void setUpSuccessfullFind() {
        when(cryptographyServiceDrivenPort.hashDocumentNumber("64699583032")).thenReturn("hashSomething".getBytes(StandardCharsets.UTF_8));
        when(holderRepositoryDrivenPort.findByDocumentHash("hashSomething".getBytes(StandardCharsets.UTF_8))).thenReturn(Optional.of(HolderFixture.createSuccessfulCase()));
    }

    void setUpNotFoundFind() {
        when(cryptographyServiceDrivenPort.hashDocumentNumber("03394281040")).thenReturn("hashSomethingElse".getBytes(StandardCharsets.UTF_8));
        when(holderRepositoryDrivenPort.findByDocumentHash("hashSomethingElse".getBytes(StandardCharsets.UTF_8))).thenReturn(Optional.empty());
    }

    void setUpSuccessfulSave() {
        when(cryptographyServiceDrivenPort.hashDocumentNumber("64699583032")).thenReturn("hashSomething".getBytes(StandardCharsets.UTF_8));
        when(holderRepositoryDrivenPort.findByDocumentHash("hashSomething".getBytes(StandardCharsets.UTF_8))).thenReturn(Optional.empty());
        when(cryptographyServiceDrivenPort.maskDocumentNumber("64699583032")).thenReturn("646#####032");
        when(holderRepositoryDrivenPort.persist(HolderFixture.createSuccessfulCase())).thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    @Test
    void findByDocumentSuccessfully() {
        setUpSuccessfullFind();

        var holder = holderService.findByDocument("64699583032");
        assertEquals(HolderFixture.createSuccessfulCase(), holder);

        verify(holderRepositoryDrivenPort, times(1)).findByDocumentHash("hashSomething".getBytes(StandardCharsets.UTF_8));
        verify(cryptographyServiceDrivenPort, times(1)).hashDocumentNumber("64699583032");
    }

    @Test
    void findByDocumentNotFound() {
        setUpNotFoundFind();

        var exception = assertThrows(BusinessException.class, () -> holderService.findByDocument("03394281040"));
        assertEquals(ExceptionMessageEnum.DOCUMENT_NUMBER_NOT_FOUND.getMessage(), exception.getMessage());

        verify(holderRepositoryDrivenPort, times(1)).findByDocumentHash("hashSomethingElse".getBytes(StandardCharsets.UTF_8));
        verify(cryptographyServiceDrivenPort, times(1)).hashDocumentNumber("03394281040");
    }

    @Test
    void saveSuccessfully() {
        setUpSuccessfulSave();

        var holder = holderService.save(new HolderRequestModel("64699583032", "Wenderson"));
        assertEquals(HolderFixture.createSuccessfulCase(), holder);

        verify(holderRepositoryDrivenPort, times(1)).findByDocumentHash("hashSomething".getBytes(StandardCharsets.UTF_8));
        verify(cryptographyServiceDrivenPort, times(1)).hashDocumentNumber("64699583032");
        verify(holderRepositoryDrivenPort, times(1)).persist(HolderFixture.createSuccessfulCase());
        verify(cryptographyServiceDrivenPort, times(1)).maskDocumentNumber("64699583032");
    }

    @Test
    void saveButAlreadyExistsDocumentSave() {
        setUpSuccessfullFind();
        var holderRequest = HolderRequestFixture.createSuccessfulCase();

        var exception = assertThrows(BusinessException.class, () ->
                holderService.save(holderRequest));
        assertEquals(ExceptionMessageEnum.DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage(), exception.getMessage());

        verify(holderRepositoryDrivenPort, times(1)).findByDocumentHash("hashSomething".getBytes(StandardCharsets.UTF_8));
        verify(cryptographyServiceDrivenPort, times(1)).hashDocumentNumber("64699583032");
        verify(holderRepositoryDrivenPort, never()).persist(any(HolderModel.class));
        verify(cryptographyServiceDrivenPort, never()).maskDocumentNumber(anyString());
    }
}