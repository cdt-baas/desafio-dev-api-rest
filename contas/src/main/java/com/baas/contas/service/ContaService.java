package com.baas.contas.service;

import java.util.List;
import java.util.Optional;

import com.baas.contas.entity.Conta;
import com.baas.contas.entity.Transacao;
import com.baas.contas.exception.ContaBloqueadaException;
import com.baas.contas.exception.SaldoInsuficienteException;
import com.baas.contas.repository.ContaRepository;
import com.baas.contas.repository.TransacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransacaoRepository transacaoRepository;

    public Conta getConta(long idConta) {
        
        Optional<Conta> conta = contaRepository.findById(idConta);

        return conta.get();

    }

    public Conta bloqueiaConta(Conta conta) {
        
        conta.setFlagAtivo(false);

        return contaRepository.save(conta);

    }

    public List<Transacao> extratoConta(Conta conta) {
        Optional<List<Transacao>> transacao = transacaoRepository.findByConta(conta);

        return transacao.get();
    }

    public Transacao transacao(Transacao transacao) {
        Optional<Conta> _conta = contaRepository.findById(transacao.getConta().getIdConta());

        verificaContaAtiva(_conta.get());

        Double vlrAtual = _conta.get().getSaldo();
        Double vlrTransacao = transacao.getValor();

        verificaContaTemSaldo(vlrAtual, vlrTransacao);

        _conta.get().setSaldo(vlrAtual+vlrTransacao);
        contaRepository.save(_conta.get());
        
        return transacaoRepository.save(transacao);

    }

    public void verificaContaAtiva(Conta conta) {
        if(!conta.isFlagAtivo()) {
            throw new ContaBloqueadaException();
        }
    }

    public void verificaContaTemSaldo(Double vlrAtual, Double vlrTransacao) {
        if (vlrAtual+vlrTransacao<0) {
            throw new SaldoInsuficienteException();
        }
    }

}
