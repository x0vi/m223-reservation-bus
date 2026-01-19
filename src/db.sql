START TRANSACTION;

CREATE TABLE IF NOT EXISTS `employe` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `nom` VARCHAR(30),
    `prenom` VARCHAR(30),
    CONSTRAINT `pk_table_1_id` PRIMARY KEY (`id`)
);



CREATE TABLE IF NOT EXISTS `vehicule` (
    `plaques` VARCHAR(8) NOT NULL,
    `capacite_max` INT,
    `marque` VARCHAR(50),
    `modele` VARCHAR(50),
    CONSTRAINT `pk_table_2_id` PRIMARY KEY (`plaques`)
);



CREATE TABLE IF NOT EXISTS `reservation` (
    `id` INT NOT NULL,
    `date` DATE,
    `plaques` VARCHAR(8),
    `capacite_max` INT,
    `id_employe` INT,
    CONSTRAINT `pk_table_3_id` PRIMARY KEY (`id`)
);


-- Foreign key constraints
ALTER TABLE `employe` ADD CONSTRAINT `fk_employe_id` FOREIGN KEY(`id`) REFERENCES `reservation`(`id_employe`);
ALTER TABLE `vehicule` ADD CONSTRAINT `fk_vehicule_plaques` FOREIGN KEY(`plaques`) REFERENCES `reservation`(`plaques`);
ALTER TABLE `vehicule` ADD CONSTRAINT `fk_vehicule_capacite_max` FOREIGN KEY(`capacite_max`) REFERENCES `reservation`(`capacite_max`);

COMMIT;