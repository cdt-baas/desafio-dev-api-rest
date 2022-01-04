const router = require('express').Router() 
const { makeAddAccountController } = require('./factories/account/dbAddAccount')
const { makeBlockAccountController } = require('./factories/account/dbBlockAccount')
const { makeLoadAccountBalance } = require('./factories/account/dbLoadAccountBalance')
const { makeAddPersonController } = require('./factories/person/dbAddPerson')
const { makeDepositTransactionController } = require('./factories/transaction/dbDepositTransaction')
const { makeLoadAccountTransactions } = require('./factories/transaction/dbLoadAccountTransactions')

router
  .route('/account')
  .post(makeAddAccountController())

router
  .route('/account/:accountId')
  .post(makeBlockAccountController())

router
  .route('/account/:accountId/balance')
  .get(makeLoadAccountBalance())

router
  .route('/account/:accountId/transactions')
  .get(makeLoadAccountTransactions())

router
  .route('/transaction/deposit')
  .post(makeDepositTransactionController())

router
  .route('/person')
  .post(makeAddPersonController())

module.exports = router