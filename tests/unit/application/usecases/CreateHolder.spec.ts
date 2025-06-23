import { HolderRepository } from "@domain/repositories/holderRepository";
import { CreateHolder } from "@application/usecases/CreateHolder";

describe("Create Holder", () => {
  let repository: HolderRepository;

  beforeEach(() => {
    repository = {
      create: jest.fn(),
      deleteById: jest.fn(),
      findById: jest.fn(),
      findByCpf: jest.fn(),
    } as HolderRepository;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should create a holder successfully", async () => {
    const createSpy = jest.spyOn(repository, "create").mockResolvedValue();

    const createHolder = new CreateHolder(repository);
    await createHolder.execute({
      fullName: "John Doe",
      cpf: "12345678901",
    });

    expect(createSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        fullName: "John Doe",
        cpf: "12345678901",
      }),
    );
  });

  it("should handle errors gracefully", async () => {
    const expectedError = new Error("Database error");
    const createSpy = jest
      .spyOn(repository, "create")
      .mockRejectedValue(expectedError);

    const createHolder = new CreateHolder(repository);

    await expect(
      createHolder.execute({
        fullName: "Jane Doe",
        cpf: "10987654321",
      }),
    ).rejects.toThrow(expectedError);
    expect(createSpy).toHaveBeenCalledWith(
      expect.objectContaining({
        fullName: "Jane Doe",
        cpf: "10987654321",
      }),
    );
  });
});
