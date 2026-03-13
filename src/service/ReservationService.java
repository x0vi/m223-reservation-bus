package service;

import dao.ReservationDao;
import dao.VehiculeDao;
import dao.EmployeDao;
import db.DatabaseConnection;
import model.Reservation;
import model.Vehicule;

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

    /**
     * Action concurrente transactionnelle :
     * Simule deux utilisateurs tentant de réserver le même véhicule en même temps.
     * - Vérifie la disponibilité du véhicule
     * - Crée la réservation si disponible
     * - Simule une transaction longue (Thread.sleep) pour observer la concurrence
     */
    public void actionConcurrente(Reservation reservation) throws Exception {

        /**
         * Début de la transaction.
         * Le mode autocommit est désactivé afin de contrôler explicitement
         * quand la transaction est validée (COMMIT) ou annulée (ROLLBACK).
         */
        connection.setAutoCommit(false);

        try {

            /**
             * Étape 1 : vérifier l'état de la ressource (disponibilité du véhicule).
             */
            System.out.println("Employé " + reservation.getIdEmploye() + " : vérification du véhicule " + reservation.getPlaque());

            Vehicule vehicule = vehiculeDao.select(reservation.getPlaque());
            int capaciteMax = (vehicule != null) ? vehicule.getCapaciteMax() : 0;
            int totalDejaReserve = reservationDao.getTotalPlacesReservees(reservation.getPlaque(), reservation.getDateReservation());
            boolean disponible = totalDejaReserve < capaciteMax;

            System.out.println("Employé " + reservation.getIdEmploye() + " : places disponibles = " + disponible
                    + " (" + totalDejaReserve + "/" + capaciteMax + ")");

            if (disponible) {

                /**
                 * Étape 2 : créer la réservation.
                 */
                System.out.println("Employé " + reservation.getIdEmploye() + " : création de la réservation");

                reservationDao.insert(reservation);

                /**
                 * Pause volontaire pour simuler une transaction longue.
                 * Cela permet d'observer le comportement concurrent avec le second utilisateur.
                 */
                System.out.println("Employé " + reservation.getIdEmploye() + " : transaction en attente (simulation...)");
                Thread.sleep(5000);

                /**
                 * Validation de la transaction.
                 */
                connection.commit();
                System.out.println("Employé " + reservation.getIdEmploye() + " : COMMIT transaction confirmé");

            } else {

                /**
                 * Si le véhicule est complet, aucune modification n'est faite.
                 */
                System.out.println("Employé " + reservation.getIdEmploye() + " : véhicule complet — aucune réservation créée");
                connection.rollback();
            }

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Reservation> listerReservations() throws Exception {
        return reservationDao.selectAll();
    }
}
