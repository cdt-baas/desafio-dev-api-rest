package com.wendersonp.account.application.service;

import com.wendersonp.account.application.dto.MovementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface MovementApplicationService {

    Page<MovementResponseDTO> getMovementsWithinAPeriod(LocalDate beginningDate, LocalDate endingDate, UUID accountIdentifier, Pageable pageable);
}
