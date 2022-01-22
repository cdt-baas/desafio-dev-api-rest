'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert('Contas', [
            {
                status: 'ativa',
                saldo: 1000,
                agencia: 1,
                portador_id: 1, 
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                status: 'ativa',
                saldo: 1000,
                agencia: 1,
                portador_id: 2, 
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                status: 'bloqueada',
                saldo: 1000,
                agencia: 1,
                portador_id: 3, 
                createdAt: new Date(),
                updatedAt: new Date()
            }
    ], {})
  },

  down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete('Contas', null, {})
  }
}

