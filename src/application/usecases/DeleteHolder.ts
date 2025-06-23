import { HolderNotFoundError } from "@domain/errors";
import { HolderRepository } from "@domain/repositories/holderRepository";

export class DeleteHolder {
  constructor(private readonly holderRepository: HolderRepository) {}

  async execute(id: number): Promise<void> {
    const holder = await this.holderRepository.findById(id);
    if (!holder) {
      throw new HolderNotFoundError();
    }
    await this.holderRepository.deleteById(id);
  }
}
