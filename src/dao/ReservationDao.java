package dao;

import model.Reservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    private Connection connection;

    public ReservationDao(Connection connection) {
        this.connection = connection;
    }

    // ===== SELECT =====
    public List<Reservation> selectAll() throws SQLException {
        String sql = """
            SELECT id_reservation, date_reservation, plaque, id_employe
            FROM t_reservation
        """;

        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id_reservation"),
                        rs.getDate("date_reservation").toLocalDate(),
                        rs.getString("plaque"),
                        rs.getInt("id_employe")
                ));
            }
        }
        return reservations;
    }

    // ===== INSERT =====
    public void insert(Reservation r) throws SQLException {
        String sql = """
            INSERT INTO t_reservation(date_reservation, plaque, id_employe, nb_places)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(r.getDateReservation()));
            ps.setString(2, r.getPlaque());
            ps.setInt(3, r.getIdEmploye());
            ps.setInt(4, r.getNbPlaces());
            ps.executeUpdate();
        }
    }

    // ===== SOMME DES PLACES DÉJÀ RÉSERVÉES =====
    public int getTotalPlacesReservees(String plaque, LocalDate date) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(nb_places), 0) AS total
            FROM t_reservation
            WHERE plaque = ? AND date_reservation = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, plaque);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // ===== UPDATE =====
    public void update(Reservation r) throws SQLException {
        String sql = """
            UPDATE t_reservation
            SET date_reservation = ?, plaque = ?, id_employe = ?
            WHERE id_reservation = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(r.getDateReservation()));
            ps.setString(2, r.getPlaque());
            ps.setInt(3, r.getIdEmploye());
            ps.setInt(4, r.getIdReservation());
            ps.executeUpdate();
        }
    }
}
