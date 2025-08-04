//package br.com.desafio.digitalaccount.services; // Adapte o pacote conforme a localização do seu teste
//
//import br.com.desafio.digitalaccount.domain.account.Account;
//import br.com.desafio.digitalaccount.domain.account.AccountStatus;
//import br.com.desafio.digitalaccount.domain.account.exceptions.*;
//import br.com.desafio.digitalaccount.domain.accountHolder.AccountHolder;
//import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.AccountHolderNotFoundException;
//import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.CPFNotFoundException;
//import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.CPFValidationException;
//import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.InvalidAccountHolderException;
//import br.com.desafio.digitalaccount.domain.operation.Operation;
//import br.com.desafio.digitalaccount.domain.operation.OperationType;
//import br.com.desafio.digitalaccount.dto.account.*;
//import br.com.desafio.digitalaccount.dto.operation.OperationResponseDTO;
//import br.com.desafio.digitalaccount.repositories.AccountHolderRepository;
//import br.com.desafio.digitalaccount.repositories.AccountRepository;
//import br.com.desafio.digitalaccount.repositories.OperationRepository;
//import br.com.desafio.digitalaccount.utils.AccountNumberGenerator;
//import br.com.desafio.digitalaccount.utils.HashUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.encrypt.BytesEncryptor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AccountServiceTest {
//
//    @Mock
//    private AccountRepository accountRepository;
//    @Mock
//    private AccountHolderRepository accountHolderRepository;
//    @Mock
//    private BytesEncryptor bytesEncryptor;
//    @Mock
//    private OperationRepository operationRepository;
//    @Mock
//    private AccountNumberGenerator accountNumberGenerator;
//
//    @InjectMocks
//    private AccountService accountService;
//
//    // Constantes para uso nos testes
//    private static final String SAMPLE_CPF = "12345678900";
//    private static final String SAMPLE_CPF_HASH = HashUtil.generateSha256(SAMPLE_CPF);
//    private static final byte[] ENCRYPTED_CPF = "encrypted_cpf_bytes".getBytes();
//    private static final String SAMPLE_ACCOUNT_NUMBER = "12345-6";
//    private static final String SAMPLE_AGENCY = "0001";
//    private static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("2000.00");
//    private static final String name = "John Doe";
//    private AccountHolder sampleAccountHolder;
//    private Account sampleAccount;
//
//    @BeforeEach
//    void setUp() {
//
//        sampleAccountHolder = new AccountHolder("Teste Holder", ENCRYPTED_CPF, SAMPLE_CPF_HASH);
//        sampleAccountHolder.setId(1L);
//
//        sampleAccount = new Account();
//        sampleAccount.setId(10L);
//        sampleAccount.setAccountHolder(sampleAccountHolder);
//        sampleAccount.setNumber(SAMPLE_ACCOUNT_NUMBER);
//        sampleAccount.setAgency(SAMPLE_AGENCY);
//        sampleAccount.setBalance(new BigDecimal("1000.00"));
//        sampleAccount.setStatus(AccountStatus.ACTIVE);
//
//        // Configura mocks padrão para serem usados em múltiplos testes
//        when(bytesEncryptor.decrypt(ENCRYPTED_CPF)).thenReturn(SAMPLE_CPF.getBytes());
//    }
//
//    // --- Métodos de Ajuda para Mocks Comuns ---
//    private void mockFindAndValidateAccountSuccess() {
//        when(accountRepository.findByNumber(SAMPLE_ACCOUNT_NUMBER)).thenReturn(Optional.of(sampleAccount));
//        when(accountHolderRepository.findByCpfHash(SAMPLE_CPF_HASH)).thenReturn(Optional.of(sampleAccountHolder));
//        when(bytesEncryptor.decrypt(sampleAccountHolder.getEncryptedCpf())).thenReturn(SAMPLE_CPF.getBytes());
//    }
//
//    private void verifyFindAndValidateAccountCalls() {
//        verify(accountRepository, times(1)).findByNumber(SAMPLE_ACCOUNT_NUMBER);
//        verify(accountHolderRepository, times(1)).findByCpfHash(SAMPLE_CPF_HASH);
//        verify(bytesEncryptor, times(1)).decrypt(sampleAccountHolder.getEncryptedCpf());
//    }
//
//    @Test
//    @DisplayName("Deve criar uma nova conta com sucesso")
//    void shouldCreateAccountSuccessfully() {
//        // Cenário de entrada
//        AccountRequestDTO dto = new AccountRequestDTO(name, SAMPLE_CPF, new BigDecimal("500.00"));
//
//        // Mocks:
//        when(accountHolderRepository.findByCpfHash(SAMPLE_CPF_HASH)).thenReturn(Optional.of(sampleAccountHolder));
//        when(bytesEncryptor.decrypt(ENCRYPTED_CPF)).thenReturn(SAMPLE_CPF.getBytes());
//        when(AccountNumberGenerator.generate()).thenReturn(SAMPLE_ACCOUNT_NUMBER); // Mockando estático
//        when(AccountNumberGenerator.getDefaultAgency()).thenReturn(SAMPLE_AGENCY); // Mockando estático
//        when(accountRepository.findByNumber(SAMPLE_ACCOUNT_NUMBER)).thenReturn(Optional.empty()); // Conta não existe
//        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount); // Retorno do save
//
//        // Ação
//        Account createdAccount = accountService.createAccount(dto);
//
//        // Verificações
//        assertNotNull(createdAccount);
//        assertEquals(SAMPLE_ACCOUNT_NUMBER, createdAccount.getNumber());
//        assertEquals(SAMPLE_AGENCY, createdAccount.getAgency());
//        assertEquals(new BigDecimal("500.00"), createdAccount.getBalance());
//        assertEquals(AccountStatus.ACTIVE, createdAccount.getStatus());
//        assertEquals(sampleAccountHolder, createdAccount.getAccountHolder());
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(SAMPLE_CPF_HASH);
//        verify(bytesEncryptor, times(1)).decrypt(ENCRYPTED_CPF);
//        verify(accountRepository, times(1)).findByNumber(SAMPLE_ACCOUNT_NUMBER);
//        verify(accountRepository, times(1)).save(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve criar conta com saldo inicial zero se DTO.initialBalance for nulo")
//    void shouldCreateAccountWithZeroInitialBalanceIfDtoInitialBalanceIsNull() {
//        // Cenário de entrada
//        AccountRequestDTO dto = new AccountRequestDTO(name, SAMPLE_CPF, null); // Saldo inicial nulo
//
//        // Mocks:
//        when(accountHolderRepository.findByCpfHash(SAMPLE_CPF_HASH)).thenReturn(Optional.of(sampleAccountHolder));
//        when(bytesEncryptor.decrypt(ENCRYPTED_CPF)).thenReturn(SAMPLE_CPF.getBytes());
//        when(AccountNumberGenerator.generate()).thenReturn(SAMPLE_ACCOUNT_NUMBER);
//        when(AccountNumberGenerator.getDefaultAgency()).thenReturn(SAMPLE_AGENCY);
//        when(accountRepository.findByNumber(SAMPLE_ACCOUNT_NUMBER)).thenReturn(Optional.empty());
//        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
//            Account acc = invocation.getArgument(0);
//            acc.setId(10L); // Simula o ID após salvar
//            return acc;
//        });
//
//        // Ação
//        Account createdAccount = accountService.createAccount(dto);
//
//        // Verificações
//        assertNotNull(createdAccount);
//        assertEquals(BigDecimal.ZERO, createdAccount.getBalance());
//
//        verify(accountRepository, times(1)).save(any(Account.class)); // Garante que o save foi chamado
//    }
//
//
//    @Test
//    @DisplayName("Deve lançar AccountHolderNotFoundException se o AccountHolder não for encontrado ao criar conta")
//    void shouldThrowAccountHolderNotFoundExceptionWhenCreatingAccount() {
//        AccountRequestDTO dto = new AccountRequestDTO(name, SAMPLE_CPF, BigDecimal.TEN);
//
//        when(accountHolderRepository.findByCpfHash(SAMPLE_CPF_HASH)).thenReturn(Optional.empty());
//
//        AccountHolderNotFoundException thrown = assertThrows(AccountHolderNotFoundException.class, () ->
//                accountService.createAccount(dto)
//        );
//
//        assertEquals("Account holder not found", thrown.getMessage());
//        verify(accountHolderRepository, times(1)).findByCpfHash(SAMPLE_CPF_HASH);
//        verify(bytesEncryptor, never()).decrypt(any(byte[].class)); // Nenhuma outra interação
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar CPFValidationException se o CPF do DTO não corresponder ao AccountHolder criptografado")
//    void shouldThrowCPFValidationExceptionWhenCpfMismatchOnCreateAccount() {
//        String wrongCpf = "99999999999";
//        AccountRequestDTO dto = new AccountRequestDTO(name, wrongCpf, BigDecimal.TEN);
//
//        when(accountHolderRepository.findByCpfHash(HashUtil.generateSha256(wrongCpf))).thenReturn(Optional.of(sampleAccountHolder));
//        when(bytesEncryptor.decrypt(ENCRYPTED_CPF)).thenReturn("differentCpf".getBytes()); // Retorna CPF diferente
//
//        CPFValidationException thrown = assertThrows(CPFValidationException.class, () ->
//                accountService.createAccount(dto)
//        );
//
//        assertEquals("Error validating CPF!", thrown.getMessage());
//        verify(accountHolderRepository, times(1)).findByCpfHash(HashUtil.generateSha256(wrongCpf));
//        verify(bytesEncryptor, times(1)).decrypt(ENCRYPTED_CPF);
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar AccountAlreadyExistsException se o número da conta já existir")
//    void shouldThrowAccountAlreadyExistsExceptionWhenAccountNumberExists() {
//        AccountRequestDTO dto = new AccountRequestDTO(name, SAMPLE_CPF, BigDecimal.TEN);
//
//        when(accountHolderRepository.findByCpfHash(SAMPLE_CPF_HASH)).thenReturn(Optional.of(sampleAccountHolder));
//        when(bytesEncryptor.decrypt(ENCRYPTED_CPF)).thenReturn(SAMPLE_CPF.getBytes());
//        when(AccountNumberGenerator.generate()).thenReturn(SAMPLE_ACCOUNT_NUMBER);
//        when(AccountNumberGenerator.getDefaultAgency()).thenReturn(SAMPLE_AGENCY);
//        when(accountRepository.findByNumber(SAMPLE_ACCOUNT_NUMBER)).thenReturn(Optional.of(sampleAccount));
//
//        AccountAlreadyExistsException thrown = assertThrows(AccountAlreadyExistsException.class, () ->
//                accountService.createAccount(dto)
//        );
//
//        assertEquals("Account already exists!", thrown.getMessage());
//        verify(accountRepository, times(1)).findByNumber(SAMPLE_ACCOUNT_NUMBER);
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve deletar uma conta com sucesso")
//    void shouldDeleteAccountSuccessfully() {
//        AccountDeletionDTO dto = new AccountDeletionDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//
//        mockFindAndValidateAccountSuccess();
//        doNothing().when(accountRepository).delete(any(Account.class));
//
//        assertDoesNotThrow(() -> accountService.deleteAccount(dto));
//
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, times(1)).delete(sampleAccount);
//    }
//
//    @Test
//    @DisplayName("Deve lançar AccountNotFoundException se a conta não for encontrada para deleção")
//    void shouldThrowAccountNotFoundExceptionOnDeleteAccount() {
//        // Cenário de entrada
//        AccountDeletionDTO dto = new AccountDeletionDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//
//        // Mocks: Conta não encontrada
//        when(accountRepository.findByNumber(SAMPLE_ACCOUNT_NUMBER)).thenReturn(Optional.empty());
//
//        // Ação e Verificação
//        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class, () ->
//                accountService.deleteAccount(dto)
//        );
//        assertEquals("Account not found", thrown.getMessage());
//        verify(accountRepository, times(1)).findByNumber(SAMPLE_ACCOUNT_NUMBER);
//        verify(accountHolderRepository, never()).findByCpfHash(anyString()); // Nenhuma outra interação
//        verify(accountRepository, never()).delete(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar InvalidAccountHolderException se a conta não pertencer ao AccountHolder fornecido para deleção")
//    void shouldThrowInvalidAccountHolderExceptionOnDeleteAccount() {
//        AccountDeletionDTO dto = new AccountDeletionDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//
//        when(accountRepository.findByNumber(SAMPLE_ACCOUNT_NUMBER)).thenReturn(Optional.of(sampleAccount));
//        when(accountHolderRepository.findByCpfHash(SAMPLE_CPF_HASH)).thenReturn(Optional.of(new AccountHolder("Other Holder", ENCRYPTED_CPF, SAMPLE_CPF_HASH) {{ setId(2L); }})); // Outro holder com ID diferente
//        when(bytesEncryptor.decrypt(any(byte[].class))).thenReturn(SAMPLE_CPF.getBytes());
//
//        InvalidAccountHolderException thrown = assertThrows(InvalidAccountHolderException.class, () ->
//                accountService.deleteAccount(dto)
//        );
//        assertEquals("The account " + SAMPLE_ACCOUNT_NUMBER + " doesn't belong to the provided CPF.", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, never()).delete(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve bloquear uma conta com sucesso")
//    void shouldBlockAccountSuccessfully() {
//        UpdateAccountStatusRequestDTO dto = new UpdateAccountStatusRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//        sampleAccount.setStatus(AccountStatus.ACTIVE);
//
//        mockFindAndValidateAccountSuccess();
//        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);
//
//        Account updatedAccount = accountService.blockAccount(dto);
//
//        assertNotNull(updatedAccount);
//        assertEquals(AccountStatus.BLOCKED, updatedAccount.getStatus());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, times(1)).save(sampleAccount);
//    }
//
//    @Test
//    @DisplayName("Deve desbloquear uma conta com sucesso")
//    void shouldUnblockAccountSuccessfully() {
//        UpdateAccountStatusRequestDTO dto = new UpdateAccountStatusRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//        sampleAccount.setStatus(AccountStatus.BLOCKED); // Garante que a conta está bloqueada inicialmente
//
//        mockFindAndValidateAccountSuccess();
//        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);
//
//        Account updatedAccount = accountService.unblockAccount(dto);
//
//        assertNotNull(updatedAccount);
//        assertEquals(AccountStatus.ACTIVE, updatedAccount.getStatus());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, times(1)).save(sampleAccount);
//    }
//
//    @Test
//    @DisplayName("Deve lançar AccountStatusException se a conta já estiver no status desejado (block)")
//    void shouldThrowAccountStatusExceptionWhenBlockingAlreadyBlockedAccount() {
//        UpdateAccountStatusRequestDTO dto = new UpdateAccountStatusRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//        sampleAccount.setStatus(AccountStatus.BLOCKED);
//
//        mockFindAndValidateAccountSuccess();
//
//        AccountStatusException thrown = assertThrows(AccountStatusException.class, () ->
//                accountService.blockAccount(dto)
//        );
//        assertEquals("Account " + SAMPLE_ACCOUNT_NUMBER + " already BLOCKED.", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar AccountStatusException se a conta já estiver no status desejado (unblock)")
//    void shouldThrowAccountStatusExceptionWhenUnblockingAlreadyActiveAccount() {
//        UpdateAccountStatusRequestDTO dto = new UpdateAccountStatusRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF);
//        sampleAccount.setStatus(AccountStatus.ACTIVE); // Já ativa
//
//        mockFindAndValidateAccountSuccess();
//
//        AccountStatusException thrown = assertThrows(AccountStatusException.class, () ->
//                accountService.unblockAccount(dto)
//        );
//        assertEquals("Account " + SAMPLE_ACCOUNT_NUMBER + " already ACTIVE.", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//
//    @Test
//    @DisplayName("Deve realizar um depósito com sucesso em conta ativa")
//    void shouldDepositSuccessfullyToActiveAccount() {
//        AccountDepositRequestDTO dto = new AccountDepositRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, new BigDecimal("200.00"));
//        sampleAccount.setBalance(new BigDecimal("500.00")); // Saldo inicial
//        sampleAccount.setStatus(AccountStatus.ACTIVE);
//
//        mockFindAndValidateAccountSuccess();
//        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount); // Simula o save da conta
//        when(operationRepository.save(any(Operation.class))).thenReturn(mock(Operation.class)); // Simula o save da operação
//
//        Account updatedAccount = accountService.deposit(dto);
//
//        assertNotNull(updatedAccount);
//        assertEquals(new BigDecimal("700.00"), updatedAccount.getBalance()); // 500 + 200
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, times(1)).save(sampleAccount);
//        verify(operationRepository, times(1)).save(argThat(op ->
//                op.getAmount().equals(new BigDecimal("200.00")) &&
//                        op.getOperationType() == OperationType.DEPOSIT &&
//                        op.getAccount().equals(sampleAccount)
//        ));
//    }
//
//    @Test
//    @DisplayName("Deve lançar BlockedAccountException ao tentar depositar em conta bloqueada")
//    void shouldThrowBlockedAccountExceptionWhenDepositingToBlockedAccount() {
//        AccountDepositRequestDTO dto = new AccountDepositRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, new BigDecimal("100.00"));
//        sampleAccount.setStatus(AccountStatus.BLOCKED); // Conta bloqueada
//
//        mockFindAndValidateAccountSuccess();
//
//        BlockedAccountException thrown = assertThrows(BlockedAccountException.class, () ->
//                accountService.deposit(dto)
//        );
//        assertEquals("Unable to deposit, the account is blocked!", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, never()).save(any(Account.class));
//        verify(operationRepository, never()).save(any(Operation.class));
//    }
//
//    @Test
//    @DisplayName("Deve realizar um saque com sucesso em conta ativa com fundos e limite diário")
//    void shouldWithdrawSuccessfullyFromActiveAccountWithFundsAndDailyLimit() {
//        AccountWithdrawalRequestDTO dto = new AccountWithdrawalRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, new BigDecimal("100.00"));
//        sampleAccount.setBalance(new BigDecimal("500.00")); // Saldo inicial
//        sampleAccount.setStatus(AccountStatus.ACTIVE);
//
//        mockFindAndValidateAccountSuccess();
//        when(operationRepository.findDailyWithdrawalSumByAccountNumber(
//                SAMPLE_ACCOUNT_NUMBER, OperationType.WITHDRAWAL.name(), LocalDate.now()))
//                .thenReturn(Optional.of(new BigDecimal("50.00"))); // Já sacou 50
//
//        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);
//        when(operationRepository.save(any(Operation.class))).thenReturn(mock(Operation.class));
//
//        Account updatedAccount = accountService.withdraw(dto);
//
//        assertNotNull(updatedAccount);
//        assertEquals(new BigDecimal("400.00"), updatedAccount.getBalance()); // 500 - 100
//        verifyFindAndValidateAccountCalls();
//        verify(operationRepository, times(1)).findDailyWithdrawalSumByAccountNumber(
//                SAMPLE_ACCOUNT_NUMBER, OperationType.WITHDRAWAL.name(), LocalDate.now());
//        verify(accountRepository, times(1)).save(sampleAccount);
//        verify(operationRepository, times(1)).save(argThat(op ->
//                op.getAmount().equals(new BigDecimal("100.00")) &&
//                        op.getOperationType() == OperationType.WITHDRAWAL &&
//                        op.getAccount().equals(sampleAccount)
//        ));
//    }
//
//    @Test
//    @DisplayName("Deve lançar BlockedAccountException ao tentar sacar de conta bloqueada")
//    void shouldThrowBlockedAccountExceptionWhenWithdrawingFromBlockedAccount() {
//        AccountWithdrawalRequestDTO dto = new AccountWithdrawalRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, new BigDecimal("100.00"));
//        sampleAccount.setStatus(AccountStatus.BLOCKED);
//
//        mockFindAndValidateAccountSuccess();
//
//        BlockedAccountException thrown = assertThrows(BlockedAccountException.class, () ->
//                accountService.withdraw(dto)
//        );
//        assertEquals("Unable to withdraw, the account is blocked!", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(accountRepository, never()).save(any(Account.class));
//        verify(operationRepository, never()).save(any(Operation.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar InsufficientFundsException ao tentar sacar com fundos insuficientes")
//    void shouldThrowInsufficientFundsExceptionWhenWithdrawingWithInsufficientFunds() {
//        AccountWithdrawalRequestDTO dto = new AccountWithdrawalRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, new BigDecimal("1000.00"));
//        sampleAccount.setBalance(new BigDecimal("500.00")); // Saldo insuficiente
//        sampleAccount.setStatus(AccountStatus.ACTIVE);
//
//        mockFindAndValidateAccountSuccess();
//
//        InsufficientFundsException thrown = assertThrows(InsufficientFundsException.class, () ->
//                accountService.withdraw(dto)
//        );
//        assertEquals("This account has not enough funds!", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(operationRepository, never()).findDailyWithdrawalSumByAccountNumber(anyString(), anyString(), any(LocalDate.class));
//        verify(accountRepository, never()).save(any(Account.class));
//        verify(operationRepository, never()).save(any(Operation.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar DailyLimitAlreadyReachedException ao exceder o limite diário de saque")
//    void shouldThrowDailyLimitAlreadyReachedExceptionWhenDailyLimitReached() {
//        AccountWithdrawalRequestDTO dto = new AccountWithdrawalRequestDTO(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, new BigDecimal("500.00"));
//        sampleAccount.setBalance(new BigDecimal("3000.00")); // Saldo suficiente
//        sampleAccount.setStatus(AccountStatus.ACTIVE);
//
//        mockFindAndValidateAccountSuccess();
//        when(operationRepository.findDailyWithdrawalSumByAccountNumber(
//                SAMPLE_ACCOUNT_NUMBER, OperationType.WITHDRAWAL.name(), LocalDate.now()))
//                .thenReturn(Optional.of(new BigDecimal("1800.00"))); // Já sacou 1800, mais 500 excede 2000
//
//        DailyLimitAlreadyReachedException thrown = assertThrows(DailyLimitAlreadyReachedException.class, () ->
//                accountService.withdraw(dto)
//        );
//        assertEquals("Daily limit of R$ " + DAILY_WITHDRAWAL_LIMIT + " reached. You have already withdrawn 1800.00 today.", thrown.getMessage());
//        verifyFindAndValidateAccountCalls();
//        verify(operationRepository, times(1)).findDailyWithdrawalSumByAccountNumber(
//                SAMPLE_ACCOUNT_NUMBER, OperationType.WITHDRAWAL.name(), LocalDate.now());
//        verify(accountRepository, never()).save(any(Account.class));
//        verify(operationRepository, never()).save(any(Operation.class));
//    }
//
//    @Test
//    @DisplayName("Deve retornar o extrato da conta com sucesso")
//    void shouldReturnAccountStatementSuccessfully() {
//        LocalDate startDate = LocalDate.of(2025, 1, 1);
//        LocalDate endDate = LocalDate.of(2025, 1, 31);
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
//
//        mockFindAndValidateAccountSuccess();
//
//        Operation op1 = new Operation(new BigDecimal("100.00"), OperationType.DEPOSIT, sampleAccount);
//        op1.setDateTime(LocalDateTime.of(2025, 1, 15, 10, 0));
//        Operation op2 = new Operation(new BigDecimal("50.00"), OperationType.WITHDRAWAL, sampleAccount);
//        op2.setDateTime(LocalDateTime.of(2025, 1, 20, 14, 30));
//        List<Operation> operations = Arrays.asList(op1, op2);
//
//        when(operationRepository.findByAccountNumberAndDateTimeBetween(
//                SAMPLE_ACCOUNT_NUMBER, startDateTime, endDateTime))
//                .thenReturn(operations);
//
//        List<OperationResponseDTO> statement = accountService.getAccountStatement(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, startDate, endDate);
//
//        assertNotNull(statement);
//        assertEquals(2, statement.size());
//        assertEquals(new BigDecimal("100.00"), statement.get(0).amount());
//        assertEquals(OperationType.DEPOSIT, statement.get(0).operationType());
//        assertEquals(SAMPLE_ACCOUNT_NUMBER, statement.get(0).accountNumber());
//
//        assertEquals(new BigDecimal("50.00"), statement.get(1).amount());
//        assertEquals(OperationType.WITHDRAWAL, statement.get(1).operationType());
//        assertEquals(SAMPLE_ACCOUNT_NUMBER, statement.get(1).accountNumber());
//
//        verifyFindAndValidateAccountCalls();
//        verify(operationRepository, times(1)).findByAccountNumberAndDateTimeBetween(
//                SAMPLE_ACCOUNT_NUMBER, startDateTime, endDateTime);
//    }
//
//    @Test
//    @DisplayName("Deve retornar extrato vazio se não houver operações no período")
//    void shouldReturnEmptyStatementIfNoOperationsInPeriod() {
//        LocalDate startDate = LocalDate.of(2025, 2, 1);
//        LocalDate endDate = LocalDate.of(2025, 2, 28);
//        LocalDateTime startDateTime = startDate.atStartOfDay();
//        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
//
//        mockFindAndValidateAccountSuccess();
//        when(operationRepository.findByAccountNumberAndDateTimeBetween(
//                SAMPLE_ACCOUNT_NUMBER, startDateTime, endDateTime))
//                .thenReturn(Collections.emptyList()); // Nenhuma operação
//
//        List<OperationResponseDTO> statement = accountService.getAccountStatement(SAMPLE_ACCOUNT_NUMBER, SAMPLE_CPF, startDate, endDate);
//
//        assertNotNull(statement);
//        assertTrue(statement.isEmpty());
//
//        verifyFindAndValidateAccountCalls();
//        verify(operationRepository, times(1)).findByAccountNumberAndDateTimeBetween(
//                SAMPLE_ACCOUNT_NUMBER, startDateTime, endDateTime);
//    }
//
//}