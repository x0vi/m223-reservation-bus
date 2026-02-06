package dao;
 
import model.Reservation;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class ReservationDao {
 
    // ===== SELECT =====
    public List<Reservation> selectAll(Connection connection) throws SQLException {
        String sql = """
            SELECT idReservation, dateReservation, plaque, idEmploye
            FROM t_reservation
        """;
 
        List<Reservation> reservations = new ArrayList<>();
 
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("idReservation"),
                        rs.getDate("dateReservation"),
                        rs.getString("plaque"),
                        rs.getInt("idEmploye")
                ));
            }
        }
        return reservations;
    }
 
    // ===== INSERT =====
    public void insert(Connection connection, Reservation r) throws SQLException {
        String sql = """
            INSERT INTO t_reservation(dateReservation, plaque, idEmploye)
            VALUES (?, ?, ?)
        """;
 
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, r.getDateReservation());
            ps.setString(2, r.getPlaque());
            ps.setInt(3, r.getIdEmploye());
            ps.executeUpdate();
        }
    }
 
    // ===== UPDATE =====
    public void update(Connection connection, Reservation r) throws SQLException {
        String sql = """
            UPDATE t_reservation
            SET dateReservation = ?, plaque = ?, idEmploye = ?
            WHERE idReservation = ?
        """;
 
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, r.getDateReservation());
            ps.setString(2, r.getPlaque());
            ps.setInt(3, r.getIdEmploye());
            ps.setInt(4, r.getIdReservation());
            ps.executeUpdate();
        }
    }
}