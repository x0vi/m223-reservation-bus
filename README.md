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

# Pour windows (PowerShell)
javac -cp "lib/*" -d out (Get-ChildItem -Path src -Filter "*.java" -Recurse).FullName
java -cp "out;lib/*" JdbcConnectionTest
```

**Note** : Sur Linux, utilisez `:` comme séparateur de classpath. Sur Windows, utilisez `;`.

## Configuration

- **MySQL** : localhost:22306
- **Base de données** : `gbc`
- **Utilisateur** : `projet_user`
- **Mot de passe** : `projet_pwd`

## Java - Architecture

Le projet suit une architecture multi-couche simple :

- **Couche Présentation** : [`Main.java`](src/Main.java) — point d'entrée, affichage des résultats
- **Couche Modèle** : [`src/model/`](src/model/) — objets simples transportant des données
- **Couche Persistance** : [`src/dao/`](src/dao/) — accès à la base de données (SELECT, INSERT, UPDATE)
- **Connexion DB** : [`src/db/`](src/db/) — gestion de la connexion à la base de données

### 1. Classes de Modèle (`src/model/`)

Objets simples transportant des données. Attributs privés, constructeur, getters.

| Classe | Table SQL | Attributs |
|--------|-----------|-----------|
| [`Vehicule`](src/model/Vehicule.java) | `t_vehicule` | `plaque`, `marque`, `modele`, `capaciteMax` |
| [`Employe`](src/model/Employe.java) | `t_employe` | `idEmploye`, `nom`, `prenom` |
| [`Reservation`](src/model/Reservation.java) | `t_reservation` | `idReservation`, `dateReservation`, `plaque`, `idEmploye` |

### 2. Classes DAO (`src/dao/`)

Les classes DAO (Data Access Object) encapsulent l'accès à la base de données. Chaque méthode reçoit la `Connection` en paramètre. Elles contiennent les requêtes SQL et retournent des objets du modèle.

| Classe | Méthodes |
|--------|----------|
| [`VehiculeDao`](src/dao/VehiculeDao.java) | `selectAll()`, `insert()`, `update()` |
| [`EmployeDao`](src/dao/EmployeDao.java) | `selectAll()`, `insert()`, `update()` |
| [`ReservationDao`](src/dao/ReservationDao.java) | `selectAll()`, `insert()`, `update()` |

**Caractéristiques des DAO :**
- Pas de constructeur spécial (instanciation simple)
- Chaque méthode reçoit `Connection` en paramètre
- Méthodes utilisant `PreparedStatement` pour éviter les injections SQL
- Gestion automatique des `ResultSet` avec try-with-resources

### Template pour ajouter une nouvelle classe de modèle

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

### Template pour ajouter un nouveau DAO

```java
package dao;

import model.NomClasse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NomClasseDao {

    // ===== SELECT =====
    public List<NomClasse> selectAll(Connection connection) throws SQLException {
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
    public void insert(Connection connection, NomClasse obj) throws SQLException {
        String sql = "INSERT INTO t_table(id, nom) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, obj.getId());
            ps.setString(2, obj.getNom());
            ps.executeUpdate();
        }
    }

    // ===== UPDATE =====
    public void update(Connection connection, NomClasse obj) throws SQLException {
        String sql = "UPDATE t_table SET nom = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, obj.getNom());
            ps.setInt(2, obj.getId());
            ps.executeUpdate();
        }
    }
}
```

**Conventions :**
- Modèles dans [`src/model/`](src/model/) — attributs privés, getters uniquement
- DAO dans [`src/dao/`](src/dao/) — chaque méthode reçoit `Connection` en paramètre
- Utiliser `PreparedStatement` pour toutes les requêtes paramétrées
- Fermer les ressources avec try-with-resources