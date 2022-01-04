const AddAccountController = require('../../../presentation/controllers/account/addAccountController')
const { DbAddAccount } = require('../../../usecases/account/addAccount')
const { AccountMongoRepository } = require('../../../repositories/accountMongoRepository')

exports.makeAddAccountController = () => {
  const dbAddAccount = DbAddAccount(AccountMongoRepository())
  const controller = AddAccountController(dbAddAccount)
  return controller
}
