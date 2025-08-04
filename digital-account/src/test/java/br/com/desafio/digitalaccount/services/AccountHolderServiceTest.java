//package br.com.desafio.digitalaccount.services;
//
//import br.com.desafio.digitalaccount.domain.accountHolder.AccountHolder;
//import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.AccountHolderAlreadyExistsException;
//import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.CPFNotFoundException;
//import br.com.desafio.digitalaccount.dto.accountHolder.AccountHolderRequestDTO;
//import br.com.desafio.digitalaccount.repositories.AccountHolderRepository;
//import br.com.desafio.digitalaccount.utils.HashUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.encrypt.BytesEncryptor;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AccountHolderServiceTest {
//
//    @Mock
//    private AccountHolderRepository accountHolderRepository;
//
//    @Mock
//    private BytesEncryptor bytesEncryptor;
//
//    @InjectMocks
//    private AccountHolderService accountHolderService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("Deve salvar um novo portador de conta quando o CPF não existe")
//    void shouldSaveNewAccountHolderWhenCpfDoesNotExist() {
//        String name = "Teste Nome";
//        String cpf = "12345678900";
//        AccountHolderRequestDTO dto = new AccountHolderRequestDTO(name, cpf);
//
//        when(accountHolderRepository.findByCpfHash(anyString())).thenReturn(Optional.empty());
//
//        byte[] encryptedCpfBytes = "encryptedBytes".getBytes();
//        when(bytesEncryptor.encrypt(cpf.getBytes())).thenReturn(encryptedCpfBytes);
//
//        when(accountHolderRepository.save(any(AccountHolder.class))).thenAnswer(invocation -> {
//            AccountHolder savedAccountHolder = invocation.getArgument(0);
//            if (savedAccountHolder.getId() == null) {
//                savedAccountHolder.setId(1L);
//            }
//            return savedAccountHolder;
//        });
//
//        assertDoesNotThrow(() -> accountHolderService.save(dto));
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(HashUtil.generateSha256(cpf));
//        verify(bytesEncryptor, times(1)).encrypt(cpf.getBytes());
//        verify(accountHolderRepository, times(1)).save(argThat(ah ->
//                ah.getName().equals(name) &&
//                        new String(ah.getEncryptedCpf()).equals(new String(encryptedCpfBytes)) &&
//                        ah.getCpfHash().equals(HashUtil.generateSha256(cpf))
//        ));
//    }
//
//    @Test
//    @DisplayName("Deve lançar AccountHolderAlreadyExistsException se o CPF já existe ao salvar")
//    void shouldThrowAccountHolderAlreadyExistsExceptionWhenCpfExistsOnSave() {
//        String name = "Existente";
//        String cpf = "11122233344";
//        AccountHolderRequestDTO dto = new AccountHolderRequestDTO(name, cpf);
//        when(accountHolderRepository.findByCpfHash(anyString()))
//                .thenReturn(Optional.of(new AccountHolder(name, "encrypted".getBytes(), HashUtil.generateSha256(cpf))));
//
//        AccountHolderAlreadyExistsException thrown = assertThrows(AccountHolderAlreadyExistsException.class, () ->
//                accountHolderService.save(dto)
//        );
//
//        assertEquals("Account holder already exists!", thrown.getMessage());
//
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(HashUtil.generateSha256(cpf));
//        verify(bytesEncryptor, never()).encrypt(any(byte[].class));
//        verify(accountHolderRepository, never()).save(any(AccountHolder.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar RuntimeException se ocorrer um erro inesperado ao salvar")
//    void shouldThrowRuntimeExceptionOnUnexpectedErrorWhenSaving() {
//        String name = "Erro Teste";
//        String cpf = "99988877766";
//        AccountHolderRequestDTO dto = new AccountHolderRequestDTO(name, cpf);
//
//        when(accountHolderRepository.findByCpfHash(anyString())).thenReturn(Optional.empty());
//        when(bytesEncryptor.encrypt(any(byte[].class))).thenThrow(new RuntimeException("Erro de criptografia mockado"));
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
//                accountHolderService.save(dto)
//        );
//
//        assertEquals("Erro de criptografia mockado", thrown.getCause().getMessage());
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(anyString());
//        verify(bytesEncryptor, times(1)).encrypt(any(byte[].class));
//        verify(accountHolderRepository, never()).save(any(AccountHolder.class));
//    }
//
//
//    @Test
//    @DisplayName("Deve deletar um portador de conta com sucesso quando o CPF existe e corresponde")
//    void shouldDeleteAccountHolderSuccessfullyWhenCpfExistsAndMatches() {
//        String cpfToDelete = "11122233300";
//        String cpfHash = HashUtil.generateSha256(cpfToDelete);
//        byte[] encryptedCpf = "encryptedCpfOf11122233300".getBytes();
//
//        AccountHolder existingAccountHolder = new AccountHolder("Delete Me", encryptedCpf, cpfHash);
//        existingAccountHolder.setId(10L);
//        when(accountHolderRepository.findByCpfHash(cpfHash)).thenReturn(Optional.of(existingAccountHolder));
//
//        when(bytesEncryptor.decrypt(encryptedCpf)).thenReturn(cpfToDelete.getBytes());
//
//        doNothing().when(accountHolderRepository).delete(any(AccountHolder.class));
//
//        assertDoesNotThrow(() -> accountHolderService.delete(cpfToDelete));
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(cpfHash);
//        verify(bytesEncryptor, times(1)).decrypt(encryptedCpf);
//        verify(accountHolderRepository, times(1)).delete(existingAccountHolder);
//    }
//
//    @Test
//    @DisplayName("Deve lançar CPFNotFoundException se o CPF não for encontrado para deleção")
//    void shouldThrowCPFNotFoundExceptionWhenCpfNotFoundOnDelete() {
//        String cpfToDelete = "00000000000";
//        String cpfHash = HashUtil.generateSha256(cpfToDelete);
//
//        when(accountHolderRepository.findByCpfHash(cpfHash)).thenReturn(Optional.empty());
//
//        CPFNotFoundException thrown = assertThrows(CPFNotFoundException.class, () ->
//                accountHolderService.delete(cpfToDelete)
//        );
//
//        assertEquals("Cpf not found for account holder", thrown.getMessage());
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(cpfHash);
//        verify(bytesEncryptor, never()).decrypt(any(byte[].class));
//        verify(accountHolderRepository, never()).delete(any(AccountHolder.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar CPFNotFoundException se o CPF informado não corresponder ao CPF persistido")
//    void shouldThrowCPFNotFoundExceptionWhenCpfMismatchOnDelete() {
//        String inputCpf = "12345678900";
//        String inputCpfHash = HashUtil.generateSha256(inputCpf);
//
//        String actualPersistedCpf = "98765432100";
//        byte[] encryptedActualCpf = "encrypted98765432100".getBytes(); // Estes bytes correspondem a actualPersistedCpf
//        AccountHolder existingAccountHolder = new AccountHolder("Fulano", encryptedActualCpf, inputCpfHash);
//        existingAccountHolder.setId(5L);
//
//        when(accountHolderRepository.findByCpfHash(inputCpfHash)).thenReturn(Optional.of(existingAccountHolder));
//
//        when(bytesEncryptor.decrypt(encryptedActualCpf)).thenReturn(actualPersistedCpf.getBytes());
//
//        CPFNotFoundException thrown = assertThrows(CPFNotFoundException.class, () ->
//                accountHolderService.delete(inputCpf)
//        );
//
//        assertEquals("CPF does not match", thrown.getMessage());
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(inputCpfHash);
//        verify(bytesEncryptor, times(1)).decrypt(encryptedActualCpf);
//        verify(accountHolderRepository, never()).delete(any(AccountHolder.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar RuntimeException se ocorrer um erro inesperado ao deletar")
//    void shouldThrowRuntimeExceptionOnUnexpectedErrorWhenDeleting() {
//        String cpfToDelete = "55544433322";
//        String cpfHash = HashUtil.generateSha256(cpfToDelete);
//        byte[] encryptedCpf = "someEncryptedCpf".getBytes();
//
//        AccountHolder existingAccountHolder = new AccountHolder("Error Delete", encryptedCpf, cpfHash);
//        when(accountHolderRepository.findByCpfHash(cpfHash)).thenReturn(Optional.of(existingAccountHolder));
//
//        when(bytesEncryptor.decrypt(any(byte[].class))).thenThrow(new RuntimeException("Erro de descriptografia inesperado"));
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
//                accountHolderService.delete(cpfToDelete)
//        );
//
//        assertEquals("Erro de descriptografia inesperado", thrown.getCause().getMessage());
//
//        verify(accountHolderRepository, times(1)).findByCpfHash(cpfHash);
//        verify(bytesEncryptor, times(1)).decrypt(any(byte[].class));
//        verify(accountHolderRepository, never()).delete(any(AccountHolder.class));
//    }
//}