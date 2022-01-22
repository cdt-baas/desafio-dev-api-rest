const Services = require('./Services')
const database = require('../models')
const ContasServices = require('./ContasServices')

class TransacoesServices extends Services {
    constructor() {
        super('Transacoes')
        this.contas = new ContasServices()
    }

    criaDebito = (transacaoOriginal) => database.sequelize.transaction(
        transactionSQL => super.criaRegistro(transacaoOriginal, transactionSQL)
            .then(async ({dataValues}) => this.contas.atualizaContaPorDebito(dataValues, dataValues.conta_id, transactionSQL))
    )

    criaCredito = (transacaoOriginal) => database.sequelize.transaction(
        transactionSQL => super.criaRegistro(transacaoOriginal, transactionSQL)
            .then(async ({dataValues}) => this.contas.atualizaContaPorCredito(dataValues, dataValues.conta_id, transactionSQL))
    )
}

module.exports = TransacoesServices