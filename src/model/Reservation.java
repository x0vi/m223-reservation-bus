package model;

import java.time.LocalDate;

public class Reservation {
    private int idReservation;
    private LocalDate dateReservation;
    private String plaque;
    private int idEmploye;
    private int nbPlaces;
    private boolean disponibilite;

    public Reservation(int idReservation, LocalDate dateReservation, String plaque, int idEmploye) {
        this(idReservation, dateReservation, plaque, idEmploye, 1);
    }

    public Reservation(int idReservation, LocalDate dateReservation, String plaque, int idEmploye, int nbPlaces) {
        this.idReservation = idReservation;
        this.dateReservation = dateReservation;
        this.plaque = plaque;
        this.idEmploye = idEmploye;
        this.nbPlaces = nbPlaces;
        this.disponibilite = true;
    }

    public int getIdReservation() { return idReservation; }
    public LocalDate getDateReservation() { return dateReservation; }
    public String getPlaque() { return plaque; }
    public int getIdEmploye() { return idEmploye; }
    public int getNbPlaces() { return nbPlaces; }
    public boolean getDisponibilite() { return disponibilite; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }
}
