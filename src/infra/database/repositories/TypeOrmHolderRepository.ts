import { IsNull, Repository } from "typeorm";
import { Holder } from "@domain/entities/HolderEntity";
import { HolderRepository } from "@domain/repositories/holderRepository";
import HolderOrmEntity from "../entities/HolderOrmEntity";

export class TypeOrmHolderRepository implements HolderRepository {
  constructor(private readonly repository: Repository<HolderOrmEntity>) {}

  async create(holder: Holder): Promise<void> {
    const holderEntity = this.repository.create({
      fullName: holder.fullName,
      cpf: holder.cpf,
    });
    await this.repository.save(holderEntity);
  }

  async deleteById(id: number): Promise<void> {
    await this.repository.update(id, { deletedAt: new Date() });
  }

  async findById(id: number): Promise<Holder | null> {
    const holderEntity = await this.repository.findOne({
      where: { id, deletedAt: IsNull() },
    });
    return holderEntity ? this.toDomain(holderEntity) : null;
  }

  async findByCpf(cpf: string): Promise<Holder | null> {
    const holderEntity = await this.repository.findOne({
      where: { cpf, deletedAt: IsNull() },
    });
    return holderEntity ? this.toDomain(holderEntity) : null;
  }

  private toDomain(entity: HolderOrmEntity): Holder {
    return new Holder({
      id: entity.id,
      fullName: entity.fullName,
      cpf: entity.cpf,
      createdAt: entity.createdAt,
      deletedAt: entity.deletedAt,
    });
  }
}
