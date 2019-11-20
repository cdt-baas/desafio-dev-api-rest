from models.contas import Contas
from models.pessoas import Pessoas
from models.transacoes import Transacoes


def cria_pessoa():
    pessoa = Pessoas(nome='Guilherme', cpf='12212621', data_nascimento=1992)
    print(pessoa)
    pessoa.save()


# Implementar path que realiza a criação de uma conta;
def cria_conta():
    conta = Contas(pessoa_id=1, saldo=16000, limite_saque_diario=10000, flag_ativo=True, tipo_conta=1,
                   data_criacao=2019)
    print(conta)
    conta.save()


def cria_transacao():
    transacao = Transacoes(conta_id=1, valor=300, tipo='saque')
    print(transacao)
    transacao.save()


def deleta_pessoa():
    pessoa = Pessoas.query.all()
    pessoa.delete()


if __name__ == '__main__':
    cria_pessoa()
    cria_conta()
    #cria_transacao()
