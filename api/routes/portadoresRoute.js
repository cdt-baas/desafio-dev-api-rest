const { Router } = require('express')

const PortadorController = require('../controllers/PortadorController')

const router = Router()

router
    .get(   '/portadores', PortadorController.pegaPortadores)
    .post(  '/portadores', PortadorController.criaPortador)
    .get(   '/portadores/ativos', PortadorController.pegaPortadoresAtivos)
    .put(   '/portadores', PortadorController.atualizaPortador)
    .delete('/portadores', PortadorController.deletaPortador)
    .post(  '/portadores/restaura', PortadorController.restauraPortador)
    .post(  '/portadores/bloqueia', PortadorController.bloqueiaPortador) 
    .post(  '/portadores/desbloqueia', PortadorController.desbloqueiaPortador)
 
module.exports = router