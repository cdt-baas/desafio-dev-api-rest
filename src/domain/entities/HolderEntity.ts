import { InvalidCpfError } from "@domain/errors";

type HolderProps = {
  fullName: string;
  cpf: string;
  id?: number;
  createdAt?: Date;
  deletedAt?: Date | null;
};

export class Holder {
  id?: number;
  fullName: string;
  cpf: string;
  createdAt?: Date;
  deletedAt?: Date | null;

  constructor(props: HolderProps) {
    this.fullName = props.fullName.trim();
    this.cpf = props.cpf.replace(/\D/g, "");
    this.id = props.id;
    this.createdAt = props.createdAt;
    this.deletedAt = props.deletedAt;
    this.validateCpf();
  }

  private validateCpf(): void {
    if (this.cpf.length !== 11) {
      throw new InvalidCpfError();
    }
  }
}
