package com.echecs.main;

/**
 * Classe qui gère la création et la gestion des joueurs
 */
public class Joueur {
    // Initialisation des paramètres
    private String nomJoueur;
    private int nbParties;
    private int nbPartiesGagne;

    /**
     * Constructeur de la classe
     *
     * @param nomJoueur
     * @param nbParties
     * @param nbPartiesGagne
     */
    public Joueur(String nomJoueur, int nbParties, int nbPartiesGagne) {
        this.nomJoueur = nomJoueur;
        this.nbParties = nbParties;
        this.nbPartiesGagne = nbPartiesGagne;
    }

    // Getter et setter

    /**
     * Méthode qui retourne le nom du joueur
     *
     * @return
     */
    public String getNomJoueur() {
        return nomJoueur;
    }

    /**
     * Méthode qui retourne le nombre de partie jouer
     *
     * @return
     */
    public int getNbParties() {
        return nbParties;
    }

    /**
     * Méthode qui modifie le nombre de partie
     *
     * @param nbParties
     */
    public void setNbParties(int nbParties) {
        this.nbParties = nbParties;
    }

    /**
     * Méthode retourne le nombre de partie gagnée
     *
     * @return
     */
    public int getNbPartiesGagne() {
        return nbPartiesGagne;
    }

    /**
     * Méthode qui modifie le nombre de partie gangné
     *
     * @param nbPartiesGagne
     */
    public void setNbPartiesGagne(int nbPartiesGagne) {
        this.nbPartiesGagne = nbPartiesGagne;
    }

    /**
     * Méthode retournant un String
     *
     * @return
     */
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