package com.echecs.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

public class mainController {
    @FXML
    private GridPane jeu;

    @FXML
    private Button boutonJouer;

    public List<Piece> pions = new ArrayList<Piece>();

    private Piece selectedPiece = null;

    private Double height = 0.0;
    private Double width = 0.0;

    @FXML
    private void jouerClicked() {
        jeu.getChildren().clear();
        pions.clear();
        selectedPiece = null;

        jeu.setStyle("");

        for (int row = 0; row < 8; ++row) {
            for(int col = 0; col < 8; ++col) {
                VBox rect = new VBox();

                Piece p = null;
                if (row == 1) p = new Piece("PAWN", "BLACK", row, col);
                if (row == 6) p = new Piece("PAWN", "WHITE", row, col);
                if ((row == 0 || row == 7) && (col == 0 ||col == 7)) p = new Piece("ROOK", (row == 0 ? "BLACK" : "WHITE"), row, col);
                if ((row == 0 || row == 7) && (col == 1 || col == 6))  p = new Piece("KNIGHT", (row == 0 ? "BLACK" : "WHITE"), row, col);
                if ((row == 0 || row == 7) && (col == 2 || col == 5))  p = new Piece("BISHOP", (row == 0 ? "BLACK" : "WHITE"), row, col);
                if ((row == 0 || row == 7) && col == 3)  p = new Piece("QUEEN", (row == 0 ? "BLACK" : "WHITE"), row, col);
                if ((row == 0 || row == 7) && col == 4)  p = new Piece("KING", (row == 0 ? "BLACK" : "WHITE"), row, col);
                if(p != null) pions.add(p);

                GridPane.setHgrow(rect, Priority.ALWAYS);
                GridPane.setVgrow(rect, Priority.ALWAYS);

                rect.setMaxWidth(Double.MAX_VALUE);
                rect.setMaxHeight(Double.MAX_VALUE);

                rect.setMinHeight(Double.MIN_VALUE);
                rect.setMinWidth(Double.MIN_VALUE);

                rect.setAlignment(Pos.CENTER);

                rect.setOnMouseClicked(event -> {
                    clickEvent(event, rect);
                });

                if ((row + col) % 2 == 0) rect.setBackground(Background.fill(Paint.valueOf("#EBECD0")));
                else rect.setBackground(Background.fill(Paint.valueOf("#779556")));

                jeu.add(rect, col, row);
            }
        }
        updatePiece();
        updateGameSize();
    }

    private void clickEvent(MouseEvent event, VBox rect) {
        int newRow = GridPane.getRowIndex(rect);
        int newCol = GridPane.getColumnIndex(rect);

        VBox selectedCase = null;

        if (selectedPiece == null) {
            for(int i = 0; i < pions.size(); ++i) {
                if(pions.get(i).getX() == newRow && pions.get(i).getY() == newCol) selectedPiece = pions.get(i);
            }
            selectedCase = (VBox) jeu.getChildren().get(selectedPiece.getX()*8+selectedPiece.getY());
            if(selectedPiece != null) selectedCase.setStyle("-fx-border-color: green; -fx-border-style: solid; -fx-border-width: 10;");

            return;
        }
        selectedCase = (VBox) jeu.getChildren().get(selectedPiece.getX()*8+selectedPiece.getY());

        Piece selectedOtherPiece = null;
        for(int i = 0; i < pions.size(); ++i) {
            if(pions.get(i).getX() == newRow && pions.get(i).getY() == newCol) selectedOtherPiece = pions.get(i);
        }

        // on reset le border
        selectedCase.setStyle("");

        // si piece identique on enleve la selection
        if(selectedPiece == selectedOtherPiece) {
            selectedPiece = null;
            return;
        }

        if(selectedOtherPiece != null && selectedPiece.getEquipe() == selectedOtherPiece.getEquipe()) {
            selectedPiece = null;
            return;
        }

        String valide = movePieceIsValid(selectedPiece, selectedPiece.getY(), selectedPiece.getX(), newCol, newRow);
        if(valide == "false") {
            selectedPiece = null;
            return;
        } else {
            if(selectedOtherPiece == null && valide == "CAPTURE") {
                selectedPiece = null;
                return;
            } else if(selectedOtherPiece != null && valide == "AVANCE") {
                selectedPiece = null;
                return;
            }
        }

        // on supprime tout du rect et on ajoute la nouvelle piece
        VBox piece2 = (VBox) jeu.getChildren().get(newRow*8+newCol);
        piece2.getChildren().clear();
        piece2.getChildren().add(selectedPiece.getSymbole());

        // on clear la piece originale pour la deplacer
        VBox piece = (VBox) jeu.getChildren().get(selectedPiece.getX()*8+selectedPiece.getY());
        piece.getChildren().clear();

        // on supprime l'ancienne piece
        if(selectedOtherPiece != null) pions.remove(selectedOtherPiece);

        // on la deplace
        selectedPiece.setX(newRow);
        selectedPiece.setY(newCol);

        selectedPiece = null;
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
    }

    private String movePieceIsValid(Piece piece, int col, int row, int currentCol, int currentRow) {
        if (col < 0 || col >= 8 || row < 0 || row >= 8) return "false";

        String PieceType = piece.getType();
        String PieceEquipe = piece.getEquipe();

        // Déplacement des pions
        if (PieceType == "PAWN" && PieceEquipe == "WHITE") {
            if (col == currentCol && row == currentRow + 1) {
                return "AVANCE"; // Avance d'une case
            } else if (currentRow == 1 && col == currentCol && row == currentRow + 2) {
                return "AVANCE"; // Premier mouvement spécial de deux cases
            } else if (Math.abs(col - currentCol) == 1 && row == currentRow + 1) {
                return "CAPTURE"; // Capture en diagonale vers l'avant
            }
        } else if (PieceType == "PAWN" && PieceEquipe == "BLACK") {
            if (col == currentCol && row == currentRow - 1) {
                return "AVANCE"; // Avance d'une case
            } else if (currentRow == 6 && col == currentCol && row == currentRow - 2) {
                return "AVANCE"; // Premier mouvement spécial de deux cases
            } else if (Math.abs(col - currentCol) == 1 && row == currentRow - 1) {
                return "CAPTURE"; // Capture en diagonale vers l'avant
            }
        }

        // Déplacement des tours
        if (PieceType == "ROOK") {
            if (col == currentCol || row == currentRow) {
                return "true"; // Déplacement horizontal ou vertical
            }
        }

        // Déplacement des cavaliers
        if (PieceType == "KNIGHT") {
            if ((Math.abs(col - currentCol) == 2 && Math.abs(row - currentRow) == 1) ||
                    (Math.abs(col - currentCol) == 1 && Math.abs(row - currentRow) == 2)) {
                return "true"; // Déplacement en forme de L
            }
        }

        // Déplacement des fous
        if (PieceType == "BISHOP") {
            if (Math.abs(col - currentCol) == Math.abs(row - currentRow)) {
                return "true"; // Déplacement en diagonale
            }
        }

        // Déplacement des reines
        if (PieceType == "QUEEN") {
            if (col == currentCol || row == currentRow || Math.abs(col - currentCol) == Math.abs(row - currentRow)) {
                return "true"; // Déplacement horizontal, vertical ou en diagonale
            }
        }

        // Déplacement des rois
        if (PieceType == "KING") {
            if (Math.abs(col - currentCol) <= 1 && Math.abs(row - currentRow) <= 1) {
                return "true"; // Déplacement d'une case dans n'importe quelle direction
            }
        }

        return "false";
    }

    @FXML
    private void initialize() {
        jouerClicked();
    }

    public void setResizeEvents(WindowEvent windowEvent) {
        // Get the stage
        Stage stage = (Stage) boutonJouer.getScene().getWindow();

        width = stage.getWidth();
        height = stage.getHeight();
        updateGameSize();

        // Listen for changes to stage size.
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            width = (Double) newVal;
            updateGameSize();
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            height = (Double) newVal;
            updateGameSize();
        });
    }

    public void updateGameSize() {
        Double superVal;
        if(width > height) superVal = height;
        else superVal = width;

        superVal = superVal - 100;

        jeu.setMaxWidth(superVal);
        jeu.setPrefWidth(superVal);
        jeu.setMaxHeight(superVal);
        jeu.setPrefHeight(superVal);

        // update the size of each pieces /8
        for(int i = 0; i < jeu.getChildren().size(); ++i) {
            VBox piece = (VBox) jeu.getChildren().get(i);
            piece.setPrefWidth(superVal/8);
            piece.setPrefHeight(superVal/8);
            if(piece.getChildren().size() > 0) {
                ImageView symbole = (ImageView) piece.getChildren().get(0);
                symbole.setFitWidth(superVal/8);
                symbole.setFitHeight(superVal/8);
            }
        }
    }
}