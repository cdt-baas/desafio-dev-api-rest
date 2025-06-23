import { IsNull, Repository } from "typeorm";
import { TypeOrmAccountRepository } from "@infra/database/repositories/TypeOrmAccountRepository";
import AccountOrmEntity from "@infra/database/entities/AccountOrmEntity";
import HolderOrmEntity from "@infra/database/entities/HolderOrmEntity";
import { Holder } from "@domain/entities/HolderEntity";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { Money } from "@domain/valueObject/Money";

describe("TypeOrm Account Repository", () => {
  let accountRepository: TypeOrmAccountRepository;
  let mockAccountRepository: Repository<AccountOrmEntity>;

  beforeAll(async () => {
    mockAccountRepository = {
      create: jest.fn(),
      save: jest.fn(),
      findOne: jest.fn(),
      delete: jest.fn(),
      find: jest.fn(),
      findOneByOrFail: jest.fn(),
    } as unknown as Repository<AccountOrmEntity>;

    accountRepository = new TypeOrmAccountRepository(mockAccountRepository);
  });

  afterAll(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(accountRepository).toBeDefined();
  });

  it("should create an account", async () => {
    const holder = new Holder({
      id: 123,
      fullName: "John Doe",
      cpf: "12345678901",
    });
    const savedAccount = {
      id: 1,
      holder: holder as unknown as HolderOrmEntity,
      balance: Money.zero().toString(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    } as AccountOrmEntity;
    const createSpy = jest
      .spyOn(mockAccountRepository, "create")
      .mockReturnValue(savedAccount);
    const saveSpy = jest.spyOn(mockAccountRepository, "save");

    const account = new Account({
      holderId: holder.id!,
      balance: Money.zero(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
    });
    await accountRepository.create(account, holder);
    expect(createSpy).toHaveBeenCalledWith({
      holder,
      balance: account.balance.toString(),
      agency: account.agency,
      number: account.number,
      status: account.status,
      isBlocked: account.isBlocked,
    });
    expect(saveSpy).toHaveBeenCalledWith(savedAccount);
  });

  it("should find an account by agency and account number", async () => {
    const holder = new Holder({
      id: 123,
      fullName: "John Doe",
      cpf: "12345678901",
    });
    const mockAccount = {
      id: 1,
      holder: holder as unknown as HolderOrmEntity,
      balance: Money.zero().toString(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    } as AccountOrmEntity;
    const findOneSpy = jest
      .spyOn(mockAccountRepository, "findOne")
      .mockResolvedValue(mockAccount);
    const account = await accountRepository.findByAgencyAndNumber(
      "0001",
      "123456",
    );
    expect(account).toBeInstanceOf(Account);
    expect(account?.id).toBe(mockAccount.id);
    expect(account?.holderId).toBe(holder.id);
    expect(account?.balance.toNumber()).toBe(0);
    expect(account?.agency).toBe(mockAccount.agency);
    expect(account?.number).toBe(mockAccount.number);
    expect(account?.status).toBe(mockAccount.status);
    expect(account?.isBlocked).toBe(mockAccount.isBlocked);
    expect(account?.createdAt).toEqual(mockAccount.createdAt);
    expect(account?.deletedAt).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { agency: "0001", number: "123456", deletedAt: IsNull() },
      relations: ["holder"],
    });
  });

  it("should return null when finding an account by non-existent agency and number", async () => {
    const findOneSpy = jest
      .spyOn(mockAccountRepository, "findOne")
      .mockResolvedValue(null);
    const account = await accountRepository.findByAgencyAndNumber(
      "0001",
      "654321",
    );
    expect(account).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { agency: "0001", number: "654321", deletedAt: IsNull() },
      relations: ["holder"],
    });
  });

  it("should save an account", async () => {
    const existingAccount = new Account({
      id: 1,
      holderId: 123,
      balance: Money.zero(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    const findOneSpy = jest
      .spyOn(mockAccountRepository, "findOneByOrFail")
      .mockResolvedValue(existingAccount as unknown as AccountOrmEntity);
    const saveSpy = jest.spyOn(mockAccountRepository, "save");

    const account = new Account({
      id: 1,
      holderId: 123,
      balance: Money.zero(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    account.close();
    await accountRepository.save(account);
    expect(findOneSpy).toHaveBeenCalledWith({ id: existingAccount.id });
    expect(saveSpy).toHaveBeenCalledWith({
      ...account,
      balance: account.balance.toString(),
    });
  });
  it("should throw an error when saving a non-existent account", async () => {
    const nonExistentAccount = new Account({
      id: 999,
      holderId: 123,
      balance: new Money(1000),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
    });
    const expectedError = new Error("Account not found");
    jest
      .spyOn(mockAccountRepository, "findOneByOrFail")
      .mockRejectedValue(expectedError);

    await expect(accountRepository.save(nonExistentAccount)).rejects.toThrow(
      expectedError,
    );
  });
  it("should update an account's status, balance, and isBlocked properties", async () => {
    const existingAccount = new Account({
      id: 1,
      holderId: 123,
      balance: new Money(1000),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    const findOneSpy = jest
      .spyOn(mockAccountRepository, "findOneByOrFail")
      .mockResolvedValue(existingAccount as unknown as AccountOrmEntity);
    const saveSpy = jest.spyOn(mockAccountRepository, "save");

    existingAccount.block();
    await accountRepository.save(existingAccount);
    expect(findOneSpy).toHaveBeenCalledWith({ id: existingAccount.id });
    expect(saveSpy).toHaveBeenCalledWith(existingAccount);
  });

  it("should find an account by ID", async () => {
    const mockHolder = new Holder({
      id: 123,
      fullName: "John Doe",
      cpf: "12345678901",
    });
    const mockAccount = new Account({
      id: 1,
      holderId: mockHolder.id!,
      balance: new Money(1000),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    const findOneSpy = jest
      .spyOn(mockAccountRepository, "findOne")
      .mockResolvedValue({
        ...mockAccount,
        balance: new Money(1000).toString(),
        holder: mockHolder as unknown as HolderOrmEntity,
      } as AccountOrmEntity);
    const account = await accountRepository.findById(1);
    expect(account).toBeInstanceOf(Account);
    expect(account?.id).toBe(mockAccount.id);
    expect(account?.holderId).toBe(mockAccount.holderId);
    expect(account?.balance.toNumber()).toBe(1000);
    expect(account?.agency).toBe(mockAccount.agency);
    expect(account?.number).toBe(mockAccount.number);
    expect(account?.status).toBe(mockAccount.status);
    expect(account?.isBlocked).toBe(mockAccount.isBlocked);
    expect(account?.createdAt).toEqual(mockAccount.createdAt);
    expect(account?.deletedAt).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { id: 1, deletedAt: IsNull() },
      relations: ["holder"],
    });
  });
  it("should return null when finding an account by non-existent ID", async () => {
    const findOneSpy = jest
      .spyOn(mockAccountRepository, "findOne")
      .mockResolvedValue(null);
    const account = await accountRepository.findById(999);
    expect(account).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { id: 999, deletedAt: IsNull() },
      relations: ["holder"],
    });
  });
});
