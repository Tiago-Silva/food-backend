ALTER TABLE `food`.`users`
    ADD COLUMN `pais` VARCHAR(45) NULL AFTER `email`,
    ADD COLUMN `estado` VARCHAR(45) NULL AFTER `pais`,
    ADD COLUMN `cidade` VARCHAR(45) NULL AFTER `estado`,
    ADD COLUMN `cep` VARCHAR(45) NULL AFTER `cidade`,
    CHANGE COLUMN `endereco` `endereco` VARCHAR(45) NULL DEFAULT NULL AFTER `cep`;