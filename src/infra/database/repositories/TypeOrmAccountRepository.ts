import { IsNull, Repository } from "typeorm";
import AccountOrmEntity from "@infra/database/entities/AccountOrmEntity";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { Account } from "@domain/entities/AccountEntity";
import { Holder } from "@domain/entities/HolderEntity";
import { Money } from "@domain/valueObject/Money";

export class TypeOrmAccountRepository implements AccountRepository {
  constructor(private readonly repository: Repository<AccountOrmEntity>) {}

  async create(account: Account, holder: Holder): Promise<void> {
    const accountEntity = this.repository.create({
      holder,
      balance: account.balance.toString(),
      agency: account.agency,
      number: account.number,
      status: account.status,
      isBlocked: account.isBlocked,
    });
    await this.repository.save(accountEntity);
  }
  async findByAgencyAndNumber(
    agency: string,
    number: string,
  ): Promise<Account | null> {
    const accountEntity = await this.repository.findOne({
      where: { agency, number, deletedAt: IsNull() },
      relations: ["holder"],
    });
    return accountEntity ? this.toDomain(accountEntity) : null;
  }

  async save(account: Account): Promise<void> {
    const existingEntity = await this.repository.findOneByOrFail({
      id: account.id,
    });
    existingEntity.status = account.status;
    existingEntity.balance = account.balance.toString();
    existingEntity.isBlocked = account.isBlocked;
    existingEntity.deletedAt = account.deletedAt || null;

    await this.repository.save(existingEntity);
  }

  async findById(id: number): Promise<Account | null> {
    const accountEntity = await this.repository.findOne({
      where: { id, deletedAt: IsNull() },
      relations: ["holder"],
    });
    return accountEntity ? this.toDomain(accountEntity) : null;
  }

  private toDomain(entity: AccountOrmEntity): Account {
    return new Account({
      id: entity.id,
      holderId: entity.holder.id,
      balance: new Money(entity.balance),
      agency: entity.agency,
      number: entity.number,
      createdAt: entity.createdAt,
      deletedAt: entity.deletedAt,
      status: entity.status,
      isBlocked: entity.isBlocked,
    });
  }
}
