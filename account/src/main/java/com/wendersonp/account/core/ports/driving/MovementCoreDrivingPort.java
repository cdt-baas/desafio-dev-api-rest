package com.wendersonp.account.core.ports.driving;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface MovementCoreDrivingPort {

    MovementModel registerMovement(AccountModel account, BigDecimal amount, BigDecimal balanceAfter, String description, MovementType type);

    BigDecimal getTotalWithdrawWithinADate(LocalDate date, UUID accountIdentifier);

    Page<MovementModel> getMovementsWithinAPeriod(LocalDate beginningDate, LocalDate endingDate, UUID accountIdentifier, Pageable pageable);
}
