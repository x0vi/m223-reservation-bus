package service;

import dao.ReservationDao;
import dao.VehiculeDao;
import dao.EmployeDao;
import db.DatabaseConnection;
import model.Reservation;
import model.Vehicule;

import java.sql.Connection;
import java.time.LocalDate;
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
    public void creerReservation(String plaque, int idEmploye, int nbPlaces) throws Exception {
        Reservation reservation = new Reservation(LocalDate.now(), plaque, idEmploye, nbPlaces);

        int capaciteMax = 0;
        // Début de transaction
        connection.setAutoCommit(false);

        try {

            // 0. Vérification de la disponibilité
            // lire vehicule (plaque)
            Vehicule vehicule = vehiculeDao.select(reservation.getPlaque());
            if(vehicule != null) {
                capaciteMax = vehicule.getCapaciteMax();
            }

            int totalDejaReserve = reservationDao.getTotalPlacesReservees(
                    reservation.getPlaque(), reservation.getDateReservation());

            if (totalDejaReserve + reservation.getNbPlaces() > capaciteMax) {
                reservation.setDisponibilite(false);
                connection.rollback();
                throw new Exception("Réservation refusée : capacité maximale du véhicule "
                        + reservation.getPlaque() + " atteinte pour la date "
                        + reservation.getDateReservation() + ". Disponibilité : false.");
            }

            reservation.setDisponibilite(true);

            // 1. Insérer la réservation
            reservationDao.insert(reservation);

            // 2. Tout s'est bien passé
            connection.commit();

        } catch (Exception e) {

            // Un problème est survenu -> retour à l'état initial
            connection.rollback();

            // Relancer l'exception originale pour que l'appelant reçoive le message précis
            throw e;

        } finally {
            
            // Retour au mode normal
            connection.setAutoCommit(true);
        }                
    }

    public List<Reservation> listerReservations() throws Exception {
        return reservationDao.selectAll();
    }
}
