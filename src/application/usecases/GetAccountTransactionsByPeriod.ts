import { AccountTransaction } from "@domain/entities/AccountTransactionEntity";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";

export interface GetAccountTransactionsByPeriodDto {
  accountId: number;
  startDate: Date;
  endDate: Date;
  pagination: {
    page: number;
    size: number;
  };
}

export interface GetAccountTransactionsByPeriodResponse {
  data: AccountTransaction[];
  pagination: {
    total: number;
    totalPages: number;
    currentPage: number;
    pageSize: number;
  };
}

export class GetAccountTransactionsByPeriod {
  constructor(
    private readonly accountTransactionRepository: AccountTransactionRepository,
  ) {}
  async execute(
    input: GetAccountTransactionsByPeriodDto,
  ): Promise<GetAccountTransactionsByPeriodResponse> {
    const { transactions, total } =
      await this.accountTransactionRepository.findByAccountAndPeriod(
        input.accountId,
        input.startDate,
        input.endDate,
        {
          page: input.pagination.page,
          size: input.pagination.size,
        },
      );
    const totalPages = Math.ceil(total / input.pagination.size);
    return {
      data: transactions,
      pagination: {
        total,
        totalPages,
        currentPage: input.pagination.page,
        pageSize: input.pagination.size,
      },
    };
  }
}
