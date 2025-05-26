package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.model.HolderModel;
import com.wendersonp.account.core.ports.driven.HolderServiceDrivenPort;
import com.wendersonp.account.infrastructure.clients.HolderFeignClient;
import com.wendersonp.account.infrastructure.clients.dto.HolderResponseDTO;
import com.wendersonp.account.infrastructure.exception.CircuitBreakerException;
import com.wendersonp.account.infrastructure.exception.InfrastructureException;
import com.wendersonp.account.util.ExceptionMessageEnum;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolderClientServiceAdapter implements HolderServiceDrivenPort {

    public static final String SERVICE_TEMPORARILY_UNAVAILABLE = "Serviço temporariamente indisponivel";
    private final HolderFeignClient holderFeignClient;

    @Override
    @CircuitBreaker(name = "holderService.findByDocument", fallbackMethod = "fallBack")
    public Optional<HolderModel> findByDocument(String documentNumber) {
        try {
            return Optional
                    .of(holderFeignClient.findByDocumentNumber(documentNumber))
                    .map(HolderResponseDTO::toModel);
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        } catch (FeignException ex) {
            throw new InfrastructureException(ExceptionMessageEnum.HOLDER_SERVICE_UNAVAILABLE, ex);
        }
    }

    @Override
    @CircuitBreaker(name = "holderService.findByIdentifier", fallbackMethod = "fallBack")
    public Optional<HolderModel> findByIdentifier(UUID identifier) {
        try {
            return Optional
                    .of(holderFeignClient.findByIdentifier(identifier))
                    .map(HolderResponseDTO::toModel);
        } catch (FeignException.NotFound ex) {
            return Optional.empty();
        } catch (FeignException ex) {
            throw new InfrastructureException(ExceptionMessageEnum.HOLDER_SERVICE_UNAVAILABLE, ex);
        }
    }

    @Override
    @CircuitBreaker(name = "holderService.removeByIdentifier", fallbackMethod = "fallBack")
    public void removeByIdentifier(UUID identifier) {
        try {
            holderFeignClient.removeByIdentifier(identifier);
        } catch (FeignException.NotFound ex) {
            log.error("Holder not found", ex);
            throw new BusinessException(ExceptionMessageEnum.HOLDER_NOT_FOUND);
        } catch (FeignException ex) {
            throw new InfrastructureException(ExceptionMessageEnum.HOLDER_SERVICE_UNAVAILABLE, ex);
        }
    }

    @Override
    @CircuitBreaker(name = "holderService.reactivate", fallbackMethod = "fallBack")
    public void reactivate(UUID holderId) {
        try {
            holderFeignClient.reactivate(holderId);
        } catch (FeignException.NotFound ex) {
            log.error("Holder not found", ex);
            throw new BusinessException(ExceptionMessageEnum.HOLDER_NOT_FOUND);
        } catch (FeignException ex) {
            throw new InfrastructureException(ExceptionMessageEnum.HOLDER_SERVICE_UNAVAILABLE, ex);
        }
    }

    public void fallBack() {
        var circuitBreakerOpenException = new CircuitBreakerException(SERVICE_TEMPORARILY_UNAVAILABLE);
        throw new InfrastructureException(ExceptionMessageEnum.HOLDER_SERVICE_UNAVAILABLE, circuitBreakerOpenException);
    }
}
