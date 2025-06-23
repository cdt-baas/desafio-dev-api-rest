import { AccountRepository } from "@domain/repositories/accountRepository";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { AccountNotFoundError, AccountUnBlockedError } from "@domain/errors";
import { UnBlockAccount } from "@application/usecases/UnBlockAccount";
import { Money } from "@domain/valueObject/Money";

describe("UnBlock Account", () => {
  let repository: AccountRepository;
  let usecase: UnBlockAccount;

  beforeEach(() => {
    repository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
    usecase = new UnBlockAccount(repository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(usecase).toBeDefined();
    expect(repository).toBeDefined();
  });
  it("should unblock an account successfully", async () => {
    const mockAccount = new Account({
      id: 1,
      holderId: 1,
      balance: Money.zero(),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: true,
      createdAt: new Date(),
      deletedAt: null,
    });
    const unblockSpy = jest.spyOn(mockAccount, "unblock");
    const findByIdSpy = jest
      .spyOn(repository, "findById")
      .mockResolvedValue(mockAccount as any);
    const saveSpy = jest.spyOn(repository, "save").mockResolvedValue(undefined);
    await usecase.execute(1);
    expect(unblockSpy).toHaveBeenCalled();
    expect(findByIdSpy).toHaveBeenCalledWith(1);
    expect(saveSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        isBlocked: false,
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
  it("should throw an error if account is already unblocked", async () => {
    const mockAccount = new Account({
      id: 2,
      holderId: 42,
      balance: Money.zero(),
      agency: "0001",
      number: "654321",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    jest.spyOn(repository, "findById").mockResolvedValue(mockAccount as any);
    await expect(usecase.execute(2)).rejects.toThrow(AccountUnBlockedError);
  });
});
