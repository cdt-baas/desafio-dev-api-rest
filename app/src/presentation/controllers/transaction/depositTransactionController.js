module.exports = (dbRepositTransaction) => 
  async (httpRequest, httpResponse) => {
    try {
      const { body } = httpRequest
      const deposit = await dbRepositTransaction.deposit(body)
      if (deposit) {
        return httpResponse.status(204).send()
      } else {
        return httpResponse.status(400).json({ message: 'An error occurred.'})
      }
    } catch (error) {
      return httpResponse.status(500).json(error.stack)
    }
  }