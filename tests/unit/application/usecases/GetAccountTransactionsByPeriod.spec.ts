import { GetAccountTransactionsByPeriod } from "@application/usecases/GetAccountTransactionsByPeriod";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { AccountTransaction } from "@domain/entities/AccountTransactionEntity";
import { Money } from "@domain/valueObject/Money";

describe("Get Account Transactions By Period", () => {
  let accountTransactionRepository: AccountTransactionRepository;
  let usecase: GetAccountTransactionsByPeriod;

  const accountId = 1;
  const startDate = new Date("2023-10-01");
  const endDate = new Date("2023-10-31");

  beforeEach(() => {
    accountTransactionRepository = {
      findByAccountAndPeriod: jest.fn(),
    } as unknown as AccountTransactionRepository;

    usecase = new GetAccountTransactionsByPeriod(accountTransactionRepository);
  });

  test("should return transactions with correct pagination data when transactions exist", async () => {
    const transactions: AccountTransaction[] = [
      {
        id: 1,
        accountId,
        type: "DEPOSIT",
        amount: new Money(500.0),
        createdAt: new Date("2023-10-15T10:00:00Z"),
      } as AccountTransaction,
      {
        id: 2,
        accountId,
        type: "WITHDRAW",
        amount: new Money(200.0),
        createdAt: new Date("2023-10-20T12:00:00Z"),
      } as AccountTransaction,
    ];
    const total = transactions.length;
    const page = 1;
    const size = 10;

    const findByAccountAndPeriodSpy = jest
      .spyOn(accountTransactionRepository, "findByAccountAndPeriod")
      .mockResolvedValue({
        transactions,
        total,
      });
    const result = await usecase.execute({
      accountId,
      startDate,
      endDate,
      pagination: {
        page,
        size,
      },
    });

    expect(result.data).toEqual(transactions);
    expect(result.pagination.total).toBe(total);
    expect(result.pagination.currentPage).toBe(page);
    expect(result.pagination.pageSize).toBe(size);
    expect(result.pagination.totalPages).toBe(Math.ceil(total / size));
    expect(findByAccountAndPeriodSpy).toHaveBeenCalledWith(
      1,
      expect.any(Date),
      expect.any(Date),
      { page: 1, size: 10 },
    );
  });

  test("should return empty transactions list with correct pagination when no transactions occur", async () => {
    const transactions: AccountTransaction[] = [];
    const total = 0;
    const page = 2;
    const size = 5;

    const findByAccountAndPeriodSpy = jest
      .spyOn(accountTransactionRepository, "findByAccountAndPeriod")
      .mockResolvedValue({
        transactions,
        total,
      });

    const result = await usecase.execute({
      accountId,
      startDate,
      endDate,
      pagination: {
        page,
        size,
      },
    });

    expect(result.data).toEqual([]);
    expect(result.pagination.total).toBe(0);
    expect(result.pagination.currentPage).toBe(page);
    expect(result.pagination.pageSize).toBe(size);
    expect(result.pagination.totalPages).toBe(0);
    expect(findByAccountAndPeriodSpy).toHaveBeenCalled();
  });

  test("should correctly calculate totalPages when total is not a multiple of page size", async () => {
    const transactions: AccountTransaction[] = [
      {
        id: 1,
        accountId,
        type: "DEPOSIT",
        amount: new Money(300.0),
        createdAt: new Date("2023-10-05T09:00:00Z"),
      } as AccountTransaction,
      {
        id: 2,
        accountId,
        type: "WITHDRAW",
        amount: new Money(100.0),
        createdAt: new Date("2023-10-10T15:00:00Z"),
      } as AccountTransaction,
      {
        id: 3,
        accountId,
        type: "DEPOSIT",
        amount: new Money(400.0),
        createdAt: new Date("2023-10-15T11:00:00Z"),
      } as AccountTransaction,
    ];
    const total = 5;
    const page = 1;
    const size = 2;

    const findByAccountAndPeriodSpy = jest
      .spyOn(accountTransactionRepository, "findByAccountAndPeriod")
      .mockResolvedValue({
        transactions,
        total,
      });

    const result = await usecase.execute({
      accountId,
      startDate,
      endDate,
      pagination: {
        page,
        size,
      },
    });
    expect(result.pagination.totalPages).toBe(3);
    expect(findByAccountAndPeriodSpy).toHaveBeenCalled();
  });
});
