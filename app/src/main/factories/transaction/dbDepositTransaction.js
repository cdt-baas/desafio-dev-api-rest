const DepositTransactionController = require('../../../presentation/controllers/transaction/depositTransactionController')
const { DbDepositTransaction } = require('../../../usecases/transactions/depositTransaction')
const { AccountMongoRepository } = require('../../../repositories/accountMongoRepository')
const { TransactionMongoRepository } = require('../../../repositories/transactionMongoRepository')

exports.makeDepositTransactionController = () => {
  const dbDepositTransaction = DbDepositTransaction(TransactionMongoRepository(), AccountMongoRepository())
  const controller = DepositTransactionController(dbDepositTransaction)
  return controller
}
