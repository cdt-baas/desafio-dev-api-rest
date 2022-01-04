module.exports = (dbLoadAccountBalance) => 
  async (httpRequest, httpResponse) => {
    try {
      const { accountId } = httpRequest.params
      const accountBalance = await dbLoadAccountBalance.loadBalance(accountId)
      return httpResponse.status(200).json({ balance: accountBalance })
    } catch (error) {
      return httpResponse.status(500).json(error.stack)
    }
  }