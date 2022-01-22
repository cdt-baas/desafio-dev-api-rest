const { ContasServices } = require('../services')
const contaServices = new ContasServices()

class ContaController {

    static pegaContas = async (req, res) => {
        try {
            const where = {}
            if (req.query.agencia) where.agencia = req.query.agencia
            if (req.query.portador_id) where.portador_id = req.query.portador_id
            const contas  = await contaServices.pegaRegistros(where)
            return res.status(200).json(contas)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static criaConta = async (req, res) => {
        try {
            if (!req.body.cpf) throw new Error('CPF cadastrado do portador é obrigatorio')
            const conta = await contaServices.criaConta({ ...req.body, saldo: 0, status: 'ativa'})    
            return res.status(201).json(conta)
        } catch (error) {
            return res.status(500).json(error.message)       
        }
    }

    static atualizaConta = async (req, res) => {
        try {
            if (!req.query.conta_id) throw new Error('O campo "conta_id" é obrigatorio')
            if (!req.query.portador_id) throw new Error('O campo "portador_id" é obrigatorio')
            const conta = await contaServices.atualizaConta(req.body, Number(req.query.portador_id), Number(req.query.conta_id))
            return res.status(201).json(conta)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static apagaConta = async (req, res) => {
        try {
            if (!req.query.conta_id) throw new Error('O campo "conta_id" é obrigatorio')
            if (!req.query.portador_id) throw new Error('O campo "portador_id" é obrigatorio')
            const msg = await contaServices.apagaConta(Number(req.query.portador_id), Number(req.query.conta_id))
            return res.status(201).json(msg)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static restauraConta = async (req, res) => {
        try {
            if (!req.query.conta_id) throw new Error('O campo "conta_id" é obrigatorio')
            if (!req.query.portador_id) throw new Error('O campo "portador_id" é obrigatorio')
            const msg = await contaServices.restauraConta(Number(req.query.portador_id), Number(req.query.conta_id))
            return res.status(201).json(msg)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }
}

module.exports = ContaController