package dao;
 
import model.Employe;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class EmployeDao {
 
    // ===== SELECT =====
    public List<Employe> selectAll(Connection connection) throws SQLException {
        String sql = "SELECT idEmploye, nom, prenom FROM t_employe";
        List<Employe> employes = new ArrayList<>();
 
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                employes.add(new Employe(
                        rs.getInt("idEmploye"),
                        rs.getString("nom"),
                        rs.getString("prenom")
                ));
            }
        }
        return employes;
    }
 
    // ===== INSERT =====
    public void insert(Connection connection, Employe e) throws SQLException {
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
    public void update(Connection connection, Employe e) throws SQLException {
        String sql = """
            UPDATE t_employe
            SET nom = ?, prenom = ?
            WHERE idEmploye = ?
        """;
 
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setInt(3, e.getIdEmploye());
            ps.executeUpdate();
        }
    }
}