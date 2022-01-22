'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert('Transacoes', [
            {
                valor: 2000,
                tipo: 'credito',
                conta_id: 1,
                liquidado: true,
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                valor: 1000,
                tipo: 'debito',
                conta_id: 1,
                liquidado: true,
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                valor: 2000,
                tipo: 'credito',
                conta_id: 2,
                liquidado: true,
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                valor: 1000,
                tipo: 'debito',
                conta_id: 2,
                liquidado: true,
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                valor: 2000,
                tipo: 'credito',
                conta_id: 3,
                liquidado: true,
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                valor: 1000,
                tipo: 'debito',
                conta_id: 3,
                liquidado: true,
                createdAt: new Date(),
                updatedAt: new Date()
            }
    ], {})
  },

  down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete('Transacoes', null, {})
  }
}

