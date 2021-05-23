module.exports = {
  Transacao: class {
    idTransacao = null;
    conta = null;
    valor = 0;
    dataTransacao = null;

    constructor(idTransacao, conta, valor, dataTransacao) {
      this.idTransacao = idTransacao;
      this.conta = conta;
      this.valor = valor;
      this.dataTransacao = dataTransacao;
    }
  },
};
