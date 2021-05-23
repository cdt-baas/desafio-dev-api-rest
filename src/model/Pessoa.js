module.exports = {
  Pessoa: class Pessoa {
    idPessoa = null;
    nome = '';
    cpf = '';
    dataNascimento = null;

    constructor(idPessoa, nome, cpf, dataNascimento) {
      this.idPessoa = idPessoa;
      this.nome = nome;
      this.cpf = cpf;
      this.dataNascimento = dataNascimento;
    }
  },
};
