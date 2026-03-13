# m223-reservation-bus

## Prérequis

- Docker & Docker Compose
- Java JDK (pour compiler et exécuter le code Java)

## Démarrage

### 1. Lancer les conteneurs Docker

```bash
cd reservation-bus
docker-compose up -d
```

### 2. Accéder à phpMyAdmin

Ouvrez votre navigateur et allez à : [http://localhost:22380](http://localhost:22380)

- **Serveur** : `m223-db`
- **Nom d'utilisateur** : `root`
- **Mot de passe** : `root_pwd`

### 3. Compiler et exécuter le code Java

```bash
cd reservation-bus

# Pour linux
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" Main

# Pour windows (PowerShell)
javac -cp "lib/*" -d out (Get-ChildItem -Path src -Filter "*.java" -Recurse).FullName
java -cp "out;lib/*" Main
```

**Note** : Sur Linux, utilisez `:` comme séparateur de classpath. Sur Windows, utilisez `;`.

## Configuration

- **MySQL** : localhost:22306
- **Base de données** : `gbc`
- **Utilisateur** : `projet_user`
- **Mot de passe** : `projet_pwd`

## Java - Architecture

Le projet suit une architecture multi-couche :

- **Couche Présentation** : [`Main.java`](src/Main.java) — point d'entrée, affichage des résultats
- **Couche Métier (Service)** : [`src/service/`](src/service/) — logique applicative, coordination des actions
- **Couche Persistance (DAO)** : [`src/dao/`](src/dao/) — accès à la base de données (SELECT, INSERT, UPDATE)
- **Couche Modèle** : [`src/model/`](src/model/) — objets simples transportant des données
- **Connexion DB** : [`src/db/`](src/db/) — gestion de la connexion à la base de données

### 1. Classes de Modèle (`src/model/`)

Objets simples transportant des données. Attributs privés, constructeur, getters.

| Classe | Table SQL | Attributs |
|--------|-----------|-----------|
| [`Vehicule`](src/model/Vehicule.java) | `t_vehicule` | `plaque`, `marque`, `modele`, `capaciteMax` |
| [`Employe`](src/model/Employe.java) | `t_employe` | `idEmploye`, `nom`, `prenom` |
| [`Reservation`](src/model/Reservation.java) | `t_reservation` | `dateReservation`, `plaque`, `idEmploye`, `nbPlaces`, `disponibilite` — `idReservation` géré par AUTO_INCREMENT |

### 2. Classes DAO (`src/dao/`)

Les classes DAO (Data Access Object) encapsulent l'accès à la base de données. Elles reçoivent la `Connection` dans leur constructeur et contiennent les requêtes SQL.

| Classe | Méthodes |
|--------|----------|
| [`VehiculeDao`](src/dao/VehiculeDao.java) | `selectAll()`, `select()`, `insert()`, `update()` |
| [`EmployeDao`](src/dao/EmployeDao.java) | `selectAll()`, `insert()`, `update()` |
| [`ReservationDao`](src/dao/ReservationDao.java) | `selectAll()`, `insert()`, `update()`, `getTotalPlacesReservees()` |

**Caractéristiques des DAO :**
- Constructeur prenant `Connection` en paramètre
- Méthodes utilisant `PreparedStatement` pour éviter les injections SQL
- Gestion automatique des `ResultSet` avec try-with-resources

### 3. Classes Service (`src/service/`)

Les classes Service coordonnent les actions et contiennent la logique métier. Elles utilisent les DAO pour accéder aux données.

| Classe | Responsabilités |
|--------|-----------------|
| [`VehiculeService`](src/service/VehiculeService.java) | Gestion des véhicules |
| [`EmployeService`](src/service/EmployeService.java) | Gestion des employés |
| [`ReservationService`](src/service/ReservationService.java) | Gestion des réservations — `creerReservation()`, `actionConcurrente()`, `listerReservations()` |

**Caractéristiques des Services :**
- Constructeur sans paramètre qui obtient la `Connection` via `DatabaseConnection`
- Instancie les DAO nécessaires avec la `Connection`
- Contient la logique métier (validation, coordination de plusieurs DAO, etc.)

### Templates

#### Template pour une nouvelle classe de modèle

```java
package model;

public class NomClasse {
    private int id;
    private String nom;
    
    public NomClasse(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }
    
    public int getId() { return id; }
    public String getNom() { return nom; }
}
```

#### Template pour un nouveau DAO

```java
package dao;

import model.NomClasse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NomClasseDao {

    private Connection connection;

    public NomClasseDao(Connection connection) {
        this.connection = connection;
    }

    // ===== SELECT =====
    public List<NomClasse> selectAll() throws SQLException {
        String sql = "SELECT id, nom FROM t_table";
        List<NomClasse> liste = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(new NomClasse(
                    rs.getInt("id"),
                    rs.getString("nom")
                ));
            }
        }
        return liste;
    }

    // ===== INSERT =====
    public void insert(NomClasse obj) throws SQLException {
        String sql = "INSERT INTO t_table(nom) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, obj.getNom());
            ps.executeUpdate();
        }
    }

    // ===== UPDATE =====
    public void update(NomClasse obj) throws SQLException {
        String sql = "UPDATE t_table SET nom = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, obj.getNom());
            ps.setInt(2, obj.getId());
            ps.executeUpdate();
        }
    }
}
```

#### Template pour un nouveau Service

```java
package service;

import dao.NomClasseDao;
import db.DatabaseConnection;
import model.NomClasse;

import java.sql.Connection;
import java.util.List;

public class NomClasseService {

    private NomClasseDao dao;

    public NomClasseService() throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        this.dao = new NomClasseDao(connection);
    }

    public List<NomClasse> lister() throws Exception {
        return dao.selectAll();
    }

    public void creer(NomClasse obj) throws Exception {
        dao.insert(obj);
    }

    public void modifier(NomClasse obj) throws Exception {
        dao.update(obj);
    }
}
```

**Conventions :**
- Modèles dans [`src/model/`](src/model/) — attributs privés, **getters uniquement**, dates en `String`, IDs AUTO_INCREMENT non inclus dans le constructeur
- DAO dans [`src/dao/`](src/dao/) — reçoivent `Connection` dans le constructeur, gèrent toutes les conversions vers les types SQL
- Services dans [`src/service/`](src/service/) — obtiennent `Connection` via `DatabaseConnection`, instancient les DAO
- [`Main.java`](src/Main.java) utilise uniquement les Services, jamais les DAO directement
- Utiliser `PreparedStatement` pour toutes les requêtes paramétrées
- Fermer les ressources avec try-with-resources

### 4. Concurrence transactionnelle

La méthode `actionConcurrente(Reservation reservation)` dans `ReservationService` simule deux utilisateurs tentant de réserver le même véhicule simultanément :

1. `setAutoCommit(false)` — début de transaction explicite
2. `vehiculeDao.select()` avec `FOR UPDATE` — verrouillage de la ligne du véhicule
3. `getTotalPlacesReservees()` — vérification de la disponibilité
4. `reservationDao.insert()` — création si disponible, sinon rollback
5. `Thread.sleep(5000)` — simulation d'une transaction longue
6. `COMMIT` ou `ROLLBACK` selon le résultat
