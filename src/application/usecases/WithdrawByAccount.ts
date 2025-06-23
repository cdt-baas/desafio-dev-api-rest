import {
  AccountTransaction,
  TransactionType,
} from "@domain/entities/AccountTransactionEntity";
import { AccountNotFoundError } from "@domain/errors";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { Money } from "@domain/valueObject/Money";

export class WithdrawByAccount {
  constructor(
    private accountRepository: AccountRepository,
    private accountTransactionRepository: AccountTransactionRepository,
  ) {}

  async execute(id: number, amount: number): Promise<void> {
    const account = await this.accountRepository.findById(id);
    if (!account) {
      throw new AccountNotFoundError();
    }
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    const totalWithdrawnToday =
      await this.accountTransactionRepository.getTotalAmountByTypeAndPeriod(
        id,
        TransactionType.WITHDRAW,
        today,
        tomorrow,
      );
    const money = new Money(amount);
    account.withdraw(money, totalWithdrawnToday);
    await this.accountRepository.save(account);
    const transaction = new AccountTransaction({
      accountId: account.id!,
      amount: money,
      type: TransactionType.WITHDRAW,
    });
    await this.accountTransactionRepository.create(transaction);
  }
}
