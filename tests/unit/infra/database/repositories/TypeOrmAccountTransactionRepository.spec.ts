import { Between, Repository } from "typeorm";
import AccountOrmEntity from "@infra/database/entities/AccountOrmEntity";
import HolderOrmEntity from "@infra/database/entities/HolderOrmEntity";
import { AccountStatus } from "@domain/entities/AccountEntity";
import { TypeOrmAccountTransactionRepository } from "@infra/database/repositories/TypeOrmAccountTransactionRepository";
import AccountTransactionOrmEntity from "@infra/database/entities/AccountTransactionOrmEntity";
import {
  AccountTransaction,
  TransactionType,
} from "@domain/entities/AccountTransactionEntity";
import { Money } from "@domain/valueObject/Money";

describe("TypeOrm Account Transaction Repository", () => {
  let accountTransactionRepository: TypeOrmAccountTransactionRepository;
  let mockAccountRepository: Repository<AccountOrmEntity>;
  let mockAccountTransactionRepository: Repository<AccountTransactionOrmEntity>;

  beforeAll(async () => {
    mockAccountRepository = {
      create: jest.fn(),
      save: jest.fn(),
      findOne: jest.fn(),
      delete: jest.fn(),
      find: jest.fn(),
      findOneByOrFail: jest.fn(),
    } as unknown as Repository<AccountOrmEntity>;
    mockAccountTransactionRepository = {
      createQueryBuilder: jest.fn(() => ({
        select: jest.fn().mockReturnThis(),
        where: jest.fn().mockReturnThis(),
        andWhere: jest.fn().mockReturnThis(),
        getRawOne: jest.fn().mockReturnThis(),
      })),
      create: jest.fn(),
      save: jest.fn(),
      findOne: jest.fn(),
      delete: jest.fn(),
      find: jest.fn(),
      findOneByOrFail: jest.fn(),
      findAndCount: jest.fn(),
    } as unknown as Repository<AccountTransactionOrmEntity>;

    accountTransactionRepository = new TypeOrmAccountTransactionRepository(
      mockAccountTransactionRepository,
      mockAccountRepository,
    );
  });

  afterAll(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(accountTransactionRepository).toBeDefined();
  });

  it("should create a transaction", async () => {
    const mockAccount = {
      id: 1,
      holder: {
        id: 2,
        cpf: "12345678901",
        fullName: "testing",
      } as unknown as HolderOrmEntity,
      balance: Money.zero().toString(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    } as AccountOrmEntity;
    const mockTransaction = {
      id: 1,
      amount: 100,
      type: TransactionType.DEPOSIT,
      accountId: mockAccount.id,
      createdAt: new Date(),
    } as unknown as AccountTransactionOrmEntity;
    const accountFindOneByOrFailSpy = jest
      .spyOn(mockAccountRepository, "findOneByOrFail")
      .mockResolvedValue(mockAccount);
    const createSpy = jest
      .spyOn(mockAccountTransactionRepository, "create")
      .mockReturnValue(mockTransaction);
    const saveSpy = jest.spyOn(mockAccountTransactionRepository, "save");

    const transaction = new AccountTransaction({
      amount: new Money(100),
      type: TransactionType.DEPOSIT,
      accountId: mockAccount.id,
    });
    await accountTransactionRepository.create(transaction);
    expect(accountFindOneByOrFailSpy).toHaveBeenCalledWith({ id: 1 });
    expect(createSpy).toHaveBeenCalledWith({
      account: mockAccount,
      amount: new Money(100).toString(),
      type: "DEPOSIT",
    });
    expect(saveSpy).toHaveBeenCalled();
  });
  it("should throw an error when account not found during transaction creation", async () => {
    const expectedError = new Error("Account not found");
    jest
      .spyOn(mockAccountRepository, "findOneByOrFail")
      .mockRejectedValue(expectedError);
    const transaction = new AccountTransaction({
      amount: new Money(100),
      type: TransactionType.DEPOSIT,
      accountId: 999,
    });
    await expect(
      accountTransactionRepository.create(transaction),
    ).rejects.toThrow(expectedError);
  });

  it("should return transactions by period", async () => {
    const mockAccount = {
      id: 1,
      holder: {
        id: 2,
        cpf: "12345678901",
        fullName: "Testing",
      } as unknown as HolderOrmEntity,
      balance: new Money(1000).toString(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    } as AccountOrmEntity;

    const mockTransactions: AccountTransactionOrmEntity[] = [
      {
        id: 1,
        amount: 100,
        type: TransactionType.DEPOSIT,
        account: mockAccount,
        createdAt: new Date("2023-10-15"),
      } as unknown as AccountTransactionOrmEntity,
      {
        id: 2,
        amount: 50,
        type: TransactionType.WITHDRAW,
        account: mockAccount,
        createdAt: new Date("2023-10-20"),
      } as unknown as AccountTransactionOrmEntity,
    ];

    const findSpy = jest
      .spyOn(mockAccountTransactionRepository, "findAndCount")
      .mockResolvedValue([mockTransactions, 2]);

    const { transactions, total } =
      await accountTransactionRepository.findByAccountAndPeriod(
        1,
        new Date("2023-10-01"),
        new Date("2023-10-31"),
        {
          size: 10,
          page: 1,
        },
      );
    expect(total).toEqual(2);
    expect(transactions).toHaveLength(2);
    expect(transactions[0]).toBeInstanceOf(AccountTransaction);
    expect(transactions[0].id).toBe(1);
    expect(transactions[1].id).toBe(2);
    expect(findSpy).toHaveBeenCalledWith({
      where: {
        account: { id: 1 },
        createdAt: Between(new Date("2023-10-01"), new Date("2023-10-31")),
      },
      relations: ["account"],
      take: 10,
      skip: (1 - 1) * 10,
      order: { createdAt: "DESC" },
    });
  });

  it("should return an empty array when no transactions found in the period", async () => {
    jest
      .spyOn(mockAccountTransactionRepository, "findAndCount")
      .mockResolvedValue([[], 0]);
    const start = new Date("2023-10-01");
    const end = new Date("2023-10-31");

    const { transactions, total } =
      await accountTransactionRepository.findByAccountAndPeriod(1, start, end, {
        page: 1,
        size: 10,
      });
    expect(transactions).toHaveLength(0);
    expect(total).toEqual(0);
  });

  it("should return 0 when no transactions found for the given type and period", async () => {
    jest
      .spyOn(mockAccountTransactionRepository, "createQueryBuilder")
      .mockReturnValue({
        select: jest.fn().mockReturnThis(),
        where: jest.fn().mockReturnThis(),
        andWhere: jest.fn().mockReturnThis(),
        getRawOne: jest.fn().mockResolvedValue({ total: 0 }),
      } as any);

    const start = new Date("2023-10-01");
    const end = new Date("2023-10-31");
    const total =
      await accountTransactionRepository.getTotalAmountByTypeAndPeriod(
        1,
        TransactionType.DEPOSIT,
        start,
        end,
      );

    expect(total.toNumber()).toBe(0);
    expect(
      mockAccountTransactionRepository.createQueryBuilder,
    ).toHaveBeenCalled();
  });

  it("should sum up amounts for transactions of the given type and period", async () => {
    jest
      .spyOn(mockAccountTransactionRepository, "createQueryBuilder")
      .mockReturnValue({
        select: jest.fn().mockReturnThis(),
        where: jest.fn().mockReturnThis(),
        andWhere: jest.fn().mockReturnThis(),
        getRawOne: jest.fn().mockResolvedValue({ total: 250 }),
      } as any);

    const start = new Date("2023-10-01");
    const end = new Date("2023-10-31");
    const total =
      await accountTransactionRepository.getTotalAmountByTypeAndPeriod(
        1,
        TransactionType.DEPOSIT,
        start,
        end,
      );

    expect(total.toNumber()).toBe(250);
    expect(
      mockAccountTransactionRepository.createQueryBuilder,
    ).toHaveBeenCalled();
  });
});
