module.exports = (dbAddPerson) => 
  async (httpRequest, httpResponse) => {
    try {
      const { body } = httpRequest
      const newPerson = await dbAddPerson.add(body)
      console.log(newPerson);
      return httpResponse.status(200).json(newPerson)
    } catch (error) {
      return httpResponse.status(500).json(error)
    }
  }