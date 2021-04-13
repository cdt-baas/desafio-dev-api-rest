package com.baas.contas.dto;


public class SaldoDto {

    private long idConta;
    private Double saldo;
    
    public SaldoDto() {
    }

    public SaldoDto(long idConta, Double saldo) {
        this.idConta = idConta;
        this.saldo = saldo;
    }

    public long getIdConta() {
        return idConta;
    }

    public void setIdConta(long idConta) {
        this.idConta = idConta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    
}
