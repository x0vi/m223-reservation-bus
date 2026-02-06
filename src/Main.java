import java.sql.*;
import java.util.List;

import db.DatabaseConnection;
import dao.VehiculeDao;
import dao.EmployeDao;
import dao.ReservationDao;
import model.Vehicule;
import model.Employe;
import model.Reservation;

public class Main {
 
    public static void main(String[] args) {

        try {
            // 1. Obtenir la connexion à la base de données
            Connection connection = DatabaseConnection.getConnection();

            // 2. Créer les DAO
            VehiculeDao vehiculeDao = new VehiculeDao();
            EmployeDao employeDao = new EmployeDao();
            ReservationDao reservationDao = new ReservationDao();

            // 3. Lecture des données
            lireVehicules(connection, vehiculeDao);
            lireEmployes(connection, employeDao);

            // 4. Modification des données
            String plaque = assurerVehiculeExistant(connection, vehiculeDao);
            int idEmploye = assurerEmployeExistant(connection, employeDao);

            // 5. Lecture après modification
            lireReservations(connection, reservationDao);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'accès à la base de données");
            e.printStackTrace();
        }

        System.out.println("=== FIN DE L'APPLICATION ===");
    }
 
    // -------------------------
    // LECTURES
    // -------------------------
 
    private static void lireVehicules(Connection connection, VehiculeDao dao) throws SQLException {
        System.out.println("\n--- Véhicules ---");

        List<Vehicule> vehicules = dao.selectAll(connection);

        if (vehicules.isEmpty()) {
            System.out.println("(Aucun véhicule)");
        } else {
            for (Vehicule v : vehicules) {
                System.out.printf("Plaque=%s | Marque=%s | Modèle=%s | Capacité max=%d%n",
                        v.getPlaque(), v.getMarque(), v.getModele(), v.getCapaciteMax());
            }
        }
    }
 
    private static void lireEmployes(Connection connection, EmployeDao dao) throws SQLException {
        System.out.println("\n--- Employés ---");

        List<Employe> employes = dao.selectAll(connection);

        if (employes.isEmpty()) {
            System.out.println("(Aucun employé)");
        } else {
            for (Employe e : employes) {
                System.out.printf("ID=%d | Nom=%s | Prénom=%s%n",
                        e.getIdEmploye(), e.getNom(), e.getPrenom());
            }
        }
    }
 
    private static void lireReservations(Connection connection, ReservationDao dao) throws SQLException {
        System.out.println("\n--- Réservations ---");

        List<Reservation> reservations = dao.selectAll(connection);

        if (reservations.isEmpty()) {
            System.out.println("(Aucune réservation)");
        } else {
            for (Reservation r : reservations) {
                System.out.printf("Res#%d | Date=%s | Plaque=%s | Employé=%d%n",
                        r.getIdReservation(),
                        r.getDateReservation(),
                        r.getPlaque(),
                        r.getIdEmploye());
            }
        }
    }
 
    // -------------------------
    // HELPERS : pour garantir que l'exemple marche même si tables vides
    // -------------------------
 
    private static String assurerVehiculeExistant(Connection connection, VehiculeDao dao) throws SQLException {
        List<Vehicule> vehicules = dao.selectAll(connection);
        if (!vehicules.isEmpty()) return vehicules.get(0).getPlaque();

        System.out.println("\n(Aucun véhicule trouvé -> insertion d'un véhicule de test)");

        Vehicule test = new Vehicule("GE12345", "Toyota", "Yaris", 5);
        dao.insert(connection, test);

        return test.getPlaque();
    }
 
    private static int assurerEmployeExistant(Connection connection, EmployeDao dao) throws SQLException {
        List<Employe> employes = dao.selectAll(connection);
        if (!employes.isEmpty()) return employes.get(0).getIdEmploye();

        System.out.println("\n(Aucun employé trouvé -> insertion d'un employé de test)");

        Employe test = new Employe(0, "Dupont", "Alice");
        dao.insert(connection, test);

        // Re-read to get the generated ID
        employes = dao.selectAll(connection);
        if (!employes.isEmpty()) return employes.get(0).getIdEmploye();

        throw new SQLException("Impossible de créer un employé de test.");
    }
}
