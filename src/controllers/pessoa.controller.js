const typeorm = require('typeorm');
const Pessoa = require('../model/Pessoa').Pessoa;

module.exports = {
  PessoaCtrl: class {
    static save(idPessoa, nome, cpf, dataNascimento) {
      return new Promise(async (resolve) => {
        const pessoa = new Pessoa(idPessoa, nome, cpf, dataNascimento);
        typeorm
          .getConnection()
          .manager.save([pessoa])
          .then(() => {
            resolve(pessoa);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }

    static get(idPessoa) {
      return new Promise(async (resolve) => {
        typeorm
          .getConnection()
          .getRepository(Pessoa)
          .findOne({ where: { idPessoa: idPessoa } })
          .then((element) => {
            resolve(element || false);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }
  },
};
