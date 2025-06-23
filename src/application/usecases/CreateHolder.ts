import { Holder } from "@domain/entities/HolderEntity";
import { HolderRepository } from "@domain/repositories/holderRepository";

export interface CreateHolderDto {
  fullName: string;
  cpf: string;
}

export class CreateHolder {
  constructor(private readonly holderRepository: HolderRepository) {}

  async execute({ fullName, cpf }: CreateHolderDto): Promise<void> {
    const holder = new Holder({ fullName, cpf });
    await this.holderRepository.create(holder);
  }
}
