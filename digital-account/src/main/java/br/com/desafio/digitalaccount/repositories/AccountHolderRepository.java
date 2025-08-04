package br.com.desafio.digitalaccount.repositories;

import br.com.desafio.digitalaccount.domain.accountHolder.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
    Optional<AccountHolder> findByCpfHash(String cpfHash);
}
