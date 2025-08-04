package br.com.desafio.digitalaccount.repositories;

import br.com.desafio.digitalaccount.domain.operation.Operation;
import br.com.desafio.digitalaccount.domain.operation.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query(value = "SELECT SUM(o.amount) FROM operation o " +
            "JOIN account a ON o.account_number = a.number " +
            "WHERE a.number = :accountNumber " +
            "AND o.operation_type = CAST(:operationTypeName AS text) " +
            "AND DATE(o.date_time) = :currentDate",
            nativeQuery = true)
    Optional<BigDecimal> findDailyWithdrawalSumByAccountNumber(
            @Param("accountNumber") String accountNumber,
            @Param("operationTypeName") String operationTypeName,
            @Param("currentDate") LocalDate currentDate
    );

    @Query("SELECT o FROM Operation o WHERE o.account.number = :accountNumber AND o.dateTime BETWEEN :startDate AND :endDate ORDER BY o.dateTime DESC")
    List<Operation> findByAccountNumberAndDateTimeBetween(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
