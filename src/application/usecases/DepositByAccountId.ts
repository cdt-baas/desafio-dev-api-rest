import {
  AccountTransaction,
  TransactionType,
} from "@domain/entities/AccountTransactionEntity";
import { AccountNotFoundError } from "@domain/errors";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { Money } from "@domain/valueObject/Money";

export class DepositByAccountId {
  constructor(
    private accountRepository: AccountRepository,
    private accountTransactionRepository: AccountTransactionRepository,
  ) {}

  async execute(id: number, amount: number): Promise<void> {
    const money = new Money(amount);
    const account = await this.accountRepository.findById(id);
    if (!account) {
      throw new AccountNotFoundError();
    }
    account.deposit(money);
    await this.accountRepository.save(account);
    const transaction = new AccountTransaction({
      accountId: account.id!,
      amount: money,
      type: TransactionType.DEPOSIT,
    });
    await this.accountTransactionRepository.create(transaction);
  }
}
