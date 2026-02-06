package model;

public class Employe {
    private int idEmploye;
    private String nom;
    private String prenom;

    public Employe(int idEmploye, String nom, String prenom) {
        this.idEmploye = idEmploye;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
