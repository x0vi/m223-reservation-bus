import java.sql.*;

import db.DatabaseConnection;

public class Main {
 
    public static void main(String[] args) {

        try {
            // 1. Obtenir la connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();

            // 1. Lecture des données
            lireVehicules(connection);
            lireEmployes(connection);

            // 2. Modification des données
            String plaque = assurerVehiculeExistant(connection);
            int idEmploye = assurerEmployeExistant(connection);

            // 3. Lecture après modification
            lireReservations(connection);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'accès à la base de données");
            e.printStackTrace();
        }

        System.out.println("=== FIN DE L'APPLICATION ===");
    }
 
    // -------------------------
    // LECTURES
    // -------------------------
 
    private static void lireVehicules(Connection connection) throws SQLException {
        System.out.println("\n--- Véhicules ---");
 
        String sql = "SELECT plaque, marque, modele, capacite_max FROM t_vehicule";
 
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
 
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("Plaque=%s | Marque=%s | Modèle=%s | Capacité max=%d%n",
                        rs.getString("plaque"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("capacite_max"));
            }
 
            if (!any) System.out.println("(Aucun véhicule)");
        }
    }
 
    private static void lireEmployes(Connection connection) throws SQLException {
        System.out.println("\n--- Employés ---");
 
        String sql = "SELECT id_employe, nom, prenom FROM t_employe";
 
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
 
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("ID=%d | Nom=%s | Prénom=%s%n",
                        rs.getInt("id_employe"),
                        rs.getString("nom"),
                        rs.getString("prenom"));
            }
 
            if (!any) System.out.println("(Aucun employé)");
        }
    }
 
    private static void lireReservations(Connection connection) throws SQLException {
        System.out.println("\n--- Réservations ---");
 
        String sql = """
                SELECT r.id_reservation, r.date_reservation,
                       v.plaque, v.marque, v.modele,
                       e.id_employe, e.nom, e.prenom
                FROM t_reservation r
                JOIN t_vehicule v ON v.plaque = r.plaque
                JOIN t_employe e ON e.id_employe = r.id_employe
                ORDER BY r.id_reservation DESC
                """;
 
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
 
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("Res#%d | Date=%s | Véhicule=%s (%s %s) | Employé=%d (%s %s)%n",
                        rs.getInt("id_reservation"),
                        rs.getDate("date_reservation"),
                        rs.getString("plaque"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("id_employe"),
                        rs.getString("nom"),
                        rs.getString("prenom"));
            }
 
            if (!any) System.out.println("(Aucune réservation)");
        }
    }
 
    // -------------------------
    // ÉCRITURES
    // -------------------------
 
    private static void creerReservation(Connection connection, String plaque, int idEmploye) throws SQLException {
        System.out.println("\n--- Création réservation ---");
 
        String sql = "INSERT INTO t_reservation (date_reservation, plaque, id_employe) VALUES (?, ?, ?)";
 
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
 
            // date du jour
            stmt.setDate(1, new Date(System.currentTimeMillis()));
            stmt.setString(2, plaque);
            stmt.setInt(3, idEmploye);
 
            int lignes = stmt.executeUpdate();
            System.out.println("Insertion effectuée (" + lignes + " ligne(s)).");
 
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("Nouvelle réservation id = " + keys.getInt(1));
                }
            }
        }
    }
 
    // -------------------------
    // HELPERS : pour garantir que l'exemple marche même si tables vides
    // -------------------------
 
    private static String assurerVehiculeExistant(Connection connection) throws SQLException {
        // On prend un véhicule existant sinon on en crée un.
        String select = "SELECT plaque FROM t_vehicule LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(select);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString("plaque");
        }
 
        System.out.println("\n(Aucun véhicule trouvé -> insertion d'un véhicule de test)");
 
        String insert = "INSERT INTO t_vehicule (plaque, marque, modele, capacite_max) VALUES (?, ?, ?, ?)";
        String plaqueTest = "GE12345"; // <= 8 caractères
 
        try (PreparedStatement stmt = connection.prepareStatement(insert)) {
            stmt.setString(1, plaqueTest);
            stmt.setString(2, "Toyota");
            stmt.setString(3, "Yaris");
            stmt.setInt(4, 5);
 
            stmt.executeUpdate();
        }
 
        return plaqueTest;
    }
 
    private static int assurerEmployeExistant(Connection connection) throws SQLException {
        // On prend un employé existant sinon on en crée un.
        String select = "SELECT id_employe FROM t_employe LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(select);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt("id_employe");
        }
 
        System.out.println("\n(Aucun employé trouvé -> insertion d'un employé de test)");
 
        String insert = "INSERT INTO t_employe (nom, prenom) VALUES (?, ?)";
 
        try (PreparedStatement stmt = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Dupont");
            stmt.setString(2, "Alice");
            stmt.executeUpdate();
 
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
 
        throw new SQLException("Impossible de créer un employé de test.");
    }
}