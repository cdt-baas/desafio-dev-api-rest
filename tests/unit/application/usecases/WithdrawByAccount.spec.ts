import { AccountRepository } from "@domain/repositories/accountRepository";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { Money } from "@domain/valueObject/Money";
import { WithdrawByAccount } from "@application/usecases/WithdrawByAccount";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { TransactionType } from "@domain/entities/AccountTransactionEntity";
import {
  AccountBlockedError,
  AccountNotFoundError,
  InsufficientBalanceError,
} from "@domain/errors";

describe("Withdraw Account", () => {
  let repository: AccountRepository;
  let accountTransactionRepository: AccountTransactionRepository;
  let usecase: WithdrawByAccount;

  beforeEach(() => {
    repository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
    accountTransactionRepository = {
      create: jest.fn(),
      findByAccountAndPeriod: jest.fn(),
      getTotalAmountByTypeAndPeriod: jest.fn(),
    } as AccountTransactionRepository;
    usecase = new WithdrawByAccount(repository, accountTransactionRepository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(usecase).toBeDefined();
    expect(repository).toBeDefined();
    expect(accountTransactionRepository).toBeDefined();
  });
  it("should Withdraw by account successfully", async () => {
    const mockAccount = new Account({
      id: 1,
      holderId: 1,
      balance: new Money(700),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    const withdrawSpy = jest.spyOn(mockAccount, "withdraw");
    const findByIdSpy = jest
      .spyOn(repository, "findById")
      .mockResolvedValue(mockAccount as any);
    const getTotalAmountByTypeAndPeriodSpy = jest
      .spyOn(accountTransactionRepository, "getTotalAmountByTypeAndPeriod")
      .mockResolvedValue(new Money(100));
    const saveSpy = jest.spyOn(repository, "save").mockResolvedValue(undefined);
    const createSpy = jest
      .spyOn(accountTransactionRepository, "create")
      .mockResolvedValue(undefined);
    await usecase.execute(1, 500);
    expect(withdrawSpy).toHaveBeenCalled();
    expect(findByIdSpy).toHaveBeenCalledWith(1);
    expect(getTotalAmountByTypeAndPeriodSpy).toHaveBeenCalledWith(
      1,
      TransactionType.WITHDRAW,
      expect.any(Date),
      expect.any(Date),
    );
    expect(saveSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        balance: new Money(200),
      }),
    );
    expect(createSpy).toHaveBeenCalledWith({
      accountId: 1,
      amount: new Money(500),
      type: TransactionType.WITHDRAW,
    });
  });

  it("should throw AccountNotFoundError if account is not found", async () => {
    jest.spyOn(repository, "findById").mockResolvedValue(null);
    await expect(usecase.execute(999, 100)).rejects.toThrow(
      AccountNotFoundError,
    );
  });

  it("should propagate an error when withdraw fails due to insufficient balance", async () => {
    const mockAccount = new Account({
      id: 2,
      holderId: 1,
      balance: new Money(100),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    jest.spyOn(repository, "findById").mockResolvedValue(mockAccount as any);
    jest
      .spyOn(accountTransactionRepository, "getTotalAmountByTypeAndPeriod")
      .mockResolvedValue(new Money(0));

    await expect(usecase.execute(2, 150)).rejects.toThrow(
      InsufficientBalanceError,
    );
  });

  it("should propagate an error when withdraw fails due to account being blocked", async () => {
    const mockAccount = new Account({
      id: 3,
      holderId: 1,
      balance: new Money(1000),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: true,
      createdAt: new Date(),
      deletedAt: null,
    });
    jest.spyOn(repository, "findById").mockResolvedValue(mockAccount as any);
    jest
      .spyOn(accountTransactionRepository, "getTotalAmountByTypeAndPeriod")
      .mockResolvedValue(new Money(50));

    await expect(usecase.execute(3, 200)).rejects.toThrow(AccountBlockedError);
  });
});
