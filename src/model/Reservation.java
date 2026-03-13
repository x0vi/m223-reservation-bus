package model;

public class Reservation {
    private int idReservation;      // généré par la DB (AUTO_INCREMENT), pas dans le constructeur
    private String dateReservation;
    private String plaque;
    private int idEmploye;
    private int nbPlaces;
    private boolean disponibilite;

    public Reservation(String dateReservation, String plaque, int idEmploye, int nbPlaces) {
        this.dateReservation = dateReservation;
        this.plaque = plaque;
        this.idEmploye = idEmploye;
        this.nbPlaces = nbPlaces;
        this.disponibilite = true;
    }

    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
    public String getDateReservation() { return dateReservation; }
    public String getPlaque() { return plaque; }
    public int getIdEmploye() { return idEmploye; }
    public int getNbPlaces() { return nbPlaces; }
    public boolean getDisponibilite() { return disponibilite; }
    public void setDisponibilite(boolean disponibilite) { this.disponibilite = disponibilite; }
}
