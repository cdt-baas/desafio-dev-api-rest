const EntitySchema = require('typeorm').EntitySchema;

const Transacao = require('../model/Transacao').Transacao;

module.exports = new EntitySchema({
  tableName: 'transacoes',
  name: 'Transacao',
  target: Transacao,
  columns: {
    idTransacao: {
      primary: true,
      type: 'int',
      generated: true,
    },
    valor: {
      type: 'float',
    },
    dataTransacao: {
      type: 'date',
    },
  },
  relations: {
    conta: {
      target: 'Conta',
      joinColumn: {
        name: 'idConta',
        referencedColumnName: 'idConta',
      },
      type: 'many-to-one',
      joinTable: true,
      cascade: true,
    },
  },
});
