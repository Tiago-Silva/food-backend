CREATE TABLE `food`.`users` (
    `id` VARCHAR(256) NOT NULL,
    `nome` VARCHAR(45) NOT NULL,
    `sobreNome` VARCHAR(45) NULL,
    `login` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `telefone` VARCHAR(45) NULL,
    `endereco` VARCHAR(45) NULL,
    `cpf` VARCHAR(45) NULL,
    `email` VARCHAR(45) NULL,
    `type` VARCHAR(45) NOT NULL,
    `role` VARCHAR(45) NOT NULL,
    `account_non_expired` BIT(1) NOT NULL,
    `account_non_locked` BIT(1) NOT NULL,
    `credentials_non_expired` BIT(1) NOT NULL,
    `enabled` BIT(1) NOT NULL,
    `idestabelecimento` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_1_users_estabelecimento_idestabelecimento_idx` (`idestabelecimento` ASC) VISIBLE,
    CONSTRAINT `fk_1_users_estabelecimento_idestabelecimento_idx`
        FOREIGN KEY (`idestabelecimento`)
            REFERENCES `food`.`estabelecimento` (`idestabelecimento`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE
);
