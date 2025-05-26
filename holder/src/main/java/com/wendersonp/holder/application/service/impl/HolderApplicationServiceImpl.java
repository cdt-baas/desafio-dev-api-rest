package com.wendersonp.holder.application.service.impl;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;
import com.wendersonp.holder.application.service.HolderApplicationService;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.ports.driving.HolderCoreDrivingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolderApplicationServiceImpl implements HolderApplicationService {

    private final HolderCoreDrivingPort holderService;


    @Override
    public HolderResponseDTO save(HolderRequestDTO holderRequestDTO) {
        HolderModel holderModel = holderService.save(holderRequestDTO.toModel());
        return new HolderResponseDTO(holderModel);
    }

    @Override
    public HolderResponseDTO findByDocument(String documentNumber) {
        String formattedDocumentNumber = documentNumber
                .replace(".", "")
                .replace("-", "");
        HolderModel holderModel = holderService.findByDocument(formattedDocumentNumber);
        return new HolderResponseDTO(holderModel);
    }

    @Override
    public HolderResponseDTO findByIdentifier(UUID identifier) {
        HolderModel holderModel = holderService.findByIdentifier(identifier);
        return new HolderResponseDTO(holderModel);
    }

    @Override
    public void deleteByIdentifier(UUID identifier) {
        holderService.deleteByIdentifier(identifier);
    }

    @Override
    public List<HolderResponseDTO> findAll() {
        return holderService.findAll()
                .stream()
                .map(HolderResponseDTO::new)
                .toList();
    }

    @Override
    public void reactivate(UUID identifier) {
        holderService.reactivate(identifier);
    }
}
