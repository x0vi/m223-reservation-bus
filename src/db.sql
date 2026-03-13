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
    nb_places INT NOT NULL,

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

-- Données initiales : Véhicules
INSERT INTO t_vehicule (plaque, marque, modele, capacite_max) VALUES
    ('FR362122', 'Audi', 'S3', 5),
    ('FR789456', 'Iveco', 'Daily', 15),
    ('FR111222', 'Volvo', '9700', 50);

-- Données initiales : Employés
INSERT INTO t_employe (nom, prenom) VALUES
    ('Dupont', 'Jean'),
    ('Martin', 'Marie'),
    ('Bernard', 'Pierre');

-- Données initiales : Réservations
INSERT INTO t_reservation (date_reservation, plaque, id_employe, nb_places) VALUES
    ('2023-01-01', 'FR362122', 1, 4),
    ('2023-01-02', 'FR789456', 2, 10),
    ('2023-01-03', 'FR111222', 3, 20);

COMMIT;
