const PersonModel = require('../domain/models/person')

exports.PersonMongoRepository = () => ({
  async add(data) {
    const newPerson = await PersonModel.create(data)
    return newPerson
  }
})