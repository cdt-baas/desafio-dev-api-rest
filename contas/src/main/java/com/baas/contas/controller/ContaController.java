package com.baas.contas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baas.contas.dto.SaldoDto;
import com.baas.contas.entity.Conta;
import com.baas.contas.entity.Transacao;
import com.baas.contas.exception.*;
import com.baas.contas.repository.ContaRepository;
import com.baas.contas.repository.TransacaoRepository;
import com.baas.contas.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    ContaService contaService;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransacaoRepository transacaoRepository;


    @GetMapping("/{idConta}")
    public ResponseEntity<Conta> getConta(@PathVariable("idConta") long idConta) {
        Optional<Conta> conta = contaRepository.findById(idConta);

        if (conta.isPresent()) {
            return new ResponseEntity<>(conta.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping()
    public ResponseEntity<List<Conta>> getAllConta() {
        List<Conta> contas = new ArrayList<Conta>();

        contaRepository.findAll().forEach(contas::add);

        if (contas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(contas, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Conta> createConta(@RequestBody Conta conta) {

        if (contaRepository.findById(conta.getIdConta()).isPresent()) {
          throw new ContaExistException();
        }

        Conta _conta = contaRepository.save(conta);

        return new ResponseEntity<>(_conta, HttpStatus.CREATED);
    }


    @DeleteMapping("/{idConta}")
    public ResponseEntity<Conta> deleteConta(@PathVariable("idConta") long idConta) {

        Optional<Conta> _conta = contaRepository.findById(idConta);
        if (_conta.isPresent()) {
            contaRepository.delete(_conta.get());
            return new ResponseEntity<Conta>(_conta.get(), HttpStatus.OK);
        }
        return new ResponseEntity<Conta>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{idConta}/saldo")
    public ResponseEntity<SaldoDto> getSaldo(@PathVariable("idConta") long idConta) {
        Optional<Conta> conta = contaRepository.findById(idConta);

        SaldoDto saldo = new SaldoDto();
        saldo.setIdConta(conta.get().getIdConta());
        saldo.setSaldo(conta.get().getSaldo());

        if (conta.isPresent()) {
            return new ResponseEntity<>(saldo, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PatchMapping("/{idConta}/bloqueio")
    public ResponseEntity<Conta> blockConta(@PathVariable("idConta") long idConta) {

        Optional<Conta> _conta = contaRepository.findById(idConta);
        if (_conta.isEmpty()) {
            return new ResponseEntity<Conta>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(contaService.bloqueiaConta(_conta.get()), HttpStatus.CREATED);

    }

    @GetMapping("/{idConta}/extrato")
    public ResponseEntity<List<Transacao>> getExtrato(@PathVariable("idConta") Long idConta) {
        
        Optional<Conta> _conta = contaRepository.findById(idConta);

        if (_conta.isPresent()) {
            return new ResponseEntity<>(contaService.extratoConta(_conta.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
