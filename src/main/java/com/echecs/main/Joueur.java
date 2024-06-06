package com.echecs.main;

import javafx.scene.control.Label;

public class Joueur {
    // Initialisation des paramètres
    private String nomJoueur;
    private int nbParties;
    private int nbPartiesGagne;
    private boolean isOrdinateurPlayer;

    public Joueur(String nomJoueur, boolean isOrdinateurPlayer){
        this.nomJoueur = nomJoueur;
        this.nbParties = 0;
        this.nbPartiesGagne = 0;
        this.isOrdinateurPlayer = isOrdinateurPlayer;
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

    public boolean isOrdinateurPlayer() {
        return isOrdinateurPlayer;
    }

    public void setIsOrdinateurPlayer(boolean isOrdinateurPlayer) {
        this.isOrdinateurPlayer = isOrdinateurPlayer;
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
            ", isOrdinateurPlayer=" + isOrdinateurPlayer +
        '}' ;
    }
}