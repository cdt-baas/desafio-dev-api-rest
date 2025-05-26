package com.wendersonp.account.core.service;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.AccountPropertiesModel;
import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.model.HolderModel;
import com.wendersonp.account.core.model.enumeration.BlockStatus;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.model.enumeration.StatusEnum;
import com.wendersonp.account.core.ports.driven.AccountDefaultPropertiesDrivenPort;
import com.wendersonp.account.core.ports.driven.AccountRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driven.HolderServiceDrivenPort;
import com.wendersonp.account.core.ports.driving.AccountCoreDrivingPort;
import com.wendersonp.account.util.ExceptionMessageEnum;
import com.wendersonp.account.util.GenerationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCoreService implements AccountCoreDrivingPort {

    private final HolderServiceDrivenPort holderService;

    private final BalanceCoreService balanceCoreService;

    private final AccountRepositoryDrivenPort accountRepository;

    private final AccountDefaultPropertiesDrivenPort accountDefaultPropertiesDrivenPort;


    @Override
    public AccountModel createAccount(String documentNumber) {
        Optional<HolderModel> holderModelOpt = holderService.findByDocument(documentNumber);
        if (holderModelOpt.isEmpty()) {
            throw new BusinessException(ExceptionMessageEnum.HOLDER_NOT_FOUND);
        }
        HolderModel holder = holderModelOpt.get();
        AccountModel accountModel;

        accountModel = validateHolder(holder);

        if (accountModel != null) {
            return accountModel;
        }

        log.info("Creating account for holder {}", holder.getIdentifier());

        AccountPropertiesModel accountProperties = accountDefaultPropertiesDrivenPort.getDefaultProperties();

        Supplier<BalanceModel> balanceModel = balanceCoreService::createBalance;

        accountModel = AccountModel.builder()
                .holderId(holder.getIdentifier())
                .accountNumber(GenerationUtils.generateAccountNumber())
                .branch(accountProperties.getDefaultBranch())
                .withdrawDailyLimit(accountProperties.getWithdrawDefaultLimit())
                .status(BlockStatus.UNBLOCKED)
                .createdAt(LocalDateTime.now())
                .balance(balanceModel)
                .build();

        return accountRepository.persist(accountModel);
    }

    @Override
    public BalanceModel makeMovementOnBalance(UUID accountIdentifier, BigDecimal amount, MovementType type, String description) {
        AccountModel accountModel = findByIdentifier(accountIdentifier);

        validateAccountForMovement(accountModel);

        return balanceCoreService.makeMovementOnBalance(accountModel, accountModel.getBalance().get().getIdentifier(), type, amount, description);
    }

    public AccountModel findByIdentifier(UUID identifier) {
        return accountRepository.findByIdentifier(identifier).orElseThrow(
                () -> new BusinessException(ExceptionMessageEnum.ACCOUNT_NOT_FOUND)
        );
    }

    @Override
    public AccountModel setBlockStatus(UUID identifier, BlockStatus status) {
        AccountModel accountModel = findByIdentifier(identifier);
        accountModel.setStatus(status);
        return accountRepository.persist(accountModel);
    }

    @Override
    public List<AccountModel> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void closeAccount(UUID identifier) {
        AccountModel accountModel = findByIdentifier(identifier);
        holderService.removeByIdentifier(accountModel.getHolderId());
        accountModel.setStatus(BlockStatus.CLOSED);
        accountRepository.persist(accountModel);
    }

    private AccountModel validateHolder(HolderModel holderModel) {
        if (holderModel.getStatus() != StatusEnum.ACTIVE) {
            throw new BusinessException(ExceptionMessageEnum.HOLDER_INACTIVE);
        }
        Optional<AccountModel> accountModelOpt = accountRepository.findByHolderId(holderModel.getIdentifier());
        if (accountModelOpt.isPresent()) {
            AccountModel account = accountModelOpt.get();
            if (account.getStatus() == BlockStatus.CLOSED) {
                return reactivateAccount(account);
            } else {
                throw new BusinessException(ExceptionMessageEnum.ACCOUNT_EXISTS);
            }
        }
        return null;
    }

    private AccountModel reactivateAccount(AccountModel account) {
        account.setStatus(BlockStatus.UNBLOCKED);
        return accountRepository.persist(account);
    }

    private void validateAccountForMovement(AccountModel accountModel) {
        if (accountModel.getStatus() == BlockStatus.BLOCKED) {
            throw new BusinessException(ExceptionMessageEnum.BLOCKED_ACCOUNT);
        }

        Optional<HolderModel> holder = holderService.findByIdentifier(accountModel.getHolderId());
        if (holder.isEmpty() || holder.get().getStatus() != StatusEnum.ACTIVE) {
            throw new BusinessException(ExceptionMessageEnum.HOLDER_INACTIVE);
        }

    }
}
