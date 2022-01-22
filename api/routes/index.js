const portadores = require('./portadoresRoute')
const transacoes = require('./transacoesRoute')
const contas = require('./contasRoute')

module.exports = app => {
    app.use(
        portadores,
        transacoes,
        contas
    )
}