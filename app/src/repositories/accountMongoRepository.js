const AccountModel = require('../domain/models/account')

exports.AccountMongoRepository = () => ({
  async add(data) {
    const newAccount = await AccountModel.create(data)
    return newAccount
  },
  async updateById(id, data) {
    const updatedAccount = await AccountModel.findByIdAndUpdate(id, data, { new: true })
    return updatedAccount
  },
  async loadById(id) {
    const account = await AccountModel.findById(id)
    return account
  }
})