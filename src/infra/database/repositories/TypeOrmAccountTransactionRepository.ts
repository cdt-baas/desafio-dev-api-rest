import { Repository, Between } from "typeorm";
import AccountTransactionOrmEntity from "../entities/AccountTransactionOrmEntity";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import {
  AccountTransaction,
  TransactionType,
} from "@domain/entities/AccountTransactionEntity";
import AccountOrmEntity from "../entities/AccountOrmEntity";
import { Money } from "@domain/valueObject/Money";

export class TypeOrmAccountTransactionRepository
  implements AccountTransactionRepository
{
  constructor(
    private readonly repository: Repository<AccountTransactionOrmEntity>,
    private readonly accountRepository: Repository<AccountOrmEntity>,
  ) {}

  async create(transaction: AccountTransaction): Promise<void> {
    const account = await this.accountRepository.findOneByOrFail({
      id: transaction.accountId,
    });
    const entity = this.repository.create({
      account,
      type: transaction.type,
      amount: transaction.amount.toString(),
    });
    await this.repository.save(entity);
  }
  async findByAccountAndPeriod(
    accountId: number,
    start: Date,
    end: Date,
    pagination: {
      page: number;
      size: number;
    },
  ): Promise<{ transactions: AccountTransaction[]; total: number }> {
    const [transactions, total] = await this.repository.findAndCount({
      where: {
        account: { id: accountId },
        createdAt: Between(start, end),
      },
      relations: ["account"],
      take: pagination.size,
      skip: (pagination.page - 1) * pagination.size,
      order: { createdAt: "DESC" },
    });
    return {
      transactions: transactions.map((transaction) =>
        this.toDomain(transaction),
      ),
      total,
    };
  }

  async getTotalAmountByTypeAndPeriod(
    accountId: number,
    type: TransactionType,
    startDate: Date,
    endDate: Date,
  ): Promise<Money> {
    const result = await this.repository
      .createQueryBuilder("transaction")
      .select("SUM(transaction.amount)", "total")
      .where("transaction.accountId = :accountId", { accountId })
      .andWhere("transaction.type = :type", { type })
      .andWhere("transaction.createdAt BETWEEN :start AND :end", {
        start: startDate.toISOString(),
        end: endDate.toISOString(),
      })
      .getRawOne();
    const total = result.total ? parseFloat(result.total) : 0;
    return new Money(total);
  }

  private toDomain(entity: AccountTransactionOrmEntity): AccountTransaction {
    return new AccountTransaction({
      id: entity.id,
      accountId: entity.account.id,
      type: entity.type,
      amount: new Money(entity.amount),
      createdAt: entity.createdAt,
    });
  }
}
