CREATE TABLE `food`.`estabelecimento` (
    `idestabelecimento` INT NOT NULL AUTO_INCREMENT,
    `razao_social` VARCHAR(45) NOT NULL,
    `nome_fantasia` VARCHAR(45) NOT NULL,
    `cnpj` VARCHAR(45) NOT NULL,
    `cpf` VARCHAR(45) NOT NULL,
    `pais` VARCHAR(45) NULL,
    `estado` VARCHAR(6) NULL,
    `cidade` VARCHAR(45) NULL,
    `bairro` VARCHAR(45) NULL,
    `endereco` VARCHAR(45) NULL,
    `telefone` VARCHAR(45) NULL,
    PRIMARY KEY (`idestabelecimento`)
);
