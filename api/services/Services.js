const database = require('../models')

/* 
Criar uma camada de serviços
Transferir a interface com a database do controlador para o serviço 
*/
class Services {
    constructor(nomeModelo) {
        this.nomeModelo = nomeModelo
    }

    pegaRegistro = (id, transacao = {}) => database[this.nomeModelo].findOne({ where: { id } }, transacao)

    pegaRegistros = (where, transacao = {}) => database[this.nomeModelo].findAll({ where: { ...where } }, transacao)

    criaRegistro(registro, transacao = {}) {
        return database[this.nomeModelo].create(registro, transacao)
    }

    deletaRegistro = id => database[this.nomeModelo].destroy({ where: { id } })
        .then(_ => this.pegaRegistro(id))
        .then(registro => ({
            message: registro === null
                ? `Registro ${id} de ${this.nomeModelo} deletado com sucesso`
                : `Registro ${id} de ${this.nomeModelo} com erro ao deletar`
        }))

    deletaRegistros = (where, transacao = {}) => database[this.nomeModelo].destroy({ where: { ...where } })
        .then(_ => {
            if (_ === 1) return {message: `Registros de ${this.nomeModelo} deletados`}
        })

    restauraRegistro = id => database[this.nomeModelo].restore({ where: { id } })
        .then(_ => ({ menssage: `Registro ${id} de ${this.nomeModelo} restaurado com sucesso` }))

    restauraRegistros = where => database[this.nomeModelo].restore({ where: { ...where } })
        .then(_ => ({ menssage: `Registros de ${this.nomeModelo} restaurados` }))

    atualizaRegistro(dadosAtualizados, id, transacao = {}) {
        return database[this.nomeModelo].update(dadosAtualizados, { where: { id: id } }, transacao)
    }

    atualizaRegistros(dadosAtualizados, where, transacao = {}) {
        return database[this.nomeModelo].update(dadosAtualizados, { where: { ...where } }, transacao)
    }

    pegaEConta = (where = {}, agregadores) => database[this.nomeModelo].findAndCountAll({
        where: {...where}, ...agregadores
    })
}

module.exports = Services