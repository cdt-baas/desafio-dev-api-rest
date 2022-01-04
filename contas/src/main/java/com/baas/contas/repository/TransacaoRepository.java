package com.baas.contas.repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

import com.baas.contas.entity.Conta;
import com.baas.contas.entity.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

    Optional<List<Transacao>> findByConta(Conta conta);

}
