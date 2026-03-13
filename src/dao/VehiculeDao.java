package dao;

import model.Vehicule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDao {

    private Connection connection;

    public VehiculeDao(Connection connection) {
        this.connection = connection;
    }

    // ===== SELECT =====
    public List<Vehicule> selectAll() throws SQLException {
        String sql = "SELECT plaque, marque, modele, capacite_max FROM t_vehicule";
        List<Vehicule> vehicules = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehicules.add(new Vehicule(
                        rs.getString("plaque"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("capacite_max")
                ));
            }
        }
        return vehicules;
    }

    public Vehicule select(String plaque) throws SQLException {
        String sql = """
                SELECT plaque, marque, modele, capacite_max
                FROM t_vehicule
                WHERE plaque = ?
                FOR UPDATE
                """;

        Vehicule vehicule = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, plaque);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vehicule = new Vehicule(
                        rs.getString("plaque"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("capacite_max")
                    );
                }
            }
        };

        return vehicule;
    }

    // ===== INSERT =====
    public void insert(Vehicule v) throws SQLException {
        String sql = """
            INSERT INTO t_vehicule(plaque, marque, modele, capacite_max)
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
    public void update(Vehicule v) throws SQLException {
        String sql = """
            UPDATE t_vehicule
            SET marque = ?, modele = ?, capacite_max = ?
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