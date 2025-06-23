import { Holder } from "../entities/HolderEntity";

export interface HolderRepository {
  create(holder: Holder): Promise<void>;
  findById(id: number): Promise<Holder | null>;
  findByCpf(cpf: string): Promise<Holder | null>;
  deleteById(id: number): Promise<void>;
}
