exports.DbAddAccount = (addAccountRepository) => ({
  async add(accountData) {
    accountData.dataCriacao = new Date()
    const newAccount = await addAccountRepository.add(accountData)
    return newAccount
  }
})
