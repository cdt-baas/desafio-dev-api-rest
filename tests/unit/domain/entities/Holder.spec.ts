import { Holder } from "@domain/entities/HolderEntity";
import { InvalidCpfError } from "@domain/errors";

describe("Holder", () => {
  it("should create a holder", () => {
    const holder = new Holder({
      fullName: " Test testing ",
      cpf: "123.456.789-01",
    });
    expect(holder.fullName).toBe("Test testing");
    expect(holder.cpf).toBe("12345678901");
  });
  it("should return error if cpf is invalid", () => {
    expect(
      () => new Holder({ fullName: "Test testing", cpf: "12345678" }),
    ).toThrow(InvalidCpfError);
  });
  it("should create a holder with all properties", () => {
    const holder = new Holder({
      fullName: "Test testing",
      cpf: "12345678901",
      id: 1,
      createdAt: new Date(),
      deletedAt: null,
    });
    expect(holder.id).toBe(1);
    expect(holder.fullName).toBe("Test testing");
    expect(holder.cpf).toBe("12345678901");
    expect(holder.createdAt).toBeDefined();
    expect(holder.deletedAt).toBeNull();
  });
});
