package com.echecs.main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Cette classe gère la création des différentes pièces
 */
public class Piece {
    /**
     * Méthode retournant le type de la pièce
     *
     * @return
     */
    public String getType() {
        return type;
    }

    String type;

    /**
     * Méthode retournant l'équipe de la pièce
     *
     * @return
     */
    public String getEquipe() {
        return equipe;
    }

    String equipe;

    /**
     * Méthode retournant le symbole
     *
     * @return
     */
    public ImageView getSymbole() {
        return symbole;
    }

    ImageView symbole;

    /**
     * Méthode retournant la valeur X (la ligne)
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Méthode permetant de définir la ligne
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    int x;

    /**
     * Méthode retournant la valeur Y (la colonne)
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Méthode permettant de définir la colonne de la pièce
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    int y;

    /**
     * Méthode permettant de définir le type de pièce
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    private boolean hasMoved;

    /**
     * Méthode permettant de modifier si une pièce c'est déplacer ou non
     *
     * @param hasMoved
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Méthode retournant si une pièce a bouger
     *
     * @return
     */
    public boolean getHasMoved(){
        return hasMoved;
    }

    /**
     * Constructeur de la classe pièce
     *
     * @param type
     * @param equipe
     * @param x
     * @param y
     */
    Piece(String type, String equipe, int x, int y) {
        this.type = type;
        this.equipe = equipe;
        this.x = x;
        this.y = y;
        this.hasMoved = false;
    }

    /**
     * Méthode génerant les différent symbole de chaque pièce
     *
     * @param theme
     */
    public void generateSymbol(String theme) {

        // load the image
        Image image = null;

        symbole = new ImageView();

        switch (theme) {
            case "Classique" -> {
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
            }
            case "Old school" -> {
                if (equipe.equals("WHITE")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roiblancjeu2.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reineblancjeu2.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tourblancjeu2.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/foublancjeu2.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavalierblancjeu2.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionblancjeu2.png")));
                        default -> image;
                    };
                } else if (equipe.equals("BLACK")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roinoirjeu2.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reinenoirjeu2.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tournoijeu2.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/founoirjeu2.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavaliernoirjeu2.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionnoirjeu2.png")));
                        default -> image;
                    };
                }
            }
            case "Red vs Blue" -> {
                if (equipe.equals("WHITE")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roiblancjeu4.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reineblanchejeu4.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tourblancjeu4.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/foublancjeu4.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavalierblancjeu4.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionblancjeu4.png")));
                        default -> image;
                    };
                } else if (equipe.equals("BLACK")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roinoirjeu4.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reinenoirjeu4.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tournoirjeu4.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/founoirjeu4.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavaliernoirjeu4.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionnoirjeu4.png")));
                        default -> image;
                    };
                }
            }
            case "Neo" -> {
                if (equipe.equals("WHITE")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roiblancjeu3.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reineblancjeu3.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tourblancjeu3.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/foublancjeu3.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavalierblancjeu3.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionblancjeu3.png")));
                        default -> image;
                    };
                } else if (equipe.equals("BLACK")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roinoirjeu3.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reinenoirjeu3.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tournoirjeu3.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/founoirjeu3.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavaliernoirjeu3.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionnoirjeu3.png")));
                        default -> image;
                    };
                }
            }
            case "Master" -> {
                if (equipe.equals("WHITE")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roiblancjeu1.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reineblancjeu1.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tourblancjeu1.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/foublancjeu1.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavalierblancjeu1.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionblancjeu1.png")));
                        default -> image;
                    };
                } else if (equipe.equals("BLACK")) {
                    image = switch (type) {
                        case "KING" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/roinoirjeu1.png")));
                        case "QUEEN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/reinenoirjeu1.png")));
                        case "ROOK" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/tournoirjeu1.png")));
                        case "BISHOP" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/founoirjeu1.png")));
                        case "KNIGHT" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/cavaliernoirjeu1.png")));
                        case "PAWN" -> new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/pionnoirjeu1.png")));
                        default -> image;
                    };
                }
            }
        }


        if (image != null) {
            symbole.setPreserveRatio(true);
            symbole.setImage(image);
        }


        HBox.setHgrow(symbole, Priority.ALWAYS);
        VBox.setVgrow(symbole, Priority.ALWAYS);


    }

    /**
     * Méthode retournant une liste de mouvement possible d'une pièce
     *
     * @return
     */
    public List<int[]> genererMouvementsPossibles() {
        List<int[]> mouvementsPossibles = new ArrayList<>();

        if ("KING".equals(this.getType())) {
            // Roi
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
        } else if ("QUEEN".equals(this.getType())) {
            // Reine
            // Générer les mouvements horizontaux, verticaux et diagonaux
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) {
                        for (int i = 1; i < 8; i++) {
                            int nouvelleX = this.getX() + i * dx;
                            int nouvelleY = this.getY() + i * dy;
                            if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                                mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        } else if ("ROOK".equals(this.getType())) {
            // Tour
            // Générer les mouvements horizontaux et verticaux
            for (int dx = -1; dx <= 1; dx++) {
                if (dx != 0) {
                    for (int i = 1; i < 8; i++) {
                        int nouvelleX = this.getX() + i * dx;
                        int nouvelleY = this.getY();
                        if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                            mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
                        }
                    }
                }
            }
            for (int dy = -1; dy <= 1; dy++) {
                if (dy != 0) {
                    for (int i = 1; i < 8; i++) {
                        int nouvelleX = this.getX();
                        int nouvelleY = this.getY() + i * dy;
                        if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                            mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
                        }
                    }
                }
            }
        } else if ("BISHOP".equals(this.getType())) {
            // Fou
            // Générer les mouvements diagonaux
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 && dy != 0) {
                        for (int i = 1; i < 8; i++) {
                            int nouvelleX = this.getX() + i * dx;
                            int nouvelleY = this.getY() + i * dy;
                            if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                                mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        } else if ("KNIGHT".equals(this.getType())) {
            // Cavalier
            // Générer les mouvements en L
            int[] dx = {1, 1, 2, 2, -1, -1, -2, -2};
            int[] dy = {2, -2, 1, -1, 2, -2, 1, -1};
            for (int i = 0; i < 8; i++) {
                int nouvelleX = this.getX() + dx[i];
                int nouvelleY = this.getY() + dy[i];
                if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                    mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
                }
            }
        } else if ("PAWN".equals(this.getType())) {
            // Pion
            int direction = this.getEquipe().equals("WHITE") ? -1 : 1; // Direction du mouvement du pion
            int startX = this.getEquipe().equals("WHITE") ? 6 : 1; // Rang de départ du pion

            // Mouvement simple en avant
            int nouvelleX = this.getX() + direction;
            int nouvelleY = this.getY();
            if (nouvelleX >= 0 && nouvelleX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                mouvementsPossibles.add(new int[]{nouvelleX, nouvelleY});
            }

            // Première avancée de deux cases depuis le rang de départ
            if (this.getX() == startX) {
                int deuxCasesX = this.getX() + 2 * direction;
                if (deuxCasesX >= 0 && deuxCasesX < 8 && nouvelleY >= 0 && nouvelleY < 8) {
                    mouvementsPossibles.add(new int[]{deuxCasesX, nouvelleY});
                }
            }

            // Captures en diagonale
            int[] deplacementsDiagonaux = {this.getY() - 1, this.getY() + 1};
            for (int dy : deplacementsDiagonaux) {
                if (dy >= 0 && dy < 8) {
                    mouvementsPossibles.add(new int[]{nouvelleX, dy});
                }
            }
        }

        return mouvementsPossibles;
    }



}

