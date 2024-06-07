package com.echecs.main;

public class Joueur {
    // Initialisation des paramètres
    private String nomJoueur;
    private int nbParties;
    private int nbPartiesGagne;

    public Joueur(String nomJoueur, int nbParties, int nbPartiesGagne) {
        this.nomJoueur = nomJoueur;
        this.nbParties = nbParties;
        this.nbPartiesGagne = nbPartiesGagne;
    }

    // Getter et setter
    public String getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public int getNbParties() {
        return nbParties;
    }

    public void setNbParties(int nbParties) {
        this.nbParties = nbParties;
    }

    public int getNbPartiesGagne() {
        return nbPartiesGagne;
    }

    public void setNbPartiesGagne(int nbPartiesGagne) {
        this.nbPartiesGagne = nbPartiesGagne;
    }

    public void incrementNbParties() {
        this.nbParties++;
    }

    public void incrementPartiesGagne() {
        this.nbPartiesGagne++;
    }

    @Override
    public String toString(){
        return
        "Joueur{" +
            "NomJoueur='" + nomJoueur + '\'' +
            ", NbParties=" + nbParties +
            ", NbPartiesGagne=" + nbPartiesGagne +
        '}' ;
    }


}