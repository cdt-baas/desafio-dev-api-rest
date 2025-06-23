import { Account } from "@domain/entities/AccountEntity";
import { Holder } from "@domain/entities/HolderEntity";

export interface AccountRepository {
  create(account: Account, holder: Holder): Promise<void>;
  findByAgencyAndNumber(
    agency: string,
    number: string,
  ): Promise<Account | null>;
  save(account: Account): Promise<void>;
  findById(id: number): Promise<Account | null>;
}
