package service;

import dao.EmployeDao;
import db.DatabaseConnection;
import model.Employe;

import java.sql.Connection;
import java.util.List;

public class EmployeService {

    private EmployeDao employeDao;

    public EmployeService() throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        this.employeDao = new EmployeDao(connection);
    }

    public List<Employe> listerEmployes() throws Exception {
        return employeDao.selectAll();
    }

    public void creerEmploye(Employe employe) throws Exception {
        employeDao.insert(employe);
    }

    public void modifierEmploye(Employe employe) throws Exception {
        employeDao.update(employe);
    }
}
