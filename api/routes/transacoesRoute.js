const { Router } = require('express')
const TransacoesController = require('../controllers/TransacoesController')

const router = Router()
router

    .get(   '/transacoes', TransacoesController.pegaTransacoes)
    .post(  '/transacoes/debito', TransacoesController.criaDebito)
    .post(  '/transacoes/credito', TransacoesController.criaCredito)

module.exports = router