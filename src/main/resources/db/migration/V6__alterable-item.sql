ALTER TABLE `food`.`item`
    ADD INDEX `fk_item_1_produto_idproduto_idx` (`idproduto` ASC) VISIBLE;
;
ALTER TABLE `food`.`item`
    ADD CONSTRAINT `fk_item_1_produto_idproduto`
        FOREIGN KEY (`idproduto`)
            REFERENCES `food`.`produto` (`idproduto`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
