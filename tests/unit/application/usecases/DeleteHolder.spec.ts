import { HolderRepository } from "@domain/repositories/holderRepository";
import { DeleteHolder } from "@application/usecases/DeleteHolder";
import { Holder } from "@domain/entities/HolderEntity";
import { HolderNotFoundError } from "@domain/errors";

describe("Delete Holder", () => {
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

  it("should delete a holder successfully", async () => {
    const findByIdSpy = jest.spyOn(repository, "findById").mockResolvedValue(
      new Holder({
        id: 1,
        fullName: "John Doe",
        cpf: "12345678901",
      }),
    );
    const deleteSpy = jest.spyOn(repository, "deleteById").mockResolvedValue();

    const deleteHolder = new DeleteHolder(repository);
    await deleteHolder.execute(1);

    expect(findByIdSpy).toHaveBeenCalledWith(1);
    expect(deleteSpy).toHaveBeenCalledWith(1);
  });
  it("should handle errors gracefully when deleting a holder", async () => {
    const expectedError = new Error("Database error");
    const findByIdSpy = jest.spyOn(repository, "findById").mockResolvedValue(
      new Holder({
        id: 1,
        fullName: "John Doe",
        cpf: "12345678901",
      }),
    );
    const deleteSpy = jest
      .spyOn(repository, "deleteById")
      .mockRejectedValue(expectedError);

    const deleteHolder = new DeleteHolder(repository);

    await expect(deleteHolder.execute(1)).rejects.toThrow(expectedError);
    expect(findByIdSpy).toHaveBeenCalledWith(1);
    expect(deleteSpy).toHaveBeenCalledWith(1);
  });
  it("should throw an error if the holder does not exist", async () => {
    const findByIdSpy = jest
      .spyOn(repository, "findById")
      .mockResolvedValue(null);

    const deleteHolder = new DeleteHolder(repository);

    await expect(deleteHolder.execute(1)).rejects.toThrow(HolderNotFoundError);
    expect(findByIdSpy).toHaveBeenCalledWith(1);
  });
});
