CREATE TABLE `food`.`pedido` (
    `idpedido` BIGINT NOT NULL AUTO_INCREMENT,
    `data` DATE NOT NULL,
    `ano` VARCHAR(5) NOT NULL,
    `mes` VARCHAR(3) NOT NULL,
    `dia` VARCHAR(3) NOT NULL,
    `hora` VARCHAR(20) NOT NULL,
    `total` DECIMAL(10,2) NULL,
    `tipo_pagamento` VARCHAR(45) NULL,
    `id` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`idpedido`),
    INDEX `fk_1_pedido_users_id_idx` (`id` ASC) VISIBLE,
    CONSTRAINT `fk_1_pedido_users_id_idx`
        FOREIGN KEY (`id`)
            REFERENCES `food`.`users` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
