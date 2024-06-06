package com.echecs.main;


import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.util.*;

public class BotController {
    @FXML
    private GridPane jeu;

    @FXML
    private Button boutonJouer;

    public List<Piece> pions = new ArrayList<>();
    private boolean tourBlanc = true; // true si c'est le tour des blancs, false si c'est le tour des noirs

    private Piece selectedPiece = null;

    private Double height = 0.0;
    private Double width = 0.0;

    private Bot joueurNoir;
    private Bot joueurBlanc;

    @FXML
    private void jouerClicked() {
        tourBlanc = true; // Les blancs commencent toujours
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();
    }

    private void reinitialiserPlateau() {
        jeu.getChildren().clear();
        pions.clear();
        selectedPiece = null;

        joueurNoir = new Bot(jeu,"Black", false, this); // False signifie que c'est un bot
        joueurBlanc = new Bot(jeu, "WHITE", true, this); // True signifie que c'est un humain

        for (int ligne = 0; ligne < 8; ++ligne) {
            for (int col = 0; col < 8; ++col) {
                VBox caseRect = createCase(ligne, col);
                jeu.add(caseRect, col, ligne);
            }
        }
    }

    private VBox createCase(int ligne, int col) {
        VBox caseRect = new VBox();
        caseRect.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        caseRect.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
        caseRect.setAlignment(Pos.CENTER);
        caseRect.setOnMouseClicked(event -> clickEvent(caseRect));
        if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Color.WHITE));
        else caseRect.setBackground(Background.fill(Paint.valueOf("#6bbd41")));
        return caseRect;
    }

    public void configurerPieces() {
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
            if(!tourBlanc) joueurNoir.jouer();
        }
    }

    public void selectionnerPiece(int ligne, int col) {
        for (Piece pion : pions) {
            if (pion.getX() == ligne && pion.getY() == col) {
                if ((tourBlanc && "WHITE".equals(pion.getEquipe())) || (!tourBlanc && "BLACK".equals(pion.getEquipe()))) {
                    selectedPiece = pion;
                    VBox selectedCase = (VBox) jeu.getChildren().get(selectedPiece.getX() * 8 + selectedPiece.getY());
                    selectedCase.setStyle("-fx-border-color: green; -fx-border-style: solid; -fx-border-width: 10;");
                }
                break;
            }
        }
    }

    private boolean roiEnEchec(String equipe) {
        // Trouver la position du roi
        int roiX = -1;
        int roiY = -1;
        for (Piece p : pions) {
            if ("KING".equals(p.getType()) && equipe.equals(p.getEquipe())) {
                roiX = p.getX();
                roiY = p.getY();
                break;
            }
        }
        if (roiX == -1 || roiY == -1) return false; // Roi introuvable

        // Vérifier si une pièce adverse peut capturer le roi
        for (Piece p : pions) {
            if (!equipe.equals(p.getEquipe())) {
                String valide = deplacementPieceValide(p, p.getY(), p.getX(), roiY, roiX);
                if (valide.equals("CAPTURE")||valide.equals("true")) return true;
            }
        }
        return false;
    }

    private boolean deplacementPossibleSansEchec(Piece piece, int col, int ligne, int colActuelle, int ligneActuelle) {
        // Vérifier si le déplacement met le roi en échec
        Piece pieceTemp = null;
        for (Piece p : pions) {
            if (p.getX() == ligne && p.getY() == col) {
                pieceTemp = p;
                break;
            }
        }

        // Simuler le déplacement pour vérifier l'échec
        if (pieceTemp != null) {
            pions.remove(pieceTemp);
        }
        piece.setX(ligne);
        piece.setY(col);
        boolean enEchec = roiEnEchec(piece.getEquipe());
        piece.setX(ligneActuelle);
        piece.setY(colActuelle);
        if (pieceTemp != null) {
            pions.add(pieceTemp);
        }
        return !enEchec;
    }


    private List<int[]> genererTousLesMouvementsLegaux(String equipe) {
        List<int[]> mouvementsLegaux = new ArrayList<>();
        List<Piece> copiePions = new ArrayList<>(pions); // Créer une copie de la liste des pièces pour éviter la modification concurrente

        for (Piece piece : copiePions) {
            if (piece.getEquipe().equals(equipe)) {
                // Pour chaque pièce, générer les mouvements possibles
                for (int[] mouvement : piece.genererMouvementsPossibles()) {
                    int nouvelleX = mouvement[0];
                    int nouvelleY = mouvement[1];

                    if (deplacementPossibleSansEchec(piece, nouvelleY, nouvelleX, piece.getY(), piece.getX())) {
                        mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                    }
                }
            }
        }
        return mouvementsLegaux;
    }

    private List<int[]> genererTousLesMouvementsLegauxSansEchec(String equipe) {
        List<int[]> mouvementsLegaux = new ArrayList<>();
        List<Piece> copiePions = new ArrayList<>(pions); // Créer une copie de la liste des pièces pour éviter la modification concurrente

        for (Piece piece : copiePions) {
            if (piece.getEquipe().equals(equipe)) {
                // Pour chaque pièce, générer les mouvements possibles
                for (int[] mouvement : piece.genererMouvementsPossibles()) {
                    int nouvelleX = mouvement[0];
                    int nouvelleY = mouvement[1];

                    if (deplacementPossibleSansEchec(piece, nouvelleY, nouvelleX, piece.getY(), piece.getX())) {
                        mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                    }
                }
            }
        }
        return mouvementsLegaux;
    }

    private boolean estPat(String equipe) {
        if (roiEnEchec(equipe)) {
            return false;
        }

        List<int[]> mouvementsLegaux = genererTousLesMouvementsLegaux(equipe);
        return mouvementsLegaux.isEmpty();
    }

    private boolean estEchecEtMat(Piece roi) {
        if (!roiEnEchec(roi.getEquipe())) {
            return false;
        }
        List<int[]> mouvementsLegaux = genererTousLesMouvementsLegaux(roi.getEquipe());
        System.out.println(mouvementsLegaux.size());
        for (int[] mouvement : mouvementsLegaux) {
            VBox selectedCase = (VBox) jeu.getChildren().get(mouvement[0] * 8 + mouvement[1]);
            selectedCase.setStyle("-fx-border-color: yellow; -fx-border-style: solid; -fx-border-width: 10;");
        }
        return mouvementsLegaux.isEmpty();
    }

    private Piece trouverRoi(String equipe) {
        for (Piece piece : pions) {
            if (piece.getType().equals("KING") && piece.getEquipe().equals(equipe)) {
                return piece;
            }
        }
        return null;
    }
    private void promouvoirPion(Piece pion) {
        // Afficher une boîte de dialogue pour choisir la nouvelle pièce
        List<String> choix = List.of("QUEEN", "ROOK", "BISHOP", "KNIGHT");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("QUEEN", choix);
        dialog.setTitle("Promotion");
        dialog.setHeaderText(null);
        dialog.setContentText("Choisissez une pièce pour la promotion:");

        // Récupérer le choix de l'utilisateur
        Optional<String> result = dialog.showAndWait();
        String nouvellePieceType = result.orElse("QUEEN");

        // Remplacer le pion par la nouvelle pièce
        pion.setType(nouvellePieceType);
        pion.generateSymbol();

        // Mettre à jour l'affichage de la pièce
        VBox caseCible = (VBox) jeu.getChildren().get(pion.getX() * 8 + pion.getY());
        caseCible.getChildren().clear();
        caseCible.getChildren().add(pion.getSymbole());
        updateGameSize();
    }

    public void deplacerPiece(int nouvelleLigne, int nouvelleCol) {
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

        if (!deplacementPossibleSansEchec(selectedPiece, nouvelleCol, nouvelleLigne, selectedPiece.getY(), selectedPiece.getX())) {
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
        selectedPiece.setHasMoved(true);



        // Vérifier la promotion
        if ("PAWN".equals(selectedPiece.getType()) && (nouvelleLigne == 0 || nouvelleLigne == 7)) {
            promouvoirPion(selectedPiece);
        }

        // Vérification de l'échec et mat après chaque déplacement
        Piece roi = trouverRoi(tourBlanc ? "BLACK" : "WHITE");
        if (estEchecEtMat(Objects.requireNonNull(roi))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Échec et mat");
            alert.setHeaderText(null);
            alert.setContentText("Echec et mat ! " + (tourBlanc ? "Les Blancs" : "Les Noirs") + " gagnent.");
            alert.showAndWait();
        }
        else if (estPat(tourBlanc ? "BLACK" : "WHITE")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pat");
            alert.setHeaderText(null);
            alert.setContentText("Pat ! La partie est nulle.");
            alert.showAndWait();
        }
        selectedPiece = null;
        //change le tour
        tourBlanc = !tourBlanc;
    }

    public void mettreAJourPieces() {
        jeu.getChildren().forEach(node -> ((VBox) node).getChildren().clear());
        for (Piece p : pions) {
            VBox uneCase = (VBox) jeu.getChildren().get(p.getX() * 8 + p.getY());
            uneCase.getChildren().add(p.getSymbole());
        }
    }


    public String deplacementPieceValide(Piece piece, int col, int ligne, int colActuelle, int ligneActuelle) {
        if (colActuelle < 0 || colActuelle >= 8 || ligneActuelle < 0 || ligneActuelle >= 8) return "false";
        if(colActuelle == col && ligneActuelle == ligne) return "false";

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
        if (equipePiece.equals("WHITE")) {
            if (col == colActuelle && ligne == ligneActuelle + 1) return "AVANCE";
            if (!piece.getHasMoved() && col == colActuelle && ligne == ligneActuelle + 2 && getPieceAt(col, ligneActuelle+1) == null && getPieceAt(col, ligneActuelle) == null) return "AVANCE";
            if (Math.abs(col - colActuelle) == 1 && ligne == ligneActuelle + 1) return "CAPTURE";
        } else if (equipePiece.equals("BLACK")) {
            if (col == colActuelle && ligne == ligneActuelle - 1) return "AVANCE";
            if (!piece.getHasMoved() && col == colActuelle && ligne == ligneActuelle - 2 && getPieceAt(col, ligneActuelle-1) == null && getPieceAt(col, ligneActuelle) == null) return "AVANCE";
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

    public Piece getPieceAt(int col, int ligne) {
        for (Piece p : pions) {
            if (p.getX() == ligne && p.getY() == col) return p;
        }
        return null;
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
        double superVal;
        if(width > height) superVal = height;
        else superVal = width;

        superVal = superVal - 100;

        jeu.setMaxWidth(superVal);
        jeu.setPrefWidth(superVal);
        jeu.setMaxHeight(superVal);
        jeu.setPrefHeight(superVal);

        // update the size of each piece /8
        for(int i = 0; i < jeu.getChildren().size(); ++i) {
            VBox piece = (VBox) jeu.getChildren().get(i);
            piece.setPrefWidth(superVal/8);
            piece.setPrefHeight(superVal/8);
            if(!piece.getChildren().isEmpty()) {
                ImageView symbole = (ImageView) piece.getChildren().getFirst();
                symbole.setFitWidth(superVal/8);
                symbole.setFitHeight(superVal/8);
            }
        }
    }
}


