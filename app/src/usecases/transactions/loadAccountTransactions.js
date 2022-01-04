exports.DbLoadAccountTransactions = (transactionRepository) => ({
  async loadTransactions(accountId) {
    const transactions = await transactionRepository.load({ idConta: accountId })
    return transactions
  }
})
