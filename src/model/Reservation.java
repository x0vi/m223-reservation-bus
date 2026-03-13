package model;

import java.sql.Date;

public class Reservation {
    private int idReservation;
    private Date dateReservation;
    private String plaque;
    private int idEmploye;
    private int nbPlaces;

    public Reservation(int idReservation, Date dateReservation, String plaque, int idEmploye) {
        this(idReservation, dateReservation, plaque, idEmploye, 1);
    }

    public Reservation(int idReservation, Date dateReservation, String plaque, int idEmploye, int nbPlaces) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.plaque = plaque;
        this.idEmploye = idEmploye;
        this.nbPlaces = nbPlaces;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public String getPlaque() {
        return plaque;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }
}
