const { PortadoresServices } = require('../services')
const portadoresServices = new PortadoresServices()

class PortadorController {
    static pegaPortadoresAtivos = (req, res) =>
        portadoresServices.pegaRegistrosAtivos()
            .then(portadoresAtivos => res.status(200).json(portadoresAtivos))
            .catch(error => res.status(500).json(error.message))

    static pegaPortadores = async (req, res) => {
        try {
            let where = {}
            if (req.query.id) where.id = Number(req.query.id)
            let registros = await portadoresServices.pegaRegistros(where)
            if (registros.length === 0) throw new Error('O portador não existe')
            return res.status(200).json(registros)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }
        
    static pegaPortador = (req, res) =>
        portadoresServices.pegaRegistro(Number(req.params.portadorId))
            .then(umaPessoa => res.status(200).json(umaPessoa))
            .catch(error => res.status(500).json(error.message))

    static criaPortador = async (req, res) => {
        try {
            if (!req.body.nome) throw new Error('O nome é obrigatorio')
            if (!req.body.cpf) throw new Error('O cpf é obrigatorio')
            const portadorCriado = await portadoresServices.criaRegistro({...req.body, ativo: true})
            return res.status(201).json(portadorCriado)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static atualizaPortador = async (req, res) => {
        try {
            if (!req.query.id) throw new Error('O campo "id" é obrigatorio')
            if (req.body.ativo) throw new Error('O status só pode ser modificado pela rota /portadores/bloqueia')
            const portadorAtualizado = await portadoresServices.atualizaRegistro(req.body, Number(req.query.id))
            return res.status(200).json(portadorAtualizado)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static deletaPortador = async (req, res) => {
        try {
            if (!req.query.id) throw new Error('O campo "id" é obrigatorio')
            const msg = await portadoresServices.deletaRegistro(Number(req.query.id))
            return res.status(200).json(msg)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static restauraPortador = async (req, res) => {
        try {
            if (!req.query.id) throw new Error('O campo "id" é obrigatorio')
            const msg = await portadoresServices.restauraRegistro(Number(req.query.id))
            return res.status(200).json(msg)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static bloqueiaPortador = async (req, res) => {
        try {
            if (!req.query.id) throw new Error('O campo "id" é obrigatorio')
            const msg = await portadoresServices.bloqueiaPortadorEConta(Number(req.query.id))
            return res.status(200).json(msg.message)    
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

    static desbloqueiaPortador = async (req, res) => {
        try {
            if (!req.query.id) throw new Error('O campo "id" é obrigatorio')
            const msg = await portadoresServices.desbloqueiaPortadorEConta(Number(req.query.id))
            return res.status(200).json(msg.message)
        } catch (error) {
            return res.status(500).json(error.message)
        }
    }

}

module.exports = PortadorController