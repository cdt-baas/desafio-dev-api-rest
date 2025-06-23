import { Holder } from "@domain/entities/HolderEntity";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { Account, AccountStatus } from "@domain/entities/AccountEntity";
import { AccountNotFoundError } from "@domain/errors";
import { GetAccountByAgencyAndNumber } from "@application/usecases/GetAccountByAgencyAndNumber";
import { Money } from "@domain/valueObject/Money";

describe("Get Account By Agency And Number", () => {
  let accountRepository: AccountRepository;
  let usecase: GetAccountByAgencyAndNumber;

  beforeEach(() => {
    accountRepository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
    usecase = new GetAccountByAgencyAndNumber(accountRepository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(accountRepository).toBeDefined();
    expect(usecase).toBeDefined();
  });

  it("should return account details by agency and number", async () => {
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
    const findByAgencyAndNumberSpy = jest
      .spyOn(accountRepository, "findByAgencyAndNumber")
      .mockResolvedValue(mockAccount);

    const result = await usecase.execute({
      agency: "0001",
      number: "123456",
    });
    expect(result.balance).toBe(new Money(1000).toString());
    expect(result.id).toBe(1);
    expect(result.agency).toBe("0001");
    expect(result.number).toBe("123456");
    expect(findByAgencyAndNumberSpy).toHaveBeenCalledWith("0001", "123456");
  });

  it("should throw an error if account is not found", async () => {
    jest
      .spyOn(accountRepository, "findByAgencyAndNumber")
      .mockResolvedValue(null);

    await expect(
      usecase.execute({ agency: "0001", number: "123456" }),
    ).rejects.toThrow(AccountNotFoundError);
  });
});
