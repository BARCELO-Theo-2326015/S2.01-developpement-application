package com.echecs.main;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    private final List<Piece> pions = new ArrayList<>();

    private Piece selectedPiece = null;

    @FXML
    private void jouerClicked() {
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
    }

    private void reinitialiserPlateau() {
        jeu.getChildren().clear();
        pions.clear();
        selectedPiece = null;

        for (int ligne = 0; ligne < 8; ++ligne) {
            for (int col = 0; col < 8; ++col) {
                VBox caseRect = new VBox();
                GridPane.setHgrow(caseRect, Priority.ALWAYS);
                GridPane.setVgrow(caseRect, Priority.ALWAYS);

                caseRect.setMaxWidth(Double.MAX_VALUE);
                caseRect.setMaxHeight(Double.MAX_VALUE);

                caseRect.setMinHeight(Double.MIN_VALUE);
                caseRect.setMinWidth(Double.MIN_VALUE);

                caseRect.setAlignment(Pos.CENTER);
                caseRect.setOnMouseClicked(event -> clickEvent(caseRect));

                if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Color.WHITE));
                else caseRect.setBackground(Background.fill(Paint.valueOf("#6bbd41")));

                jeu.add(caseRect, col, ligne);
            }
        }
    }

    private void configurerPieces() {
        for (int ligne = 0; ligne < 8; ++ligne) {
            for (int col = 0; col < 8; ++col) {
                Piece p = null;
                if (ligne == 1) p = new Piece("PAWN", "BLACK", ligne, col);
                if (ligne == 6) p = new Piece("PAWN", "WHITE", ligne, col);
                if ((ligne == 0 || ligne == 7) && (col == 0 || col == 7)) p = new Piece("ROOK", (ligne == 0 ? "BLACK" : "WHITE"), ligne, col);
                if ((ligne == 0 || ligne == 7) && (col == 1 || col == 6)) p = new Piece("KNIGHT", (ligne == 0 ? "BLACK" : "WHITE"), ligne, col);
                if ((ligne == 0 || ligne == 7) && (col == 2 || col == 5)) p = new Piece("BISHOP", (ligne == 0 ? "BLACK" : "WHITE"), ligne, col);
                if ((ligne == 0 || ligne == 7) && col == 3) p = new Piece("QUEEN", (ligne == 0 ? "BLACK" : "WHITE"), ligne, col);
                if ((ligne == 0 || ligne == 7) && col == 4) p = new Piece("KING", (ligne == 0 ? "BLACK" : "WHITE"), ligne, col);
                if (p != null) pions.add(p);
            }
        }
    }

    private void clickEvent(Node rect) {
        int nouvelleLigne = GridPane.getRowIndex(rect);
        int nouvelleCol = GridPane.getColumnIndex(rect);

        if (selectedPiece == null) {
            selectionnerPiece(nouvelleLigne, nouvelleCol);
        } else {
            deplacerPiece(nouvelleLigne, nouvelleCol);
        }
    }

    private void selectionnerPiece(int ligne, int col) {
        for (Piece pion : pions) {
            if (pion.getX() == ligne && pion.getY() == col) {
                selectedPiece = pion;
                VBox selectedCase = (VBox) jeu.getChildren().get(selectedPiece.getX() * 8 + selectedPiece.getY());
                selectedCase.setStyle("-fx-border-color: green; -fx-border-style: solid; -fx-border-width: 10;");
                break;
            }
        }
    }

    private void deplacerPiece(int nouvelleLigne, int nouvelleCol) {
        VBox selectedCase = (VBox) jeu.getChildren().get(selectedPiece.getX() * 8 + selectedPiece.getY());
        selectedCase.setStyle("");

        Piece autrePieceSelectionnee = getPieceAt(nouvelleCol, nouvelleLigne);

        if (selectedPiece.equals(autrePieceSelectionnee) || (autrePieceSelectionnee != null && selectedPiece.getEquipe().equals(autrePieceSelectionnee.getEquipe()))) {
            selectedPiece = null;
            return;
        }

        String valide = deplacementPieceValide(selectedPiece, selectedPiece.getY(), selectedPiece.getX(), nouvelleCol, nouvelleLigne);
        if (valide.equals("false")) {
            selectedPiece = null;
            return;
        }

        if (autrePieceSelectionnee != null && valide.equals("AVANCE")) {
            selectedPiece = null;
            return;
        }

        if (autrePieceSelectionnee == null && valide.equals("CAPTURE")) {
            selectedPiece = null;
            return;
        }

        VBox caseCible = (VBox) jeu.getChildren().get(nouvelleLigne * 8 + nouvelleCol);
        caseCible.getChildren().clear();
        caseCible.getChildren().add(selectedPiece.getSymbole());

        VBox caseOriginale = (VBox) jeu.getChildren().get(selectedPiece.getX() * 8 + selectedPiece.getY());
        caseOriginale.getChildren().clear();

        if (autrePieceSelectionnee != null) pions.remove(autrePieceSelectionnee);

        selectedPiece.setX(nouvelleLigne);
        selectedPiece.setY(nouvelleCol);

        selectedPiece = null;
    }

    public void mettreAJourPieces() {
        for (int i = 0; i < jeu.getChildren().size(); ++i) {
            VBox piece = (VBox) jeu.getChildren().get(i);
            piece.getChildren().clear();
        }
        for (Piece p : pions) {
            VBox uneCase = (VBox) jeu.getChildren().get(p.getX() * 8 + p.getY());
            uneCase.getChildren().add(p.getSymbole());
        }
        for (int i = 0; i < jeu.getChildren().size(); ++i) {
            VBox piece = (VBox) jeu.getChildren().get(i);
            if (piece.getChildren().isEmpty()) {
                Label l = new Label(" ");
                l.setFont(Font.font("sans-serif", FontPosture.REGULAR, 50));
                piece.getChildren().add(l);
            }
        }
    }

    private String deplacementPieceValide(Piece piece, int col, int ligne, int colActuelle, int ligneActuelle) {
        if (col < 0 || col >= 8 || ligne < 0 || ligne >= 8) return "false";

        String typePiece = piece.getType();

        return switch (typePiece) {
            case "PAWN" -> validerDeplacementPion(piece, col, ligne, colActuelle, ligneActuelle);
            case "ROOK" -> validerDeplacementTour(col, ligne, colActuelle, ligneActuelle);
            case "KNIGHT" -> validerDeplacementCavalier(col, ligne, colActuelle, ligneActuelle);
            case "BISHOP" -> validerDeplacementFou(col, ligne, colActuelle, ligneActuelle);
            case "QUEEN" -> validerDeplacementReine(col, ligne, colActuelle, ligneActuelle);
            case "KING" -> validerDeplacementRoi(col, ligne, colActuelle, ligneActuelle);
            default -> "false";
        };
    }

    private String validerDeplacementPion(Piece piece, int col, int ligne, int colActuelle, int ligneActuelle) {
        String equipePiece = piece.getEquipe();
        System.out.println("Ligne actuelle : " + ligneActuelle);
        System.out.println("Colonne actuelle : " + colActuelle);
        System.out.println("Nouvelle ligne : " + ligne);
        System.out.println("Nouvelle colonne : " + col);
        if (equipePiece.equals("WHITE")) {
            if (col == colActuelle && ligne == ligneActuelle + 1) return "AVANCE";
            if (ligneActuelle == 1 && col == colActuelle && ligne == ligneActuelle + 2 && getPieceAt(col, ligneActuelle + 1) == null && getPieceAt(col, ligneActuelle + 2) == null) return "AVANCE";
            if (Math.abs(col - colActuelle) == 1 && ligne == ligneActuelle + 1) return "CAPTURE";
        } else if (equipePiece.equals("BLACK")) {
            if (col == colActuelle && ligne == ligneActuelle - 1) return "AVANCE";
            if (ligneActuelle == 6 && col == colActuelle && ligne == ligneActuelle - 2 && getPieceAt(col, ligneActuelle - 1) == null && getPieceAt(col, ligneActuelle - 2) == null) return "AVANCE";
            if (Math.abs(col - colActuelle) == 1 && ligne == ligneActuelle - 1) return "CAPTURE";
        }
        return "false";
    }




    private String validerDeplacementTour(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (col == colActuelle) {
            int step = (ligne > ligneActuelle) ? 1 : -1;
            for (int i = ligneActuelle + step; i != ligne; i += step) {
                if (getPieceAt(colActuelle, i) != null) return "false";
            }
            return "true";
        } else if (ligne == ligneActuelle) {
            int step = (col > colActuelle) ? 1 : -1;
            for (int i = colActuelle + step; i != col; i += step) {
                if (getPieceAt(i, ligneActuelle) != null) return "false";
            }
            return "true";
        }
        return "false";
    }

    private String validerDeplacementCavalier(int col, int ligne, int colActuelle, int ligneActuelle) {
        int deltaX = Math.abs(col - colActuelle);
        int deltaY = Math.abs(ligne - ligneActuelle);
        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return "true";
        }
        return "false";
    }

    private String validerDeplacementFou(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (Math.abs(col - colActuelle) == Math.abs(ligne - ligneActuelle)) {
            int colStep = (col > colActuelle) ? 1 : -1;
            int ligneStep = (ligne > ligneActuelle) ? 1 : -1;
            for (int i = 1; i < Math.abs(col - colActuelle); i++) {
                if (getPieceAt(colActuelle + i * colStep, ligneActuelle + i * ligneStep) != null) return "false";
            }
            return "true";
        }
        return "false";
    }

    private String validerDeplacementReine(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (col == colActuelle || ligne == ligneActuelle) {
            return validerDeplacementTour(col, ligne, colActuelle, ligneActuelle);
        } else if (Math.abs(col - colActuelle) == Math.abs(ligne - ligneActuelle)) {
            return validerDeplacementFou(col, ligne, colActuelle, ligneActuelle);
        }
        return "false";
    }

    private String validerDeplacementRoi(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (Math.abs(col - colActuelle) <= 1 && Math.abs(ligne - ligneActuelle) <= 1) {
            return "true";
        }
        return "false";
    }

    private Piece getPieceAt(int col, int ligne) {
        for (Piece p : pions) {
            if (p.getX() == ligne && p.getY() == col) return p;
        }
        return null;
    }
}
