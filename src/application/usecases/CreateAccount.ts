import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { HolderNotFoundError } from "@domain/errors";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { HolderRepository } from "@domain/repositories/holderRepository";
import { Money } from "@domain/valueObject/Money";

export interface CreateAccountDto {
  cpf: string;
  balance: number;
  agency: string;
  number: string;
}

export class CreateAccount {
  constructor(
    private readonly accountRepository: AccountRepository,
    private readonly holderRepository: HolderRepository,
  ) {}

  async execute(input: CreateAccountDto): Promise<void> {
    const holder = await this.holderRepository.findByCpf(input.cpf);
    if (!holder) {
      throw new HolderNotFoundError();
    }
    const { balance, agency, number } = input;
    const account = new Account({
      holderId: holder.id!,
      balance: new Money(balance),
      agency,
      number,
      status: AccountStatus.ACTIVE,
      isBlocked: false,
    });
    await this.accountRepository.create(account, holder);
  }
}
