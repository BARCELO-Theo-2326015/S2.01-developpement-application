package com.echecs.main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Piece {
    public String getType() {
        return type;
    }

    String type;

    public String getEquipe() {
        return equipe;
    }

    String equipe;

    public ImageView getSymbole() {
        return symbole;
    }

    ImageView symbole;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    int x;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    int y;

    Piece(String type, String equipe, int x, int y) {
        this.type = type;
        this.equipe = equipe;
        this.x = x;
        this.y = y;
        generateSymbol();
    }

    private void generateSymbol() {

        // load the image
        Image image = null;

        symbole = new ImageView();

        if(equipe == "WHITE") {
            if (type == "KING") image = new Image(getClass().getResourceAsStream("images/king_white.png"));
            else if (type == "QUEEN") image = new Image(getClass().getResourceAsStream("images/queen_white.png"));
            else if (type == "ROOK") image = new Image(getClass().getResourceAsStream("images/rook_white.png"));
            else if (type == "BISHOP") image = new Image(getClass().getResourceAsStream("images/bishop_white.png"));
            else if (type == "KNIGHT") image = new Image(getClass().getResourceAsStream("images/knight_white.png"));
            else if (type == "PAWN") image = new Image(getClass().getResourceAsStream("images/pawn_white.png"));
        } else if(equipe == "BLACK") {
            if (type == "KING") image = new Image(getClass().getResourceAsStream("images/king_black.png"));
            else if (type == "QUEEN") image = new Image(getClass().getResourceAsStream("images/queen_black.png"));
            else if (type == "ROOK") image = new Image(getClass().getResourceAsStream("images/rook_black.png"));
            else if (type == "BISHOP") image = new Image(getClass().getResourceAsStream("images/bishop_black.png"));
            else if (type == "KNIGHT") image = new Image(getClass().getResourceAsStream("images/knight_black.png"));
            else if (type == "PAWN") image = new Image(getClass().getResourceAsStream("images/pawn_black.png"));
        }

        if(image != null) {
            symbole.setPreserveRatio(true);
            symbole.setImage(image);
        }


        HBox.setHgrow(symbole, Priority.ALWAYS);
        VBox.setVgrow(symbole, Priority.ALWAYS);
    }
}
