const LoadAccountTransactions = require('../../../presentation/controllers/transaction/loadAccountTransactions')
const { DbLoadAccountTransactions } = require('../../../usecases/transactions/loadAccountTransactions')
const { TransactionMongoRepository } = require('../../../repositories/transactionMongoRepository')

exports.makeLoadAccountTransactions = () => {
  const dbLoadAccountTransactions = DbLoadAccountTransactions(TransactionMongoRepository())
  const controller = LoadAccountTransactions(dbLoadAccountTransactions)
  return controller
}
