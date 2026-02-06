package service;

import dao.ReservationDao;
import dao.VehiculeDao;
import dao.EmployeDao;
import db.DatabaseConnection;
import model.Reservation;
import model.Vehicule;
import model.Employe;

import java.sql.Connection;
import java.util.List;

public class ReservationService {

    private ReservationDao reservationDao;
    private VehiculeDao vehiculeDao;
    private EmployeDao employeDao;

    public ReservationService() throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        this.reservationDao = new ReservationDao(connection);
        this.vehiculeDao = new VehiculeDao(connection);
        this.employeDao = new EmployeDao(connection);
    }

    public void creerReservation(Reservation reservation) throws Exception {
        reservationDao.insert(reservation);
    }

    public List<Reservation> listerReservations() throws Exception {
        return reservationDao.selectAll();
    }
}
