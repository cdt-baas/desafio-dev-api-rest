'use strict';
module.exports = {
  up: async (queryInterface, Sequelize) => {
    await queryInterface.createTable('Contas', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      status: {
        type: Sequelize.STRING
      },
      saldo: {
        type: Sequelize.FLOAT
      },
      agencia: {
        allowNull: false,
        type: Sequelize.INTEGER
      },
      portador_id: {
        allowNull: false,
        type: Sequelize.INTEGER,
        references: {model: 'Portadores', key: 'id'}
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },
  down: async (queryInterface, Sequelize) => {
    await queryInterface.dropTable('Contas');
  }
};