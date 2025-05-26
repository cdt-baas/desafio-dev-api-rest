package com.wendersonp.holder.core.service;

import com.wendersonp.holder.core.exceptions.BusinessException;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.HolderRequestModel;
import com.wendersonp.holder.core.model.enumeration.StatusEnum;
import com.wendersonp.holder.core.ports.driven.CryptographyServiceDrivenPort;
import com.wendersonp.holder.core.ports.driven.HolderRepositoryDrivenPort;
import com.wendersonp.holder.core.ports.driving.HolderCoreDrivingPort;
import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolderCoreService implements HolderCoreDrivingPort {

    private final CryptographyServiceDrivenPort cryptographyServiceDrivenPort;

    private final HolderRepositoryDrivenPort holderRepositoryDrivenPort;

    @Override
    public HolderModel save(HolderRequestModel holderRequestModel) {
        byte[] documentHash = cryptographyServiceDrivenPort.hashDocumentNumber(holderRequestModel.getDocumentNumber());

        Optional<HolderModel> holderModelOptional = findByDocumentHash(documentHash);
        validateIfHolderIsActive(holderModelOptional);

        if (holderModelOptional.isPresent() && holderModelOptional.get().getStatus().equals(StatusEnum.INACTIVE)) {
            return reactivateHolder(holderModelOptional);
        }

        String maskedDocumentNumber = cryptographyServiceDrivenPort.maskDocumentNumber(holderRequestModel.getDocumentNumber());
        var holderModel = new HolderModel(documentHash, maskedDocumentNumber, holderRequestModel.getName(), StatusEnum.ACTIVE);
        return holderRepositoryDrivenPort.persist(holderModel);
    }

    private HolderModel reactivateHolder(Optional<HolderModel> holderModelOptional) {
        if (holderModelOptional.isEmpty()) {
            throw new BusinessException(ExceptionMessageEnum.HOLDER_NOT_FOUND);
        }
        HolderModel holderModel = holderModelOptional.get();
        holderModel.setStatus(StatusEnum.ACTIVE);
        return holderRepositoryDrivenPort.persist(holderModel);
    }

    private void validateIfHolderIsActive(Optional<HolderModel> holderModelOptional) {
        holderModelOptional
                .filter(holderModel -> holderModel.getStatus().equals(StatusEnum.ACTIVE))
                .ifPresent(holderModel -> {
            throw new BusinessException(ExceptionMessageEnum.DOCUMENT_NUMBER_ALREADY_EXISTS);
        });
    }

    @Override
    public HolderModel findByDocument(String documentNumber) throws BusinessException {
        byte[] documentHash = cryptographyServiceDrivenPort.hashDocumentNumber(documentNumber);
        return findByDocumentHash(documentHash)
                .orElseThrow(() -> new BusinessException(ExceptionMessageEnum.DOCUMENT_NUMBER_NOT_FOUND));
    }

    @Override
    public HolderModel findByIdentifier(UUID identifier) {
        return holderRepositoryDrivenPort.findByIdentifier(identifier).orElseThrow(
                () -> new BusinessException(ExceptionMessageEnum.HOLDER_NOT_FOUND));
    }

    @Override
    public List<HolderModel> findAll() {
        return holderRepositoryDrivenPort.findAllByStatus(StatusEnum.ACTIVE);
    }

    @Override
    public void deleteByIdentifier(UUID identifier) {
        holderRepositoryDrivenPort.setStatus(identifier, StatusEnum.INACTIVE);
    }

    @Override
    public void reactivate(UUID identifier) {
        holderRepositoryDrivenPort.setStatus(identifier, StatusEnum.ACTIVE);
    }

    private Optional<HolderModel> findByDocumentHash(byte[] documentHash) {
        return holderRepositoryDrivenPort.findByDocumentHash(documentHash);
    }
}
