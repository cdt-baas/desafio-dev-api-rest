module.exports = (dbAddAccount) => 
  async (httpRequest, httpResponse) => {
    try {
      const { body } = httpRequest
      const newAccount = await dbAddAccount.add(body)
      return httpResponse.status(200).json(newAccount)
    } catch (error) {
      return httpResponse.status(500).json(error)
    }
  }