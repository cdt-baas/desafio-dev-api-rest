### Como executar o projeto
  O projeto esta configurado para ser executado atraves do docker-compose. Ao executar o passo a passo a seguir automaticamente o banco de dados sera criado e a tabela pessoa populada com dois registros conforme solicitado.

  Passo a passo:
  - Garanta que voce possui uma versao de docker-compose em seu computador;
  - Para subir os servicos abra o seu terminal;
  - Navegue ao diretorio onde se econtra o arquivo docker-compose.yml;
  - Execute o comando docker-compose build;
  - Apos o build, execute o comando docker-compose up;
  - Os logs dos servicos devem aparecer em seu terminal;
  - A api-contas estara disponivel no endereco: localhost:3000/conta/
  - A api-transacao estara disponivel no endereco: localhost:3001/transacao/


### Arquitetura
Foi utilizada a seguinte arquitetura:
| Item | Descricao |
|-|-|
| api-contas | api que contem os endpoints responsaveis por interagir com a entidade conta |
| api-transacao | api que contem os endpoints responsaveis por realizar/retornar transacoes |
| mysql | banco de dados utilizado para implementacao de acordo com as caracteristicas do projeto |
| node-cache | package utilizada para implementar o cache de limite diario de saque |
| express | package escolhida devido a sua popularidade e eficiencia para criar aplicacoes nodejs |
| axios | package escolhida para comunicacao entre os servicos |

### Api-contas
    Descricao: esta api sera responsavel pelas interacoes com a entidade conta de nossa aplicacao.
    
| path | tipo | parametros |Descricao |
|-|-|-|-|
| /conta/:idConta | GET | idConta | retorna uma conta |
| /conta | POST | JSON conta | cria uma conta |
| /conta/saldo/:idConta | GET | idConta | retorna o saldo da conta |
| /conta/inativar/:idConta | PUT | idConta | bloqueia a conta |
| /conta/atualizarSaldo| PUT | idConta | atualiza o saldo da conta |

- JSON para criar uma conta:
    ```
    {
    "idPessoa": 1,
    "saldo": 100.00,
    "limiteSaqueDiario": 50,
    "flagAtivo": 1,
    "tipoConta":1
      }
    ```
### Api-transacao
      Descricao: esta api rece as transacoes atraves de um JSON. Se o valor for positivo um deposito sera realizado, caso o valor da transacao seja negativo um saque sera realizado. Tambem sera possivel consultar todas as transacoes para uma determinada conta.
| path | tipo | parametros |Descricao |
|-|-|-|-|
| /transacao/:idConta| GET | idConta | retorna todas as transacoes da conta |
| /transacao | POST | JSON transacao | executa um saque caso o valor seja negativo; executa um deposito caso o valor seja positivo|

- JSON para criar um saque:
    ```
    {
    "idConta": 1,
    "valor": -10.50
    }
    ```
- JSON para criar um deposito:
    ```
    {
    "idConta": 1,
    "valor": 10.50
    }
    ```




### Sobre a oportunidade 
A vaga é para Desenvolvedor(a), temos vagas com diversos níveis de senioridade e para cada um deles utilizaremos critérios específicos considerando este aspecto, combinado? 
Se você for aprovad(a) nesta etapa, será convidado para uma entrevista final.

### Desafio Técnico
  Nós trabalhamos com meios de pagamento e nada melhor do que um bom sistema para gestão de contas:
  
  - Pré-requisitos:
    ```
    * Desenvolver os recursos em API Rest que realizam operações bancárias com a entidade conta a seguir:
    ```
    | Contas | Tipo |
    |-|-|
    | idConta | Numérico |
    | idPessoa | Numérico |
    | saldo | Monetário |
    | limiteSaqueDiario | Monetário |
    | flagAtivo | Condicional |
    | tipoConta | Numérido |
    | dataCriacao | Data |

    ```
    * Tabela de transações realizadas na conta
    ```
    | Transacoes | Tipo |
    |-|-|
    | idTransacao | Numérico |
    | idConta | Numérico |
    | valor | Monetário |
    | dataTransacao | Data |

    ```
    * P.S.: Não é necessário realizar operações com a tabela pessoa, mas é necessária a criação da tabela para mapeamento da relação com a conta e enviar script de criação de pelo menos uma pessoa.
    ```

    | Pessoas | Tipo |
    |-|-|
    | idPessoa | Numérico |
    | nome | Texto |
    | cpf | Texto |
    | dataNascimento | Data |    

  - O que esperamos como escopo mínimo:
    ```
    * Implementar path que realiza a criação de uma conta;
    * Implementar path que realiza operação de depósito em uma conta;
    * Implementar path que realiza operação de consulta de saldo em determinada conta;
    * Implementar path que realiza operação de saque em uma conta;
    * Implementar path que realiza o bloqueio de uma conta;
    * Implementar path que recupera o extrato de transações de uma conta;
    ```
  - O que será diferencial:
    ```
    * Implementar extrato por período;
    * Elaborar manual de execução;
    * Elaborar documentação;
    * Elaborar testes.
    ```
    
  - O que vamos avaliar:
    ```
    * Seu código; 
    * Script de banco;
    * Organização;
    * Boas práticas;
    * Diferenciais;    
    ```


### Instruções
      1. Faça o fork do desafio;
      2. Crie um repositório privado no seu github para o projeto e adicione como colaborador o usuário wesleyjoliveira;
      3. Desenvolva. Você terá 7 (sete) dias a partir da data do envio do desafio; 
      4. Após concluir seu trabalho faça um push; 
      5. Envie um e-mail à pessoa que está mantendo o contato com você durante o processo notificando a finalização do desafio para validação.
