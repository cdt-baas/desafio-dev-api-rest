import { AccountNotFoundError } from "@domain/errors";
import { AccountRepository } from "@domain/repositories/accountRepository";

export class CloseAccount {
  constructor(private accountRepository: AccountRepository) {}

  async execute(id: number): Promise<void> {
    const account = await this.accountRepository.findById(id);

    if (!account) {
      throw new AccountNotFoundError();
    }
    account.close();
    await this.accountRepository.save(account);
  }
}
