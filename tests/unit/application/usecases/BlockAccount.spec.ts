import { AccountRepository } from "@domain/repositories/accountRepository";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { AccountBlockedError, AccountNotFoundError } from "@domain/errors";
import { BlockAccount } from "@application/usecases/BlockAccount";
import { Money } from "@domain/valueObject/Money";

describe("Block Account", () => {
  let repository: AccountRepository;
  let usecase: BlockAccount;

  beforeEach(() => {
    repository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
    usecase = new BlockAccount(repository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(usecase).toBeDefined();
    expect(repository).toBeDefined();
  });
  it("should block an account successfully", async () => {
    const mockAccount = new Account({
      id: 1,
      holderId: 1,
      balance: Money.zero(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    const blockSpy = jest.spyOn(mockAccount, "block");
    const findByIdSpy = jest
      .spyOn(repository, "findById")
      .mockResolvedValue(mockAccount as any);
    const saveSpy = jest.spyOn(repository, "save").mockResolvedValue(undefined);
    await usecase.execute(1);
    expect(blockSpy).toHaveBeenCalled();
    expect(findByIdSpy).toHaveBeenCalledWith(1);
    expect(saveSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        isBlocked: true,
      }),
    );
  });
  it("should throw an error if account not found", async () => {
    const findByIdSpy = jest
      .spyOn(repository, "findById")
      .mockResolvedValue(null);

    await expect(usecase.execute(1)).rejects.toThrow(AccountNotFoundError);
    expect(findByIdSpy).toHaveBeenCalledWith(1);
  });
  it("should throw an error if account is already blocked", async () => {
    const mockAccount = new Account({
      id: 2,
      holderId: 42,
      balance: Money.zero(),
      agency: "0001",
      number: "654321",
      status: AccountStatus.ACTIVE,
      isBlocked: true,
      createdAt: new Date(),
      deletedAt: null,
    });
    jest.spyOn(repository, "findById").mockResolvedValue(mockAccount as any);
    await expect(usecase.execute(2)).rejects.toThrow(AccountBlockedError);
  });
});
