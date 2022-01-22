const Services = require('./Services')
const database = require('../models')

const Sequelize = require('sequelize')
const Op = Sequelize.Op

class ContasServices extends Services {
    constructor() {
        super('Contas')
        this.transacoes = new Services('Transacoes')
        this.portadores = new Services('Portadores')
        this.limiteSaque = 2000
    }

    criaConta = async (registro) => {
        const portador = await this.portadores.pegaRegistros({cpf: registro.cpf})
        if (portador.length === 0) return { message: 'Não foi achado um CPF para apertura de conta ou o portador está inativo'}
        delete registro.cpf
        return this.criaRegistro({...registro, portador_id: portador[0].id})
    } 
        

    atualizaConta = (registroAtualizado, portadorId, contaId) =>
        database.sequelize.transaction(
            transaction => this.atualizaRegistros(registroAtualizado, { portador_id: portadorId, id: contaId }, transaction)
                .then(_ => this.pegaRegistros({ portador_id: portadorId, id: contaId }, transaction))
        )

    apagaConta = (portadorId, contaId) => this.deletaRegistros({ portador_id: portadorId, id: contaId })

    restauraConta = (portadorId, contaId) => this.restauraRegistros({ portador_id: portadorId, id: contaId })

    pegaTransacoesDeConta = (contaId) =>
        this.pegaRegistro(contaId)
            .then(_ => this.transacoes.pegaRegistros({ conta_id: contaId }))

    async atualizaContaPorDebito(transacaoCriada, contaId, transaction = {}) {
        const conta = await this.pegaRegistro(contaId, transaction)
        if (conta.status === 'bloqueada') throw new Error('Esta conta está bloqueada, não permite fazer o saque')  
        const result = conta.saldo - transacaoCriada.valor
        if (result < 0) return { message: 'A conta não pode ficar com saldo negativo' }
        
        const iniDia = new Date()
        iniDia.setHours(0,0,0)
        const finDia = new Date()
        finDia.setHours(23,59,59)

        const where = {
            conta_id: contaId,
            createdAt: {
                [Op.gte]: iniDia.setHours(0,0,0),
                [Op.lte]: finDia.setHours(23,59,59)
            },
            liquidado: true,
            tipo: 'debito'
        }
        const transacoes = await this.transacoes.pegaRegistros(where, transaction)
        const current = transacoes.map(tran => tran.valor ).reduce((a,b) => (a + b), 0)
        const pretendido = Number(current) + Number(transacaoCriada.valor)
        const available = this.limiteSaque - current

        if (pretendido > this.limiteSaque ) return { message: `Hoje foi retirado ${current} reais, não é possivel retirar mais de ${this.limiteSaque}. Pode retirar só ${available} reais` }
        const contaAtualizada = await this.atualizaRegistro({ saldo: result }, conta.id, transaction)
        await this.transacoes.atualizaRegistro({ liquidado: true }, transacaoCriada.id, transaction)
        return contaAtualizada
    }

    async atualizaContaPorCredito(transacaoCriada, contaId, transaction = {}) {
        const conta = await this.pegaRegistro(contaId, transaction)
        const result = Number(conta.saldo) + Number(transacaoCriada.valor)
        const contaAtualizada = await this.atualizaRegistro({ saldo: result }, conta.id, transaction)
        await this.transacoes.atualizaRegistro({ liquidado: true }, transacaoCriada.id, transaction)
        return contaAtualizada
    }
}

module.exports = ContasServices