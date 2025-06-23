import { Holder } from "@domain/entities/HolderEntity";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { AccountNotFoundError } from "@domain/errors";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { DepositByAccountId } from "@application/usecases/DepositByAccountId";
import { Money } from "@domain/valueObject/Money";
import { TransactionType } from "@domain/entities/AccountTransactionEntity";

describe("Deposit By AccountId", () => {
  let accountTransactionRepository: AccountTransactionRepository;
  let accountRepository: AccountRepository;
  let usecase: DepositByAccountId;

  beforeEach(() => {
    accountTransactionRepository = {
      create: jest.fn(),
      findByAccountAndPeriod: jest.fn(),
      getTotalAmountByTypeAndPeriod: jest.fn(),
    } as AccountTransactionRepository;

    accountRepository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
    usecase = new DepositByAccountId(
      accountRepository,
      accountTransactionRepository,
    );
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(accountRepository).toBeDefined();
    expect(accountTransactionRepository).toBeDefined();
  });

  it("should create deposit by account successfully", async () => {
    const mockHolder = new Holder({
      id: 1,
      fullName: "Test testing",
      cpf: "12345678911",
    });
    const mockAccount = new Account({
      id: 1,
      holderId: mockHolder.id!,
      balance: new Money(1000),
      agency: "0001",
      number: "123456",
      createdAt: new Date(),
      deletedAt: null,
      isBlocked: false,
      status: AccountStatus.ACTIVE,
    });
    const accountSpy = jest
      .spyOn(accountRepository, "findById")
      .mockResolvedValue(mockAccount);
    const depositSpy = jest.spyOn(mockAccount, "deposit");
    const saveSpy = jest.spyOn(accountRepository, "save");
    const createSpy = jest.spyOn(accountTransactionRepository, "create");
    await usecase.execute(1, 300);
    expect(accountSpy).toHaveBeenCalledWith(1);
    expect(depositSpy).toHaveBeenCalledWith(new Money(300));
    expect(saveSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        balance: new Money(1300),
      }),
    );
    expect(createSpy).toHaveBeenCalledWith({
      accountId: 1,
      amount: new Money(300),
      type: TransactionType.DEPOSIT,
    });
  });

  it("should return error if account not found", async () => {
    const accountSpy = jest
      .spyOn(accountRepository, "findById")
      .mockResolvedValue(null);
    await expect(usecase.execute(1, 300)).rejects.toThrow(AccountNotFoundError);
    expect(accountSpy).toHaveBeenCalled();
  });
});
