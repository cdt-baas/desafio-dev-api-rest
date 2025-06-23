import { AccountNotFoundError } from "@domain/errors";
import { AccountRepository } from "@domain/repositories/accountRepository";

export interface GetAccountByAgencyAndNumberDto {
  agency: string;
  number: string;
}

export interface GetAccountByAgencyAndNumberResponse {
  id: number;
  balance: string;
  agency: string;
  number: string;
}

export class GetAccountByAgencyAndNumber {
  constructor(private readonly accountRepository: AccountRepository) {}

  async execute({
    agency,
    number,
  }: GetAccountByAgencyAndNumberDto): Promise<GetAccountByAgencyAndNumberResponse> {
    const account = await this.accountRepository.findByAgencyAndNumber(
      agency,
      number,
    );
    if (!account) {
      throw new AccountNotFoundError();
    }
    return {
      id: account.id!,
      balance: account.balance.toString(),
      agency: account.agency,
      number: account.number,
    };
  }
}
