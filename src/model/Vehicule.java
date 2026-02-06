package model;

public class Vehicule {
    private String plaque;
    private String marque;
    private String modele;
    private int capaciteMax;

    public Vehicule(String plaque, String marque, String modele, int capaciteMax) {
        this.plaque = plaque;
        this.marque = marque;
        this.modele = modele;
        this.capaciteMax = capaciteMax;
    }

    public String getPlaque() {
        return plaque;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }
}
