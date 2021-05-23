module.exports = {
  Conta: class {
    idConta = null;
    pessoa = null;
    saldo = 0;
    limiteSaqueDiario = 0;
    flagAtivo = 1;
    tipoConta = 0;
    dataCriacao = null;

    constructor(idConta, pessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta, dataCriacao) {
      this.idConta = idConta;
      this.pessoa = pessoa;
      this.saldo = saldo;
      this.limiteSaqueDiario = limiteSaqueDiario;
      this.flagAtivo = flagAtivo;
      this.tipoConta = tipoConta;
      this.dataCriacao = dataCriacao;
    }
  },
};
