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

## Java - Classes de Modèle

Les classes de modèle se trouvent dans le dossier [`src/model/`](src/model/) et correspondent aux tables de la base de données. Chaque classe représente une entité avec ses attributs, constructeur et getters.

### Classes existantes

| Classe | Table SQL | Attributs |
|--------|-----------|-----------|
| [`Vehicule`](src/model/Vehicule.java) | `t_vehicule` | `plaque`, `marque`, `modele`, `capaciteMax` |
| [`Employe`](src/model/Employe.java) | `t_employe` | `idEmploye`, `nom`, `prenom` |
| [`Reservation`](src/model/Reservation.java) | `t_reservation` | `idReservation`, `dateReservation`, `plaque`, `idEmploye` |

### Template pour ajouter une nouvelle classe

Pour créer une nouvelle classe de modèle correspondant à une table SQL, suivez ce template :

```java
package model;

// Ajoutez les imports nécessaires (ex: java.sql.Date)

public class NomClasse {
    // Attributs privés correspondant aux colonnes SQL
    private int id;
    private String nom;
    private String description;
    
    // Constructeur avec tous les paramètres
    public NomClasse(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }
    
    // Getters pour accéder aux attributs
    public int getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getDescription() {
        return description;
    }
}
```

**Conventions à suivre :**
- Le fichier doit être placé dans [`src/model/`](src/model/)
- Le nom de la classe doit correspondre au nom du fichier (ex: `MaClasse.java` → `public class MaClasse`)
- Les attributs doivent être `private`
- Créer un constructeur avec tous les paramètres
- Créer des getters pour tous les attributs
- Utiliser le package `model;` en début de fichier
- Pour les dates SQL, utiliser `java.sql.Date`