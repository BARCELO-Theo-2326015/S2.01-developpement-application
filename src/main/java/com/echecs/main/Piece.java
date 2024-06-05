package com.echecs.main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void setType(String type) {
        this.type = type;
    }

    private boolean hasMoved;

    // Ajoutez un getter et un setter pour hasMoved
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    public boolean getHasMoved(){
        return hasMoved;
    }
    Piece(String type, String equipe, int x, int y) {
        this.type = type;
        this.equipe = equipe;
        this.x = x;
        this.y = y;
        this.hasMoved = false;
        generateSymbol();
    }

    public void generateSymbol() {

        // load the image
        Image image = null;

        symbole = new ImageView();

        if (equipe.equals("WHITE")) {
            image = switch (type) {
                case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/king_white.png")));
                case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/queen_white.png")));
                case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/rook_white.png")));
                case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/bishop_white.png")));
                case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/knight_white.png")));
                case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pawn_white.png")));
                default -> image;
            };
        } else if (equipe.equals("BLACK")) {
            image = switch (type) {
                case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/king_black.png")));
                case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/queen_black.png")));
                case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/rook_black.png")));
                case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/bishop_black.png")));
                case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/knight_black.png")));
                case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pawn_black.png")));
                default -> image;
            };

        }

        if (image != null) {
            symbole.setPreserveRatio(true);
            symbole.setImage(image);
        }


        HBox.setHgrow(symbole, Priority.ALWAYS);
        VBox.setVgrow(symbole, Priority.ALWAYS);


    }

    public List<int[]> genererMouvementsPossibles() {
        List<int[]> mouvementsPossibles = new ArrayList<>();
        // Ajouter la logique pour générer les mouvements possibles pour chaque type de pièce
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx != 0 || dy != 0) {
                    int nouvelleX = this.getX() + dx;
                    int nouvelleY = this.getY() + dy;
                    if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                        mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
                    }
                }
            }
        }

        return mouvementsPossibles;
    }

}

