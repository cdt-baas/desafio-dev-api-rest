package com.baas.contas.repository;

//import java.util.Optional;
import org.springframework.data.jpa.repository.*;

import com.baas.contas.entity.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long>{

//    Optional<Conta> findBySku(String Sku);
//    Optional<Conta> deleteBySku(String sku);

}
