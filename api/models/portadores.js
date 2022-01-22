'use strict';
const {
  Model
} = require('sequelize');
const Sequelize = require('sequelize')

const validator = require("validator-brazil");

module.exports = (sequelize, DataTypes) => {
  class Portadores extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
    }
  };
  Portadores.init({
    nome: DataTypes.STRING,
    ativo: DataTypes.BOOLEAN,
    cpf: {
      type          : DataTypes.STRING,
      allowNull     : false,
      unique        : true,
      validate      : {
        isCPF       : (value) => {
          if (!validator.isCpf(value)) throw new Error('O cpf não é válido')
        },
        isUnique    : function(value, next) {
          Portadores.findAll({
            attributes: ['cpf'],
            having: Sequelize.literal(`cpf = ${value}`)
          })
          .done(function(error, user) {
            if (error.length !== 0) return next('CPF já está em uso!');
            next();
          });
        }
      }
    },
  }, {
    sequelize,
    modelName: 'Portadores',
    paranoid: true,
    defaultScope: {
      where: { ativo: true }
    },
    scopes: {
      all: { where: {} }
    }
  });
  Portadores.associate = models => {
    Portadores.hasMany(models.Contas, {
      foreignKey: 'portador_id',
      scope: { status: 'ativa' },
      as: 'contasAtivas'
    })
  }
  return Portadores;
};