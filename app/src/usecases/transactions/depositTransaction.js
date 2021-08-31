exports.DbDepositTransaction = (transactionRepository, accountRepository) => ({
  async deposit({ accountId, value }) {
    const account = await accountRepository.loadById(accountId)
    if (!account) {
      return false
    }
    account.saldo += value
    const updatedAccount = await accountRepository.updateById(account._id, account)
    const newTransaction = {
      idConta: updatedAccount._id,
      valor: value,
      dataTransacao: new Date()
    }
    await transactionRepository.add(newTransaction)
    return true
  }
})
