exports.DbBlockAccount = (accountRepository) => ({
  async block(accountId) {
    const account = await accountRepository.loadById(accountId)
    if (account && account.flagAtivo) {
      account.flagAtivo = false
      await accountRepository.updateById(account._id, account)
    }
    return true
  }
})
