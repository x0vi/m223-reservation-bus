package dao;
 
import model.Vehicule;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class VehiculeDao {
 
    // ===== SELECT =====
    public List<Vehicule> selectAll(Connection connection) throws SQLException {
        String sql = "SELECT plaque, marque, modele, capaciteMax FROM t_vehicule";
        List<Vehicule> vehicules = new ArrayList<>();
 
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                vehicules.add(new Vehicule(
                        rs.getString("plaque"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("capaciteMax")
                ));
            }
        }
        return vehicules;
    }
 
    // ===== INSERT =====
    public void insert(Connection connection, Vehicule v) throws SQLException {
        String sql = """
            INSERT INTO t_vehicule(plaque, marque, modele, capaciteMax)
            VALUES (?, ?, ?, ?)
        """;
 
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, v.getPlaque());
            ps.setString(2, v.getMarque());
            ps.setString(3, v.getModele());
            ps.setInt(4, v.getCapaciteMax());
            ps.executeUpdate();
        }
    }
 
    // ===== UPDATE =====
    public void update(Connection connection, Vehicule v) throws SQLException {
        String sql = """
            UPDATE t_vehicule
            SET marque = ?, modele = ?, capaciteMax = ?
            WHERE plaque = ?
        """;
 
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, v.getMarque());
            ps.setString(2, v.getModele());
            ps.setInt(3, v.getCapaciteMax());
            ps.setString(4, v.getPlaque());
            ps.executeUpdate();
        }
    }
}