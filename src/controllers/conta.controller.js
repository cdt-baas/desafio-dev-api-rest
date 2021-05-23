const typeorm = require('typeorm');
const Conta = require('../model/Conta').Conta;
const Transacao = require('../model/Transacao').Transacao;

module.exports = {
  ContaCtrl: class {
    static init() {
      return new Promise(async (resolve) => {
        typeorm
          .getConnection()
          .query(
            `CREATE TRIGGER IF NOT EXISTS updateSaldo AFTER INSERT ON transacoes 
              FOR EACH ROW BEGIN 
                UPDATE contas SET saldo = saldo + NEW.valor WHERE idConta = NEW.idConta; 
              END`
          )
          .then(() => {
            resolve(true);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }

    static save({ pessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta }) {
      return new Promise(async (resolve) => {
        const conta = new Conta(null, pessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta, new Date());
        typeorm
          .getConnection()
          .manager.save([conta])
          .then(() => {
            resolve(conta);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }

    static get(idConta) {
      return new Promise(async (resolve) => {
        typeorm
          .getConnection()
          .getRepository(Conta)
          .findOne({ relations: ['pessoa'], where: { idConta: idConta } })
          .then((element) => {
            resolve(element || false);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }

    static transacao({ conta, valor }) {
      return new Promise(async (resolve) => {
        const transacao = new Transacao(null, conta, valor, new Date());
        typeorm
          .getConnection()
          .manager.save([transacao])
          .then(() => {
            resolve(transacao);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }

    static bloquear(idConta) {
      return new Promise(async (resolve, reject) => {
        typeorm
          .getConnection()
          .getRepository(Conta)
          .update({ idConta: idConta }, { flagAtivo: 0 })
          .then((element) => {
            resolve(element || false);
          })
          .catch(function () {
            resolve(false);
          });
      });
    }

    static extrato(conta, inicial, final) {
      return new Promise(async (resolve) => {
        const where = [` idConta = ${conta.idConta} `];
        if (inicial && final) {
          where.push(` and dataTransacao between '${inicial}' and '${final}' `);
        }

        typeorm
          .getConnection()
          .getRepository(Transacao)
          .find({ where: where.join('') })
          .then((elements) => {
            resolve(
              elements.map((e) => {
                return {
                  valor: Math.abs(e.valor),
                  operacao: e.valor > 0 ? 'Deposito' : 'Saque',
                  data: e.dataTransacao,
                };
              })
            );
          })
          .catch(function () {
            resolve(false);
          });
      });
    }
  },
};
