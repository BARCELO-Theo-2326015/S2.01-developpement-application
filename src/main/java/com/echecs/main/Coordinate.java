package com.echecs.main;

/**
 * Classe des coordonnée
 */
public class Coordinate {
    public int x;
    public int y;

    /**
     * Constructeur
     *
     * @param x
     * @param y
     */
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Méthode retournant un String avec des coordonnées
     *
     * @return
     */
    @Override
    public String toString() {
        return "Coordinate{" + "x=" + x + ", y=" + y + '}';
    }
}