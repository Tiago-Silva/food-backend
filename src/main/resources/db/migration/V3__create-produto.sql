CREATE TABLE `food`.`produto` (
    `idproduto` INT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(45) NOT NULL,
    `descricao` VARCHAR(45) NOT NULL,
    `valor` DECIMAL(10,2) NOT NULL,
    `categoria` VARCHAR(45) NOT NULL,
    `status` BIT(1) NOT NULL,
    `url_image` VARCHAR(45) NULL,
    `enable_promotions` BIT(1) NULL,
    `idestabelecimento` INT NOT NULL,
    PRIMARY KEY (`idproduto`),
    INDEX `fk_1_produto_estabelecimento_idestabelecimento_idx` (`idestabelecimento` ASC) VISIBLE,
    CONSTRAINT `fk_1_produto_estabelecimento_idestabelecimento_idx`
        FOREIGN KEY (`idestabelecimento`)
            REFERENCES `food`.`estabelecimento` (`idestabelecimento`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
