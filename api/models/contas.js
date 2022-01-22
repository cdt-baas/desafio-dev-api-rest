'use strict';
const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class Contas extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  };
  Contas.init({
    status: DataTypes.STRING,
    saldo: DataTypes.FLOAT,
    agencia: DataTypes.INTEGER
  }, {
    sequelize,
    modelName: 'Contas',
    paranoid: true
  });
  Contas.associate = models => {
    Contas.belongsTo(models.Portadores, {
      foreignKey: 'portador_id'
    })
    Contas.hasMany(models.Transacoes, {
      foreignKey: 'conta_id',
      scope: { tipo: 'debito' },
      as: 'transacoesDebito'
    })
  }
  return Contas;
};