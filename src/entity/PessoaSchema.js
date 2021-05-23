const EntitySchema = require('typeorm').EntitySchema;

const Pessoa = require('../model/Pessoa').Pessoa;

module.exports = new EntitySchema({
  tableName: 'pessoas',
  name: 'Pessoa',
  target: Pessoa,
  columns: {
    idPessoa: {
      primary: true,
      type: 'int',
      generated: true,
    },
    nome: {
      type: 'varchar',
    },
    cpf: {
      type: 'varchar',
    },
    dataNascimento: {
      type: 'date',
    },
  },
});
