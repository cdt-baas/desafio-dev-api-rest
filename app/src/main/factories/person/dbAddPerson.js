const AddPersonController = require('../../../presentation/controllers/person/addPersonController')
const { DbAddPerson } = require('../../../usecases/person/addPerson')
const { PersonMongoRepository } = require('../../../repositories/personMongoRepository')

exports.makeAddPersonController = () => {
  const dbAddPerson = DbAddPerson(PersonMongoRepository())
  const controller = AddPersonController(dbAddPerson)
  return controller
}
