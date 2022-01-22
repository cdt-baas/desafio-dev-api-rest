'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class Transacoes extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  };
  Transacoes.init({
    valor: DataTypes.FLOAT,
    tipo: DataTypes.STRING,
    liquidado: DataTypes.BOOLEAN
  }, {
    sequelize,
    modelName: 'Transacoes',
  });
  Transacoes.associate = models => {
    Transacoes.belongsTo(models.Contas, {
      foreignKey: 'conta_id'
    })
  }
  return Transacoes;
};