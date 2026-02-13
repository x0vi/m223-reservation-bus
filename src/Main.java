import model.*;
import service.*;

import java.sql.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("=== Démonstration du système de réservation de bus ===\n");

        // === Services ===
        VehiculeService vehiculeService = new VehiculeService();
        EmployeService employeService = new EmployeService();
        ReservationService reservationService = new ReservationService();

        // === Véhicules ===
        System.out.println("--- Création de véhicules ---");
        Vehicule v1 = new Vehicule("FR362122", "Audi", "S3 8Y Facelift", 5);
        Vehicule v2 = new Vehicule("FR789456", "Iveco", "Daily", 15);
        
        vehiculeService.creerVehicule(v1);
        vehiculeService.creerVehicule(v2);
        System.out.println("Véhicules créés avec succès.\n");

        // === Employés ===
        System.out.println("--- Création d'employés ---");
        Employe e1 = new Employe(1, "Dupont", "Jean");
        Employe e2 = new Employe(2, "Martin", "Marie");
        
        employeService.creerEmploye(e1);
        employeService.creerEmploye(e2);
        System.out.println("Employés créés avec succès.\n");

        // === Réservations (ID écrit en dur, faudrait reprendre l'ID car en auto-incrémenté) ===
        System.out.println("--- Création de réservations ---");
        Reservation r1 = new Reservation(1, new Date(System.currentTimeMillis()), "FR362122", 1);
        Reservation r2 = new Reservation(2, new Date(System.currentTimeMillis()), "FR789456", 2);
        
        reservationService.creerReservation(r1);
        reservationService.creerReservation(r2);
        System.out.println("Réservations créées avec succès.\n");

        // === Affichage des données (Pas obligé mais j'aime bien avoir des confirmations) ===
        System.out.println("=== Liste des véhicules ===");
        List<Vehicule> vehicules = vehiculeService.listerVehicules();
        for (Vehicule v : vehicules) {
            System.out.println("  - " + v.getPlaque() + " : " + v.getMarque() + " " + v.getModele() + " (" + v.getCapaciteMax() + " places)");
        }

        System.out.println("\n=== Liste des employés ===");
        List<Employe> employes = employeService.listerEmployes();
        for (Employe e : employes) {
            System.out.println("  - " + e.getIdEmploye() + " : " + e.getPrenom() + " " + e.getNom());
        }

        System.out.println("\n=== Liste des réservations ===");
        List<Reservation> reservations = reservationService.listerReservations();
        for (Reservation r : reservations) {
            System.out.println("  - Réservation " + r.getIdReservation() + " : Véhicule " + r.getPlaque() + ", Employé " + r.getIdEmploye() + ", Date " + r.getDateReservation());
        }

        System.out.println("\n=== Fin du programme ===");
    }
}
