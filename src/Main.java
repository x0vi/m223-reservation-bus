import model.*;
import service.ReservationService;

import java.sql.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("=== Système de réservation de bus ===\n");

        ReservationService reservationService = new ReservationService();

        // === Création de réservations ===
        System.out.println("--- Création de réservations ---");
        Reservation r1 = new Reservation(0, new Date(System.currentTimeMillis()), "FR362122", 1);
        Reservation r2 = new Reservation(0, new Date(System.currentTimeMillis()), "FR789456", 2);
        
        reservationService.creerReservation(r1);
        reservationService.creerReservation(r2);
        System.out.println("Réservations créées avec succès.\n");

        // === Affichage des réservations ===
        System.out.println("=== Liste des réservations ===");
        List<Reservation> reservations = reservationService.listerReservations();
        for (Reservation r : reservations) {
            System.out.println("  - Réservation " + r.getIdReservation() + 
                " : Véhicule " + r.getPlaque() + 
                ", Employé " + r.getIdEmploye() + 
                ", Date " + r.getDateReservation());
        }

        System.out.println("\n=== Fin du programme ===");
    }
}
