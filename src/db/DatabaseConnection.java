package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Paramètres de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:22306/gbc";
    private static final String USER = "projet_user";
    private static final String PASSWORD = "projet_pwd";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
    }
}