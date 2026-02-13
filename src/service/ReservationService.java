package service;

import dao.ReservationDao;
import dao.VehiculeDao;
import dao.EmployeDao;
import db.DatabaseConnection;
import model.Reservation;

import java.sql.Connection;
import java.util.List;

public class ReservationService {

    private ReservationDao reservationDao;
    private VehiculeDao vehiculeDao;
    private EmployeDao employeDao;
    private Connection connection;

    public ReservationService() throws Exception {
        this.connection = DatabaseConnection.getConnection();
        this.reservationDao = new ReservationDao(connection);
        this.vehiculeDao = new VehiculeDao(connection);
        this.employeDao = new EmployeDao(connection);
    }

    /**
     * Action métier transactionnelle :
     * - créer une réservation
     */
    public void creerReservation(Reservation reservation) throws Exception {
        
        // Début de transaction
        connection.setAutoCommit(false);

        try {

            // 1. Insérer la réservation
            reservationDao.insert(reservation);

            // 2. Tout s'est bien passé
            connection.commit();

        } catch (Exception e) {

            // Un problème est survenu -> retour à l'état initial
            connection.rollback();

            throw new Exception(
                "La réservation n'a pas pu être enregistrée. "
              + "Aucune donnée n'a été sauvegardée."
            );

        } finally {
            
            // Retour au mode normal
            connection.setAutoCommit(true);
        }                
    }

    public List<Reservation> listerReservations() throws Exception {
        return reservationDao.selectAll();
    }
}
