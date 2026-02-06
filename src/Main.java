import service.*;
import model.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            // 1. Instancier les services (chaque service gère sa propre connexion)
            VehiculeService vehiculeService = new VehiculeService();
            EmployeService employeService = new EmployeService();
            ReservationService reservationService = new ReservationService();

            // 2. Lecture des données
            lireVehicules(vehiculeService);
            lireEmployes(employeService);

            // 3. Modification des données
            assurerVehiculeExistant(vehiculeService);
            assurerEmployeExistant(employeService);

            // 4. Lecture après modification
            lireReservations(reservationService);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès à la base de données");
            e.printStackTrace();
        }

        System.out.println("=== FIN DE L'APPLICATION ===");
    }

    // -------------------------
    // LECTURES
    // -------------------------

    private static void lireVehicules(VehiculeService service) throws Exception {
        System.out.println("\n--- Véhicules ---");

        List<Vehicule> vehicules = service.listerVehicules();

        if (vehicules.isEmpty()) {
            System.out.println("(Aucun véhicule)");
        } else {
            for (Vehicule v : vehicules) {
                System.out.printf("Plaque=%s | Marque=%s | Modèle=%s | Capacité max=%d%n",
                        v.getPlaque(), v.getMarque(), v.getModele(), v.getCapaciteMax());
            }
        }
    }

    private static void lireEmployes(EmployeService service) throws Exception {
        System.out.println("\n--- Employés ---");

        List<Employe> employes = service.listerEmployes();

        if (employes.isEmpty()) {
            System.out.println("(Aucun employé)");
        } else {
            for (Employe e : employes) {
                System.out.printf("ID=%d | Nom=%s | Prénom=%s%n",
                        e.getIdEmploye(), e.getNom(), e.getPrenom());
            }
        }
    }

    private static void lireReservations(ReservationService service) throws Exception {
        System.out.println("\n--- Réservations ---");

        List<Reservation> reservations = service.listerReservations();

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

    private static void assurerVehiculeExistant(VehiculeService service) throws Exception {
        List<Vehicule> vehicules = service.listerVehicules();
        if (!vehicules.isEmpty()) return;

        System.out.println("\n(Aucun véhicule trouvé -> insertion d'un véhicule de test)");
        Vehicule test = new Vehicule("GE12345", "Toyota", "Yaris", 5);
        service.creerVehicule(test);
    }

    private static void assurerEmployeExistant(EmployeService service) throws Exception {
        List<Employe> employes = service.listerEmployes();
        if (!employes.isEmpty()) return;

        System.out.println("\n(Aucun employé trouvé -> insertion d'un employé de test)");
        Employe test = new Employe(0, "Dupont", "Alice");
        service.creerEmploye(test);
    }
}
