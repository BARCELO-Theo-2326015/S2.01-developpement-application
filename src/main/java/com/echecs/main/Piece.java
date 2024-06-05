package com.echecs.main;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class Piece extends VBox {

    public String getType() {
        return type;
    }

    String type;

    public String getEquipe() {
        return equipe;
    }

    String equipe;

    public Label getSymbole() {
        return symbole;
    }

    Label symbole;

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
    public void setType(String type) {
        this.type = type;
    }

    public void setSymbole(Label symbole) {
        this.symbole = symbole;
    }

    Piece(String type, String equipe, int x, int y) {
        this.type = type;
        this.equipe = equipe;
        this.x = x;
        this.y = y;
        generateSymbol();
    }

    private void generateSymbol() {
        if(equipe.equals("WHITE")) {
            switch (type) {
                case "KING" -> symbole = new Label("♔");
                case "QUEEN" -> symbole = new Label("♕");
                case "ROOK" -> symbole = new Label("♖");
                case "BISHOP" -> symbole = new Label("♗");
                case "KNIGHT" -> symbole = new Label("♘");
                case "PAWN" -> symbole = new Label("♙");
            }
        } else if(equipe.equals("BLACK")) {
            switch (type) {
                case "KING" -> symbole = new Label("♚");
                case "QUEEN" -> symbole = new Label("♛");
                case "ROOK" -> symbole = new Label("♜");
                case "BISHOP" -> symbole = new Label("♝");
                case "KNIGHT" -> symbole = new Label("♞");
                case "PAWN" -> symbole = new Label("♟");
            }
        }
        HBox.setHgrow(symbole, Priority.ALWAYS);
        VBox.setVgrow(symbole, Priority.ALWAYS);
        assert symbole != null;
        symbole.setFont(Font.font("sans-serif", FontPosture.REGULAR, 50));
    }
}
