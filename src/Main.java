import model.*;
import service.ReservationService;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws Exception {

        LocalDate dateRes = LocalDate.now();
        Thread utilisateur1 = new Thread(() -> {
            try {
                ReservationService service = new ReservationService();
                service.creerReservation(new Reservation(0, dateRes, "FR362122", 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread utilisateur2 = new Thread(() -> {
            try {
                ReservationService service = new ReservationService();
                service.creerReservation(new Reservation(0, dateRes, "FR362122", 2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        utilisateur1.start();
        System.out.println("Thread user1 démarré");
        Thread.sleep(1000);
        utilisateur2.start();
        System.out.println("Thread user2 démarré");
    }
}
