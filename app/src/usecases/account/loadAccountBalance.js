exports.DbLoadAccountBalance = (accountRepository) => ({
  async loadBalance(accountId) {
    const account = await accountRepository.loadById(accountId)
    return account ? account.saldo : false
  }
})
