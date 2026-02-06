package dao;

import model.Employe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDao {

    private Connection connection;

    public EmployeDao(Connection connection) {
        this.connection = connection;
    }

    // ===== SELECT =====
    public List<Employe> selectAll() throws SQLException {
        String sql = "SELECT id_employe, nom, prenom FROM t_employe";
        List<Employe> employes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employes.add(new Employe(
                        rs.getInt("id_employe"),
                        rs.getString("nom"),
                        rs.getString("prenom")
                ));
            }
        }
        return employes;
    }

    // ===== INSERT =====
    public void insert(Employe e) throws SQLException {
        String sql = """
            INSERT INTO t_employe(nom, prenom)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.executeUpdate();
        }
    }

    // ===== UPDATE =====
    public void update(Employe e) throws SQLException {
        String sql = """
            UPDATE t_employe
            SET nom = ?, prenom = ?
            WHERE id_employe = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setInt(3, e.getIdEmploye());
            ps.executeUpdate();
        }
    }
}
