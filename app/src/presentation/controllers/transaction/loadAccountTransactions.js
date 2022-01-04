module.exports = (dbLoadAccountTransactions) => 
  async (httpRequest, httpResponse) => {
    try {
      const { accountId } = httpRequest.params
      const transactions = await dbLoadAccountTransactions.loadTransactions(accountId)
      if (transactions) {
        return httpResponse.status(200).send(transactions)
      } else {
        return httpResponse.status(400).json({ message: 'An error occurred.'})
      }
    } catch (error) {
      return httpResponse.status(500).json(error.stack)
    }
  }