import { AccountRepository } from "@domain/repositories/accountRepository";
import { CloseAccount } from "@application/usecases/CloseAccount";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { AccountNotFoundError } from "@domain/errors";
import { Money } from "@domain/valueObject/Money";

describe("Close Account", () => {
  let repository: AccountRepository;
  let usecase: CloseAccount;

  beforeEach(() => {
    repository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
    usecase = new CloseAccount(repository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(usecase).toBeDefined();
    expect(repository).toBeDefined();
  });
  it("should close an account successfully", async () => {
    const mockAccount = new Account({
      id: 1,
      holderId: 1,
      balance: new Money(0),
      agency: "0001",
      number: "123456",
      status: AccountStatus.ACTIVE,
      isBlocked: false,
      createdAt: new Date(),
      deletedAt: null,
    });
    const closeSpy = jest.spyOn(mockAccount, "close");
    const findByIdSpy = jest
      .spyOn(repository, "findById")
      .mockResolvedValue(mockAccount as any);
    const saveSpy = jest.spyOn(repository, "save").mockResolvedValue(undefined);
    await usecase.execute(1);
    expect(closeSpy).toHaveBeenCalled();
    expect(findByIdSpy).toHaveBeenCalledWith(1);
    expect(saveSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        status: "CLOSED",
        deletedAt: expect.any(Date),
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
});
