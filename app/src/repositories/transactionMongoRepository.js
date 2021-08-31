const TransactionModel = require('../domain/models/transaction')

exports.TransactionMongoRepository = () => ({
  async add(data) {
    const newTransaction = await TransactionModel.create(data)
    return newTransaction
  },
  async load(query = {}) {
    const transactions = await TransactionModel.find(query)
    return transactions
  }
})