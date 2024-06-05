package com.echecs.main;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

public class Joueur {
    // Initialisation des paramètres
    private Label nomJoueur;
    private int nbParties;
    private int nbPartiesGagne;
    private boolean isOrdinateurPlayer;
    private double timer;

    public Joueur(Label nomJoueur, boolean isOrdinateurPlayer, double tempsLimite){
        this.nomJoueur = nomJoueur;
        this.nbParties = 0;
        this.nbPartiesGagne = 0;
        this.timer = tempsLimite;
        this.isOrdinateurPlayer = isOrdinateurPlayer;
    }

    // Getter et setter
    public Label getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(Label nomJoueur) {
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

    public double getTimer() {
        return timer;
    }

    public void setTimeLimit(long timeLimit) {
        this.timer = timer;
    }

    public void incrementNbParties() {
        this.nbParties++;
    }

    public void incrementPartiesGagne() {
        this.nbPartiesGagne++;
    }

    @Override
    public String toString(){
        return "Joueur{" +
                "NomJoueur='" + nomJoueur.getText() + '\'' + // Modification pour Label
                ", NbParties=" + nbParties +
                ", NbPartiesGagne=" + nbPartiesGagne +
                ", isOrdinateurPlayer=" + isOrdinateurPlayer +
                ", timer=" + timer +
                '}' ;
    }

    // Timer
    private void initialiserEtDemarrerTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                nbPartiesGagne++;
                Platform.runLater(() -> nomJoueur.setText("Nombre de victoires: " + nbPartiesGagne));
            }
        }, 0, 600000); // 600000 ms = 10 minutes
    }
}
//a mettre dans le main application pour que ca utilise la classe :

//Label joueurLabel = new Label("Nom du Joueur");
//Joueur joueur = new Joueur(joueurLabel, false, 600);
//joueur.initialiserEtDemarrerTimer(); // Démarrer le timer
// VBox root = new VBox(joueurLabel);
