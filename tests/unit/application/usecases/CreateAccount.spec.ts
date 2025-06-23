import { HolderRepository } from "@domain/repositories/holderRepository";
import { Holder } from "@domain/entities/HolderEntity";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { AccountStatus } from "@domain/entities/AccountEntity";
import { CreateAccount } from "@application/usecases/CreateAccount";
import { HolderNotFoundError } from "@domain/errors";
import { Money } from "@domain/valueObject/Money";

describe("Create Account", () => {
  let holderRepository: HolderRepository;
  let accountRepository: AccountRepository;

  beforeEach(() => {
    holderRepository = {
      create: jest.fn(),
      deleteById: jest.fn(),
      findById: jest.fn(),
      findByCpf: jest.fn(),
    } as HolderRepository;

    accountRepository = {
      create: jest.fn(),
      findByAgencyAndNumber: jest.fn(),
      save: jest.fn(),
      findById: jest.fn(),
    } as AccountRepository;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(accountRepository).toBeDefined();
    expect(holderRepository).toBeDefined();
  });

  it("should create an account successfully", async () => {
    const cpf = "12345678911";
    const mockHolder = new Holder({
      id: 1,
      fullName: "Test testing",
      cpf,
    });
    const findByCpfSpy = jest
      .spyOn(holderRepository, "findByCpf")
      .mockResolvedValue(mockHolder);
    const createSpy = jest
      .spyOn(accountRepository, "create")
      .mockResolvedValue();

    const createAccount = new CreateAccount(
      accountRepository,
      holderRepository,
    );
    await createAccount.execute({
      cpf,
      balance: 1000,
      agency: "0001",
      number: "123456",
    });

    expect(findByCpfSpy).toHaveBeenCalledWith(cpf);
    expect(createSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        holderId: mockHolder.id,
        balance: new Money(1000),
        agency: "0001",
        number: "123456",
        status: AccountStatus.ACTIVE,
        isBlocked: false,
      }),
      mockHolder,
    );
  });

  it("should throw an error if holder is not found", async () => {
    const cpf = "12345678911";
    jest.spyOn(holderRepository, "findByCpf").mockResolvedValue(null);

    const createAccount = new CreateAccount(
      accountRepository,
      holderRepository,
    );

    await expect(
      createAccount.execute({
        cpf,
        balance: 1000,
        agency: "0001",
        number: "123456",
      }),
    ).rejects.toThrow(HolderNotFoundError);
  });
});
