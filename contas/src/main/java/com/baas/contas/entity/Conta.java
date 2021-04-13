package com.baas.contas.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Conta {

    @Id
    private Long idConta;

    @OneToOne(targetEntity=Pessoa.class, cascade = CascadeType.ALL)
    private Pessoa pessoa;

    private Double saldo;
    private Double limiteSaqueDiario;
    private boolean flagAtivo;
    private int tipoConta;

    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dataCriacao;

    public Conta() {
    }

    public Conta(Long idConta, Pessoa pessoa, Double saldo, Double limiteSaqueDiario, boolean flagAtivo, int tipoConta, Date dataCriacao) {
        this.idConta = idConta;
        this.pessoa = pessoa;
        this.saldo = saldo;
        this.limiteSaqueDiario = limiteSaqueDiario;
        this.flagAtivo = flagAtivo;
        this.tipoConta = tipoConta;
        this.dataCriacao = dataCriacao;
    }

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getLimiteSaqueDiario() {
        return limiteSaqueDiario;
    }

    public void setLimiteSaqueDiario(Double limiteSaqueDiario) {
        this.limiteSaqueDiario = limiteSaqueDiario;
    }

    public boolean isFlagAtivo() {
        return flagAtivo;
    }

    public void setFlagAtivo(boolean flagAtivo) {
        this.flagAtivo = flagAtivo;
    }

    public int getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(int tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
