package com.wendersonp.account.core.ports.driven;

import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface MovementRepositoryDrivenPort {

    MovementModel persist(MovementModel movementModel);

    BigDecimal getTotalMovementWithinAPeriod(LocalDateTime beginningDate, LocalDateTime endingDate, UUID accountIdentifier, MovementType type);

    Page<MovementModel> getMovementsWithinAPeriod(LocalDateTime startTime, LocalDateTime endTime, UUID accountIdentifier, Pageable pageable);
}
