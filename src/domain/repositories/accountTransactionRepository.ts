import {
  AccountTransaction,
  TransactionType,
} from "@domain/entities/AccountTransactionEntity";
import { Money } from "@domain/valueObject/Money";

export interface AccountTransactionRepository {
  create(transaction: AccountTransaction): Promise<void>;
  findByAccountAndPeriod(
    accountId: number,
    start: Date,
    end: Date,
    pagination: {
      page: number;
      size: number;
    },
  ): Promise<{ transactions: AccountTransaction[]; total: number }>;
  getTotalAmountByTypeAndPeriod(
    accountId: number,
    type: TransactionType,
    startDate: Date,
    endDate: Date,
  ): Promise<Money>;
}
