package com.echecs.main;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class test extends Application {

    // Constantes définissant la taille de l'échiquier et des cases
    private static final int TAILLE = 8;
    private static final int TAILLE_CASE = 80;

    // Le plateau de jeu représenté par un GridPane
    private GridPane echiquier = new GridPane();
    // La pièce actuellement sélectionnée
    private Circle pieceSelectionnee = null;

    // Enumération des différents types de pièces avec leur couleur associée
    private enum TypePiece {
        PION_NOIR(Color.BLACK),
        PION_BLANC(Color.WHITE),
        TOUR_NOIR(Color.BLACK),
        TOUR_BLANC(Color.WHITE),
        CAVALIER_NOIR(Color.BLACK),
        CAVALIER_BLANC(Color.WHITE),
        FOU_NOIR(Color.BLACK),
        FOU_BLANC(Color.WHITE),
        REINE_NOIR(Color.BLACK),
        REINE_BLANC(Color.WHITE),
        ROI_NOIR(Color.BLACK),
        ROI_BLANC(Color.WHITE);

        private final Color couleur;

        TypePiece(Color couleur) {
            this.couleur = couleur;
        }

        public Color getCouleur() {
            return couleur;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialisation de l'échiquier avec les cases et les pièces
        initialiserEchiquier();
        // Création de la scène avec le GridPane représentant l'échiquier
        Scene scene = new Scene(echiquier, TAILLE * TAILLE_CASE, TAILLE * TAILLE_CASE);
        // Ajout d'un gestionnaire d'événements pour les clics de souris
        scene.setOnMouseClicked(event -> gererClic(event.getX(), event.getY()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jeu d'échecs");
        primaryStage.show();
    }

    // Méthode pour initialiser l'échiquier avec les cases et les pièces
    private void initialiserEchiquier() {
        for (int y = 0; y < TAILLE; y++) {
            for (int x = 0; x < TAILLE; x++) {
                // Création d'une case
                Rectangle caseEchiquier = creerCase(x, y);
                // Création d'une pièce si nécessaire
                Circle piece = creerPiece(x, y);
                // Placement de la case et de la pièce sur le GridPane
                placerPiece(caseEchiquier, piece, x, y);
            }
        }
    }

    // Méthode pour créer une case de l'échiquier
    private Rectangle creerCase(int x, int y) {
        Rectangle caseEchiquier = new Rectangle(TAILLE_CASE, TAILLE_CASE);
        caseEchiquier.setFill((x + y) % 2 == 0 ? Color.BEIGE : Color.DARKGREEN);
        return caseEchiquier;
    }

    // Méthode pour créer une pièce à une position donnée
    private Circle creerPiece(int x, int y) {
        TypePiece typePiece = obtenirTypePiece(x, y);
        if (typePiece == null) return null;
        Circle piece = new Circle(TAILLE_CASE / 3);
        piece.setFill(typePiece.getCouleur());
        piece.setUserData(typePiece); // Stocke le type de pièce dans les données utilisateur du cercle
        return piece;
    }

    // Méthode pour déterminer le type de pièce à une position donnée
    private TypePiece obtenirTypePiece(int x, int y) {
        if (y == 1) return TypePiece.PION_NOIR;
        if (y == 6) return TypePiece.PION_BLANC;
        if ((y == 0 || y == 7) && (x == 0 || x == 7)) return y == 0 ? TypePiece.TOUR_NOIR : TypePiece.TOUR_BLANC;
        if ((y == 0 || y == 7) && (x == 1 || x == 6)) return y == 0 ? TypePiece.CAVALIER_NOIR : TypePiece.CAVALIER_BLANC;
        if ((y == 0 || y == 7) && (x == 2 || x == 5)) return y == 0 ? TypePiece.FOU_NOIR : TypePiece.FOU_BLANC;
        if ((y == 0 || y == 7) && x == 3) return y == 0 ? TypePiece.REINE_NOIR : TypePiece.REINE_BLANC;
        if ((y == 0 || y == 7) && x == 4) return y == 0 ? TypePiece.ROI_NOIR : TypePiece.ROI_BLANC;
        return null;
    }

    // Méthode pour placer une pièce sur l'échiquier
    private void placerPiece(Rectangle caseEchiquier, Circle piece, int x, int y) {
        echiquier.add(caseEchiquier, x, y);
        if (piece != null) {
            GridPane.setHalignment(piece, javafx.geometry.HPos.CENTER);
            GridPane.setValignment(piece, javafx.geometry.VPos.CENTER);
            echiquier.add(piece, x, y);
        }
    }

    // Méthode pour gérer les clics de souris
    private void gererClic(double x, double y) {
        int col = (int) (x / TAILLE_CASE);
        int row = (int) (y / TAILLE_CASE);

        if (pieceSelectionnee == null) {
            // Sélectionner une pièce
            selectionnerPiece(col, row);
        } else {
            // Déplacer la pièce si le mouvement est valide
            if (mouvementPieceEstValide(pieceSelectionnee, col, row)) {
                deplacerPiece(pieceSelectionnee, col, row);
            } else {
                System.out.println("Mouvement invalide !");
                pieceSelectionnee = null; // Réinitialiser pieceSelectionnee en cas de mouvement invalide
            }
        }
    }

    // Méthode pour sélectionner une pièce
    private void selectionnerPiece(int col, int row) {
        if (col < 0 || col >= TAILLE || row < 0 || row >= TAILLE) {
            return;
        }
        for (Node node : echiquier.getChildren()) {
            if (node instanceof Circle) {
                Circle piece = (Circle) node;
                if (GridPane.getColumnIndex(piece) == col && GridPane.getRowIndex(piece) == row) {
                    pieceSelectionnee = piece;
                    return;
                }
            }
        }
    }

    // Méthode pour vérifier si le mouvement d'une pièce est valide
    private boolean mouvementPieceEstValide(Circle piece, int col, int row) {
        int currentCol = GridPane.getColumnIndex(piece);
        int currentRow = GridPane.getRowIndex(piece);

        if (col < 0 || col >= TAILLE || row < 0 || row >= TAILLE) {
            return false;
        }

        for (Node node : echiquier.getChildren()) {
            if (node instanceof Circle) {
                Circle autrePiece = (Circle) node;
                int autrePieceCol = GridPane.getColumnIndex(autrePiece);
                int autrePieceRow = GridPane.getRowIndex(autrePiece);
                if (autrePieceCol == col && autrePieceRow == row && pieceRemplissageEgal(piece, autrePiece)) {
                    return false;
                }
            }
        }

        TypePiece typePiece = (TypePiece) piece.getUserData();

        // Déplacement des pions
        if (typePiece == TypePiece.PION_NOIR) {
            if (col == currentCol && row == currentRow + 1) {
                return true; // Avance d'une case
            } else if (currentRow == 1 && col == currentCol && row == currentRow + 2) {
                return true; // Premier mouvement spécial de deux cases
            } else if (Math.abs(col - currentCol) == 1 && row == currentRow + 1) {
                return estPieceAdverseA(col, row); // Capture en diagonale vers l'avant
            }
        } else if (typePiece == TypePiece.PION_BLANC) {
            if (col == currentCol && row == currentRow - 1) {
                return true; // Avance d'une case
            } else if (currentRow == 6 && col == currentCol && row == currentRow - 2) {
                return true; // Premier mouvement spécial de deux cases
            } else if (Math.abs(col - currentCol) == 1 && row == currentRow - 1) {
                return estPieceAdverseA(col, row); // Capture en diagonale vers l'avant
            }
        }

        // Déplacement des tours
        if (typePiece == TypePiece.TOUR_NOIR || typePiece == TypePiece.TOUR_BLANC) {
            if (col == currentCol || row == currentRow) {
                return !estCheminBloque(currentCol, currentRow, col, row); // Déplacement horizontal ou vertical
            }
        }

        // Déplacement des cavaliers
        if (typePiece == TypePiece.CAVALIER_NOIR || typePiece == TypePiece.CAVALIER_BLANC) {
            if ((Math.abs(col - currentCol) == 2 && Math.abs(row - currentRow) == 1) ||
                    (Math.abs(col - currentCol) == 1 && Math.abs(row - currentRow) == 2)) {
                return true; // Déplacement en forme de L
            }
        }

        // Déplacement des fous
        if (typePiece == TypePiece.FOU_NOIR || typePiece == TypePiece.FOU_BLANC) {
            if (Math.abs(col - currentCol) == Math.abs(row - currentRow)) {
                return !estCheminBloque(currentCol, currentRow, col, row); // Déplacement diagonal
            }
        }

        // Déplacement des reines
        if (typePiece == TypePiece.REINE_NOIR || typePiece == TypePiece.REINE_BLANC) {
            if (col == currentCol || row == currentRow || Math.abs(col - currentCol) == Math.abs(row - currentRow)) {
                return !estCheminBloque(currentCol, currentRow, col, row); // Déplacement horizontal, vertical ou diagonal
            }
        }

        // Déplacement des rois
        if (typePiece == TypePiece.ROI_NOIR || typePiece == TypePiece.ROI_BLANC) {
            if (Math.abs(col - currentCol) <= 1 && Math.abs(row - currentRow) <= 1) {
                return true; // Déplacement d'une case dans toutes les directions
            }
        }

        return false;
    }

    // Méthode pour vérifier si deux pièces ont la même couleur
    private boolean pieceRemplissageEgal(Circle piece1, Circle piece2) {
        return piece1.getFill().equals(piece2.getFill());
    }

    // Méthode pour vérifier si une position donnée contient une pièce adverse
    private boolean estPieceAdverseA(int col, int row) {
        for (Node node : echiquier.getChildren()) {
            if (node instanceof Circle) {
                Circle piece = (Circle) node;
                if (GridPane.getColumnIndex(piece) == col && GridPane.getRowIndex(piece) == row) {
                    return !pieceRemplissageEgal(pieceSelectionnee, piece);
                }
            }
        }
        return false;
    }

    // Méthode pour vérifier si le chemin entre deux positions est bloqué par une autre pièce
    private boolean estCheminBloque(int colDepart, int rowDepart, int colArrivee, int rowArrivee) {
        int deltaCol = Integer.signum(colArrivee - colDepart);
        int deltaRow = Integer.signum(rowArrivee - rowDepart);

        int col = colDepart + deltaCol;
        int row = rowDepart + deltaRow;

        while (col != colArrivee || row != rowArrivee) {
            for (Node node : echiquier.getChildren()) {
                if (node instanceof Circle) {
                    Circle piece = (Circle) node;
                    if (GridPane.getColumnIndex(piece) == col && GridPane.getRowIndex(piece) == row) {
                        return true;
                    }
                }
            }
            col += deltaCol;
            row += deltaRow;
        }

        return false;
    }

    // Méthode pour déplacer une pièce sur l'échiquier
    private void deplacerPiece(Circle piece, int col, int row) {
        echiquier.getChildren().remove(piece);
        echiquier.add(piece, col, row);
        pieceSelectionnee = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
