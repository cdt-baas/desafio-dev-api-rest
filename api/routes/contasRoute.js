const { Router } = require('express')

const PortadorController = require('../controllers/PortadorController')
const ContaController = require('../controllers/ContaController')

const router = Router()

router
    .post(  '/contas', ContaController.criaConta)
    .get(   '/contas', ContaController.pegaContas)
    .put(   '/contas', ContaController.atualizaConta)
    .delete('/contas', ContaController.apagaConta)
    .post(  '/contas/restaura', ContaController.restauraConta)
 
module.exports = router