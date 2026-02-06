package service;

import dao.VehiculeDao;
import db.DatabaseConnection;
import model.Vehicule;

import java.sql.Connection;
import java.util.List;

public class VehiculeService {

    private VehiculeDao vehiculeDao;

    public VehiculeService() throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        this.vehiculeDao = new VehiculeDao(connection);
    }

    public List<Vehicule> listerVehicules() throws Exception {
        return vehiculeDao.selectAll();
    }

    public void creerVehicule(Vehicule vehicule) throws Exception {
        vehiculeDao.insert(vehicule);
    }

    public void modifierVehicule(Vehicule vehicule) throws Exception {
        vehiculeDao.update(vehicule);
    }
}
