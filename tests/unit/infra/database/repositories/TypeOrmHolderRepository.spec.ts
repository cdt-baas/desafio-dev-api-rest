import { TypeOrmHolderRepository } from "@infra/database/repositories/TypeOrmHolderRepository";
import { Holder } from "@domain/entities/HolderEntity";
import HolderOrmEntity from "@infra/database/entities/HolderOrmEntity";
import { IsNull, Repository } from "typeorm";

describe("TypeOrm Holder Repository", () => {
  let mockHolderRepository: Repository<HolderOrmEntity>;
  let repository: TypeOrmHolderRepository;

  beforeAll(async () => {
    mockHolderRepository = {
      create: jest.fn(),
      save: jest.fn(),
      findOne: jest.fn(),
      update: jest.fn(),
      find: jest.fn(),
    } as unknown as Repository<HolderOrmEntity>;

    repository = new TypeOrmHolderRepository(mockHolderRepository);
  });

  afterAll(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(repository).toBeDefined();
  });

  it("should create a holder", async () => {
    const holder = new Holder({ fullName: "Test testing", cpf: "98765432100" });
    const savedHolder = {
      ...holder,
      id: 1,
      createdAt: new Date(),
      deletedAt: null,
    } as HolderOrmEntity;

    const createSpy = jest
      .spyOn(mockHolderRepository, "create")
      .mockReturnValue(savedHolder);
    const saveSpy = jest.spyOn(mockHolderRepository, "save");

    await repository.create(holder);
    expect(createSpy).toHaveBeenCalledWith({
      fullName: holder.fullName,
      cpf: holder.cpf,
    });
    expect(saveSpy).toHaveBeenCalledWith(savedHolder);
  });

  it("should find a holder by id", async () => {
    const holderId = 1;
    const holder = new Holder({
      id: holderId,
      fullName: "Test testing",
      cpf: "98765432100",
    });

    const findOneSpy = jest
      .spyOn(mockHolderRepository, "findOne")
      .mockResolvedValue({
        ...holder,
        id: holderId,
        createdAt: new Date(),
        deletedAt: null,
      } as HolderOrmEntity);

    const result = await repository.findById(holderId);
    expect(result).toBeDefined();
    expect(result?.id).toBe(holderId);
    expect(result?.fullName).toBe(holder.fullName);
    expect(result?.cpf).toBe(holder.cpf);
    expect(result?.createdAt).toBeDefined();
    expect(result?.deletedAt).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { id: holderId, deletedAt: IsNull() },
    });
  });
  it("should return null when finding a holder by non-existent id", async () => {
    const holderId = 999;
    const findOneSpy = jest
      .spyOn(mockHolderRepository, "findOne")
      .mockResolvedValue(null);

    const result = await repository.findById(holderId);
    expect(result).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { id: holderId, deletedAt: IsNull() },
    });
  });
  it("should delete a holder by id", async () => {
    const holderId = 1;
    const updateSpy = jest
      .spyOn(mockHolderRepository, "update")
      .mockResolvedValue({} as any);

    await repository.deleteById(holderId);
    expect(updateSpy).toHaveBeenCalledWith(holderId, {
      deletedAt: expect.any(Date),
    });
  });

  it("should find a holder by CPF", async () => {
    const cpf = "98765432100";
    const mockHolder = {
      id: 1,
      fullName: "Test testing",
      cpf,
    } as HolderOrmEntity;
    const findOneSpy = jest
      .spyOn(mockHolderRepository, "findOne")
      .mockResolvedValue(mockHolder);

    const result = await repository.findByCpf(cpf);
    expect(result).toBeDefined();
    expect(result?.id).toBe(mockHolder.id);
    expect(result?.cpf).toBe(mockHolder.cpf);
    expect(result?.fullName).toBe(mockHolder.fullName);
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { cpf, deletedAt: IsNull() },
    });
  });
  it("should return null when finding a holder by non-existent CPF", async () => {
    const cpf = "00000000000";
    const findOneSpy = jest
      .spyOn(mockHolderRepository, "findOne")
      .mockResolvedValue(null);

    const result = await repository.findByCpf(cpf);
    expect(result).toBeNull();
    expect(findOneSpy).toHaveBeenCalledWith({
      where: { cpf, deletedAt: IsNull() },
    });
  });
});
