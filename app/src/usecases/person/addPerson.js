exports.DbAddPerson = (personRepository) => ({
  async add(personData) {
    const newPerson = await personRepository.add(personData)
    return newPerson
  }
})
