package com.wendersonp.holder.application.service;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;

import java.util.List;
import java.util.UUID;

public interface HolderApplicationService {
    HolderResponseDTO save(HolderRequestDTO holderRequestDTO);
    HolderResponseDTO findByDocument(String documentNumber);

    HolderResponseDTO findByIdentifier(UUID identifier);

    void deleteByIdentifier(UUID identifier);

    List<HolderResponseDTO> findAll();

    void reactivate(UUID identifier);
}
