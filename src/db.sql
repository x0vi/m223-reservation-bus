START TRANSACTION;
USE gbc;

CREATE TABLE IF NOT EXISTS t_vehicule (
    plaque VARCHAR(8) PRIMARY KEY,
    marque VARCHAR(50) ,
    modele VARCHAR(50) ,
    capacite_max INT (2)
);


CREATE TABLE IF NOT EXISTS t_employe (
    id_employe INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(30) NOT NULL,
    prenom VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_reservation (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    date_reservation DATE NOT NULL,
    plaque VARCHAR(8) NOT NULL,
    id_employe INT NOT NULL,

    FOREIGN KEY (plaque)
        REFERENCES t_vehicule(plaque)
        ON DELETE RESTRICT
        /*si le véhicules est dans une reservation il ne peut pas etre supprimé*/
        ON UPDATE CASCADE,

    FOREIGN KEY (id_employe)
        REFERENCES t_employe(id_employe)
        ON DELETE RESTRICT
        /*si l'employé est dans une reservation il ne peut pas etre supprimé*/
        ON UPDATE CASCADE
);
COMMIT;