package com.baas.contas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baas.contas.entity.Transacao;
import com.baas.contas.repository.ContaRepository;
import com.baas.contas.repository.TransacaoRepository;
import com.baas.contas.service.ContaService;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    ContaService contaService;

    @GetMapping("/{idTransacao}")
    public ResponseEntity<Transacao> getTransacao(@PathVariable("idTransacao") Long idTransacao) {
        Optional<Transacao> transacao = transacaoRepository.findById(idTransacao);

        if (transacao.isPresent()) {
            return new ResponseEntity<>(transacao.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping()
    public ResponseEntity<List<Transacao>> getAllTransacao() {
        List<Transacao> contas = new ArrayList<Transacao>();

        transacaoRepository.findAll().forEach(contas::add);

        if (contas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(contas, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Transacao> transacao(@RequestBody Transacao transacao) {
        
        return new ResponseEntity<>(contaService.transacao(transacao), HttpStatus.CREATED);
    }

}
