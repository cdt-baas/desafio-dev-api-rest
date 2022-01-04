module.exports = (dbBlockAccount) => 
  async (httpRequest, httpResponse) => {
    try {
      const { accountId } = httpRequest.params
      const blocked = await dbBlockAccount.block(accountId)
      if(blocked) {
        return httpResponse.status(204).send()
      } else {
        return httpResponse.status(400).json({ error: 'An error occurred.' })
      }
    } catch (error) {
      return httpResponse.status(500).json(error.stack)
    }
  }