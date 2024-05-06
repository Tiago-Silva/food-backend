CREATE TABLE `food`.`item` (
    `iditem` BIGINT NOT NULL AUTO_INCREMENT,
    `quantidade` INT NOT NULL,
    `descricao` VARCHAR(45) NOT NULL,
    `idproduto` INT NOT NULL,
    `idpedido` BIGINT NOT NULL,
    PRIMARY KEY (`iditem`),
    INDEX `fk_1_item_pedido_idpedido_idx` (`idpedido` ASC) VISIBLE,
    CONSTRAINT `fk_1_item_pedido_idpedido_idx`
        FOREIGN KEY (`idpedido`)
            REFERENCES `food`.`pedido` (`idpedido`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
