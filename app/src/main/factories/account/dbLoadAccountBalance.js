const LoadAccountBalance = require('../../../presentation/controllers/account/loadAccountBalanceController')
const { DbLoadAccountBalance } = require('../../../usecases/account/loadAccountBalance')
const { AccountMongoRepository } = require('../../../repositories/accountMongoRepository')

exports.makeLoadAccountBalance = () => {
  const dbLoadAccountBalance = DbLoadAccountBalance(AccountMongoRepository())
  const controller = LoadAccountBalance(dbLoadAccountBalance)
  return controller
}
