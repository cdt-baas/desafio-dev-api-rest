package com.baas.contas.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Transacao {

    @Id
    private Long idTransacao;

    @ManyToOne
    private Conta conta;

    private Double valor;

    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dataTransacao;

    public Transacao() {
    }

    public Transacao(Long idTransacao, Conta conta, Double valor, Date dataTransacao) {
        this.idTransacao = idTransacao;
        this.conta = conta;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
    }

    public Long getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(Long idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
    
}
