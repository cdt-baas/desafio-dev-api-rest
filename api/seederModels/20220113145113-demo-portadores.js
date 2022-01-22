'use strict';

module.exports = {
  up: async (queryInterface, Sequelize) => {
        return queryInterface.bulkInsert('Portadores', [
            {
                nome: 'Ana Souza',
                ativo: true,
                cpf: '38484462005', 
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                nome: 'Marcos Cintra',
                ativo: true,
                cpf: '98476512058',
                createdAt: new Date(),
                updatedAt: new Date()
            },
            {
                nome: 'Felipe Cardoso',
                ativo: false,
                cpf: '95425043066',
                createdAt: new Date(),
                updatedAt: new Date()
            },
    ], {})
  },

  down: async (queryInterface, Sequelize) => {
        return queryInterface.bulkDelete('Portadores', null, {})
  }
}

