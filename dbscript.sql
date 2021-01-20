CREATE TABLE `contas` (
  `idConta` int NOT NULL AUTO_INCREMENT,
  `idPessoa` int NOT NULL,
  `saldo` decimal(15,2) NOT NULL,
  `limiteSaqueDiario` decimal(15,2) NOT NULL,
  `flagAtivo` tinyint NOT NULL,
  `tipoConta` int NOT NULL,
  `dataCriacao` date NOT NULL,
  PRIMARY KEY (`idConta`),
  KEY `fk_contas_1_idx` (`idPessoa`),
  CONSTRAINT `fk_contas_1` FOREIGN KEY (`idPessoa`) REFERENCES `pessoas` (`idPessoa`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pessoas` (
  `idPessoa` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `cpf` varchar(45) NOT NULL,
  `dataNascimento` date NOT NULL,
  PRIMARY KEY (`idPessoa`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transacoes` (
  `idTransacao` int NOT NULL AUTO_INCREMENT,
  `idConta` int NOT NULL,
  `valor` decimal(15,2) NOT NULL,
  `dataTransacao` date NOT NULL,
  PRIMARY KEY (`idTransacao`),
  KEY `fk_transacoes_1_idx` (`idConta`),
  CONSTRAINT `fk_transacoes_1` FOREIGN KEY (`idConta`) REFERENCES `contas` (`idConta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
