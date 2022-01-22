
const { TransacoesServices } = require('../services')
const transacoesServices = new TransacoesServices()

const Sequelize = require('sequelize')
const Op = Sequelize.Op
class TransacaoController {

    static async pegaTransacoes(req, res) {
        try {
            const { data_inicial, data_final } = req.query
            const where = {}

            if (!req.query.conta_id) throw new Error('O número de conta é obrigatorio')
            where.conta_id = req.query.conta_id

            if (data_inicial && data_final) {            
                where.createdAt = {}
                where.createdAt[Op.gte] = data_inicial ? data_inicial : null
                where.createdAt[Op.lte] = data_final ? data_final : null
            } else {
                if (data_inicial && !data_final) throw new Error('Debe incluir a data final')
                if (!data_inicial && data_final) throw new Error('Debe incluir a data inicial')
            }

            const transacoes = await transacoesServices.pegaRegistros(where)
            return res.status(200).json(transacoes)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static criaDebito = async (req, res) => {
        try {
            if (!req.body.conta_id) throw new Error('O número de conta é obrigatorio')
            if (!req.body.valor) throw new Error('O valor é obrigatorio')

            const transacao = await transacoesServices.criaDebito({ ...req.body, tipo: 'debito', liquidado: false })
            return res.status(201).json(transacao)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static criaCredito = async (req, res) => {
        try {
            if (!req.body.conta_id) throw new Error('O número de conta é obrigatorio')
            if (!req.body.valor) throw new Error('O valor é obrigatorio')

            const transacao = await transacoesServices.criaCredito({ ...req.body, tipo: 'credito', liquidado: false })
            return res.status(201).json(transacao)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }
}

module.exports = TransacaoController