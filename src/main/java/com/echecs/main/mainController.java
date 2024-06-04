package com.echecs.main;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.util.ArrayList;
import java.util.List;

public class mainController {
    @FXML
    private GridPane jeu;

    @FXML
    private Button boutonJouer;

    List<Piece> pions = new ArrayList<Piece>();

    List<VBox> cases = new ArrayList<VBox>();

    private Piece selectedPiece = null;

    @FXML
    private void jouerClicked() {
        for (int row = 0; row < 8; ++row) {
            for(int col = 0; col < 8; ++col) {
                VBox rect = new VBox();

                if(row < 2) {
                    Piece p = new Piece("pion", "blanc", row, col);
                    pions.add(p);
                } else if(row > 8-3) {
                    Piece p = new Piece("pion", "noir", row, col);
                    pions.add(p);
                }

                GridPane.setHgrow(rect, Priority.ALWAYS);
                GridPane.setVgrow(rect, Priority.ALWAYS);

                rect.setMaxWidth(Double.MAX_VALUE);
                rect.setMaxHeight(Double.MAX_VALUE);

                rect.setMinHeight(Double.MIN_VALUE);
                rect.setMinWidth(Double.MIN_VALUE);

                rect.setAlignment(Pos.CENTER);

                rect.setOnMouseClicked(event -> {
                    int newRow = GridPane.getRowIndex(rect);
                    int newCol = GridPane.getColumnIndex(rect);

                    if (selectedPiece == null) {
                        for(int i = 0; i < pions.size(); ++i) {
                            if(pions.get(i).getX() == newRow && pions.get(i).getY() == newCol) selectedPiece = pions.get(i);
                        }
                        if(selectedPiece != null) selectedPiece.getSymbole().setStyle("-fx-border-color: green; -fx-border-style: solid; -fx-border-width: 10;");

                    } else {
                        Piece selectedOtherPiece = null;
                        for(int i = 0; i < pions.size(); ++i) {
                            if(pions.get(i).getX() == newRow && pions.get(i).getY() == newCol) selectedOtherPiece = pions.get(i);
                        }

                        selectedPiece.getSymbole().setStyle("");
                        if(selectedPiece == selectedOtherPiece) {
                            selectedPiece = null;
                            return;
                        }
                        if(selectedOtherPiece != null) pions.remove(selectedOtherPiece);

                        selectedPiece.setX(newRow);
                        selectedPiece.setY(newCol);

                        updatePiece();
                        selectedPiece = null;
                    }
                });

                if ((row + col) % 2 == 0) rect.setBackground(Background.fill(Color.WHITE));
                else rect.setBackground(Background.fill(Paint.valueOf("#6bbd41")));

                jeu.add(rect, col, row);
            }
        }
        updatePiece();
    }

    public void updatePiece() {
        for(int i = 0; i < jeu.getChildren().size(); ++i) {
            VBox piece = (VBox) jeu.getChildren().get(i);
            piece.getChildren().clear();
        }
        for(int i = 0; i < pions.size(); ++i) {
            Piece p = pions.get(i);
            VBox uneCase = (VBox) jeu.getChildren().get(p.getX()*8+p.getY());
            uneCase.getChildren().add(p.getSymbole());
        }
        for(int i = 0; i < jeu.getChildren().size(); ++i) {
            VBox piece = (VBox) jeu.getChildren().get(i);
            Label l = new Label(" ");
            l.setFont(Font.font("sans-serif", FontPosture.REGULAR, 50));
            if(piece.getChildren().size() == 0) piece.getChildren().add(l);
        }
    }
}