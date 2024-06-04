package com.echecs.main;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.sql.Types;

public class Piece {
    Button box;

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

    Piece(String type, String equipe, int x, int y) {
        this.type = type;
        this.equipe = equipe;
        this.x = x;
        this.y = y;
        generateSymbol();
    }

    private void generateSymbol() {
        if(equipe == "WHITE") {
            if (type == "KING") symbole = new Label("♔");
            else if (type == "QUEEN") symbole = new Label("♕");
            else if (type == "ROOK") symbole = new Label("♖");
            else if (type == "BISHOP") symbole = new Label("♗");
            else if (type == "KNIGHT") symbole = new Label("♘");
            else if (type == "PAWN") symbole = new Label("♙");
        } else if(equipe == "BLACK") {
            if (type == "KING") symbole = new Label("♚");
            else if (type == "QUEEN") symbole = new Label("♛");
            else if (type == "ROOK") symbole = new Label("♜");
            else if (type == "BISHOP") symbole = new Label("♝");
            else if (type == "KNIGHT") symbole = new Label("♞");
            else if (type == "PAWN") symbole = new Label("♟");
        }
        HBox.setHgrow(symbole, Priority.ALWAYS);
        VBox.setVgrow(symbole, Priority.ALWAYS);
        symbole.setFont(Font.font("sans-serif", FontPosture.REGULAR, 40));
    }
}
