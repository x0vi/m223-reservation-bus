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
- **Mot de passe** : `projet_pwd`

### 3. Compiler et exécuter le code Java

```bash
cd reservation-bus
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" JdbcConnectionTest
```

**Note** : Sur Linux, utilisez `:` comme séparateur de classpath. Sur Windows, utilisez `;`.

## Configuration

- **MySQL** : localhost:22306
- **Base de données** : `projet_db`
- **Utilisateur** : `projet_user`
- **Mot de passe** : `projet_pwd`
