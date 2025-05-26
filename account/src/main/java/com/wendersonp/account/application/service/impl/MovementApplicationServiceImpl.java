package com.wendersonp.account.application.service.impl;

import com.wendersonp.account.application.dto.MovementResponseDTO;
import com.wendersonp.account.application.service.MovementApplicationService;
import com.wendersonp.account.core.ports.driving.MovementCoreDrivingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementApplicationServiceImpl implements MovementApplicationService {

    private final MovementCoreDrivingPort movementCore;


    @Override
    public Page<MovementResponseDTO> getMovementsWithinAPeriod(LocalDate beginningDate, LocalDate endingDate, UUID accountIdentifier, Pageable pageable) {
        return movementCore.getMovementsWithinAPeriod(beginningDate, endingDate, accountIdentifier, pageable).map(MovementResponseDTO::new);
    }
}
