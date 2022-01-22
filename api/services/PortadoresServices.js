const Services = require('./Services')
const database = require('../models')

class PortadoresServices extends Services {
    constructor() {
        super('Portadores')
        this.contas = new Services('Contas')
    }

    pegaRegistrosAtivos = (where = {}) => database[this.nomeModelo].findAll({ where: { ...where } })

    pegaRegistros = (where = {}) => database[this.nomeModelo]
        .scope('all')
        .findAll({ where: { ...where } })

    bloqueiaPortadorEConta = async (portadorId) => {
        const transaction = database.sequelize.transaction()
        const _ = super.atualizaRegistro({ ativo: false }, portadorId, { transaction })
        console.log('_', _)
        if (_[0] === 0) throw new Error('Não foi possivel bloquear ao portador, tente novamente')
        const conta = await this.contas.pegaRegistros({ portador_id: portadorId }, { transaction })
        if (conta.length === 0) return {message: 'O portador foi bloqueado mas não tem contas'}
        await this.contas.atualizaRegistros({ status: 'bloqueada' }, { portador_id: portadorId }, { transaction })
        return { message: `Contas referenciadas no portador id:${portadorId} foram bloqueadas` }
    }

    desbloqueiaPortadorEConta = async (portadorId) => {
        const transaction = await database.sequelize.transaction()
        const _ = await super.atualizaRegistro({ ativo: true }, portadorId, transaction)
        console.log('_', _)
        if (_[0] === 0) throw new Error('Não foi possivel bloquear ao portador, tente novamente')
        const contas = await this.contas.pegaRegistros({ portador_id: portadorId }, transaction)
        if (contas.length === 0) return {message: 'O portador foi desbloqueado mas não tem contas'}
        await this.contas.atualizaRegistros({ status: 'ativa' }, { portador_id: portadorId },transaction)
        return { message: `Contas referenciadas no portador id:${portadorId} foram desbloqueadas` }
    }
}

module.exports = PortadoresServices