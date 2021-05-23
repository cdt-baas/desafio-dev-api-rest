const EntitySchema = require('typeorm').EntitySchema;

const Conta = require('../model/Conta').Conta;

module.exports = new EntitySchema({
  tableName: 'contas',
  name: 'Conta',
  target: Conta,
  columns: {
    idConta: {
      primary: true,
      type: 'int',
      generated: true,
    },
    saldo: {
      type: 'float',
    },
    limiteSaqueDiario: {
      type: 'float',
    },
    tipoConta: {
      type: 'int',
    },
    dataCriacao: {
      type: 'date',
    },
    flagAtivo: {
      type: 'boolean',
    },
  },
  relations: {
    pessoa: {
      target: 'Pessoa',
      joinColumn: {
        name: 'idPessoa',
        referencedColumnName: 'idPessoa',
      },
      type: 'many-to-one',
      joinTable: true,
      cascade: true,
    },
  },
});
