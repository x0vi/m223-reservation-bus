import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionTest {
    private static final String URL = "jdbc:mysql://localhost:22306/gbc";
    private static final String USER = "projet_user";
    private static final String PASSWORD = "projet_pwd";

    public static void main(String[] args) {
        System.out.println("Tentative de connexion à la base de données...");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connexion réussie !");
            System.out.println("Base de données : " + connection.getCatalog());
            } 
        catch (SQLException e) {
            System.out.println("Échec de la connexion !");
            System.out.println("Message : " + e.getMessage());
        }
    }
}