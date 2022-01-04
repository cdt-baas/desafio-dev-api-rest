package com.baas.contas.repository;

import org.springframework.data.jpa.repository.*;

import com.baas.contas.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
