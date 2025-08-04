package br.com.desafio.digitalaccount.domain.accountHolder;

import br.com.desafio.digitalaccount.domain.account.Account;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_holder")
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "encrypted_cpf", nullable = false)
    private byte[] encryptedCpf; // CPF criptografado com AES (reversível)

    @Column(name = "cpf_hash", nullable = false, unique = true, length = 64)
    private String cpfHash; // Hash do CPF original (não reversível, para busca)

    @OneToMany(mappedBy = "accountHolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public AccountHolder() {}

    public AccountHolder(String name, byte[] encryptedCpf, String cpfHash) {
        this.name = name;
        this.encryptedCpf = encryptedCpf;
        this.cpfHash = cpfHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getEncryptedCpf() {
        return encryptedCpf;
    }

    public void setEncryptedCpf(byte[] encryptedCpf) {
        this.encryptedCpf = encryptedCpf;
    }

    public String getCpfHash() {
        return cpfHash;
    }

    public void setCpfHash(String cpfHash) {
        this.cpfHash = cpfHash;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
