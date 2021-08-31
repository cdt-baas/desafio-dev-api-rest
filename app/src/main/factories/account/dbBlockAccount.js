const BlockAccountController = require('../../../presentation/controllers/account/blockAccountController')
const { DbBlockAccount } = require('../../../usecases/account/blockAccount')
const { AccountMongoRepository } = require('../../../repositories/accountMongoRepository')

exports.makeBlockAccountController = () => {
  const dbBlockAccount = DbBlockAccount(AccountMongoRepository())
  const controller = BlockAccountController(dbBlockAccount)
  return controller
}
