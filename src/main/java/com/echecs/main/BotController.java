package com.echecs.main;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;
import java.util.*;

/**
 * Cette classe est l'affichage de la fenêtre de jeu quand on joue contre l'odinateur
 */
public class BotController {

    //Partie du code dédiée à la declaration des attributs de la classe
    @FXML
    private GridPane jeu;

    @FXML
    Button playPlayer;

    @FXML
    private Button boutonJouer;

    public List<Piece> pions = new ArrayList<>();

    private boolean tourBlanc = true; // true si c'est le tour des blancs, false si c'est le tour des noirs

    private Piece selectedPiece = null;

    private Double height = 0.0;

    private Double width = 0.0;

    private Bot joueurNoir;

    Stage stage;

    @FXML
    public ComboBox themeBox;

    @FXML
    public ComboBox pieceBox;


    /**
     * Méthode permettant de passer à l'interface de joueur contre joueur depuis un bouton
     *
     * @throws IOException
     */
    @FXML
    private void playPlayer() throws IOException {
        Stage stg = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        mainController controller = fxmlLoader.getController();
        stg.setOnShown(controller::setResizeEvents);

        stg.setTitle("Echecs");
        stg.setScene(scene);
        stg.show();
        stage.close();
    }


    //Partie du code dédiée a la mise en place d'une partie


    /**
     * Méthode initialisant une partie quand on clique sur le bouton jouer
     */
    @FXML
    private void jouerClicked() {
        tourBlanc = true; // Les blancs commencent toujours
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();
    }


    /**
     * Méthode permettant de reinitialiser le plateau
     */
    private void reinitialiserPlateau() {
        jeu.getChildren().clear();
        pions.clear();
        selectedPiece = null;

        joueurNoir = new Bot(jeu,"Black", false, this); // False signifie que c'est un bot

        for (int ligne = 0; ligne < 8; ++ligne) {
            for (int col = 0; col < 8; ++col) {
                VBox caseRect = createCase(ligne, col);
                jeu.add(caseRect, col, ligne);
            }
        }
    }

    /**
     * Méthode créeant les différentes cases du plateau sous forme de VBox
     *
     * @param ligne
     * @param col
     * @return
     */
    private VBox createCase(int ligne, int col) {
        VBox caseRect = new VBox();
        caseRect.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        caseRect.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
        caseRect.setAlignment(Pos.CENTER);
        caseRect.setOnMouseClicked(event -> clickEvent(caseRect));
        String selectedTheme = (String) themeBox.getValue();
        switch (selectedTheme) {
            case "Classique" -> {
                if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Paint.valueOf("#EBECD0")));
                else caseRect.setBackground(Background.fill(Paint.valueOf("#739552")));
            }
            case "Océan" -> {
                if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Paint.valueOf("#D5E0E5")));
                else caseRect.setBackground(Background.fill(Paint.valueOf("#779AB0")));
            }
            case "Bois" -> {
                if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Paint.valueOf("#EDD6B0")));
                else caseRect.setBackground(Background.fill(Paint.valueOf("#B88762")));
            }
            case "Pierre" -> {
                if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Paint.valueOf("#C8C3BC")));
                else caseRect.setBackground(Background.fill(Paint.valueOf("#545350")));
            }
            case "Violet" -> {
                if ((ligne + col) % 2 == 0) caseRect.setBackground(Background.fill(Paint.valueOf("#F0F1F0")));
                else caseRect.setBackground(Background.fill(Paint.valueOf("#8476BA")));
            }
        }
        return caseRect;
    }

    /**
     * Méthode configurant les pièces à leur position initiales sur le plateau
     */
    public void configurerPieces() {
        String selectedTheme = (String) pieceBox.getValue();
        switch (selectedTheme) {
            case "Classique" -> {
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
                        if (p != null) {
                            p.generateSymbol("Classique");
                            pions.add(p);
                        }
                    }
                }
            }
            case "Old school" -> {
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
                        if (p != null) {
                            p.generateSymbol("Old school");
                            pions.add(p);
                        }                    }
                }
            }
            case "Red vs Blue" -> {
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
                        if (p != null) {
                            p.generateSymbol("Red vs Blue");
                            pions.add(p);
                        }                    }
                }
            }
            case "Neo" -> {
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
                        if (p != null) {
                            p.generateSymbol("Neo");
                            pions.add(p);
                        }                    }
                }
            }
            case "Master" -> {
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
                        if (p != null) {
                            p.generateSymbol("Master");
                            pions.add(p);
                        }                    }
                }
            }
        }

    }

    /**
     * Méthode permettant la gestion d'un clic sur une pièce sur le plateau
     *
     * @param rect
     */
    private void clickEvent(Node rect) {
        int nouvelleLigne = GridPane.getRowIndex(rect);
        int nouvelleCol = GridPane.getColumnIndex(rect);

        if (selectedPiece == null) {
            selectionnerPiece(nouvelleLigne, nouvelleCol);
        } else {
            deplacerPiece(nouvelleLigne, nouvelleCol);
            if ((!tourBlanc)) joueurNoir.jouer();
        }
    }

    /**
     * Méthode permettant de gérer la selection d'une pièce
     *
     * @param ligne
     * @param col
     */
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

    /**
     * Méthode gérant le système de promotion d'un pion
     *
     * @param pion
     */
    private void promouvoirPion(Piece pion) {
        // Afficher une boîte de dialogue pour choisir la nouvelle pièce
        List<String> choix = List.of("QUEEN", "ROOK", "BISHOP", "KNIGHT");
        if(tourBlanc){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PromotionDialog.fxml"));
                Parent root = loader.load();

                PromotionDialogController controller = loader.getController();

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Promotion");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                String nouvellePieceType = controller.getSelectedPiece();
                if (nouvellePieceType == null) {
                    nouvellePieceType = "QUEEN";
                }

                // Remplacer le pion par la nouvelle pièce
                pion.setType(nouvellePieceType);
                pion.generateSymbol((String) pieceBox.getValue());

                // Mettre à jour l'affichage de la pièce
                VBox caseCible = (VBox) jeu.getChildren().get(pion.getX() * 8 + pion.getY());
                caseCible.getChildren().clear();
                caseCible.getChildren().add(pion.getSymbole());
                updateGameSize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            // Créer une instance de Random
            Random random = new Random();
            // Obtenir un index aléatoire de la liste
            int randomIndex = random.nextInt(choix.size());
            //choix aléatoire pour le bot
            String choice = choix.get(randomIndex);
            // Remplacer le pion par la nouvelle pièce
            pion.setType(choice);
            pion.generateSymbol((String) pieceBox.getValue());

            // Mettre à jour l'affichage de la pièce
            VBox caseCible = (VBox) jeu.getChildren().get(pion.getX() * 8 + pion.getY());
            caseCible.getChildren().clear();
            caseCible.getChildren().add(pion.getSymbole());
            updateGameSize();
        }

    }

    //méthode permettant de déplacer une pièce sur le plateau
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
           showAlert(tourBlanc);
        }
        else if (estPat(tourBlanc ? "BLACK" : "WHITE")) {
        showAlert2();
        }
        selectedPiece = null;
        //change le tour
        tourBlanc = !tourBlanc;
    }

    /**
     * Méthode mettant a jour les pièce déplacés sur le plateau
     */
    public void mettreAJourPieces() {
        jeu.getChildren().forEach(node -> ((VBox) node).getChildren().clear());
        for (Piece p : pions) {
            VBox uneCase = (VBox) jeu.getChildren().get(p.getX() * 8 + p.getY());
            uneCase.getChildren().add(p.getSymbole());
        }
    }


    /**
     * Méthode permettant d'initialiser certains éléments du stage à l'execution qui ont des comportements liés à des méthodes dans ce controller
     */
    @FXML
    private void initialize() {
        themeBox.setItems(FXCollections.observableArrayList("Classique", "Océan", "Bois","Pierre","Violet"));
        themeBox.getSelectionModel().selectFirst();

        pieceBox.setItems(FXCollections.observableArrayList("Classique", "Old school", "Red vs Blue","Neo","Master"));
        pieceBox.getSelectionModel().selectFirst();

        jouerClicked();
    }

    /**
     * Méthode permettant la gestion de la redimension de la fenêtre
     *
     * @param windowEvent
     */
    public void setResizeEvents(WindowEvent windowEvent) {
        // Get the stage
        stage = (Stage) boutonJouer.getScene().getWindow();

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

    /**
     * Méthode permettant d'adapter la taille des éléments après la redimension de la fenêtre
     */
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



    //Partie du code dédiée à la vérification d'un mouvement spécifique durant une partie


    /**
     * Méthode permettant de vérifier un déplacement d'une quelconque pièce
     *
     * @param piece
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
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

    /**
     * Méthode permettant de vérifier un mouvement d'un pion
     *
     * @param piece
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
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

    /**
     * Méthode permettant de vérifier le déplacement d'une tour
     *
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
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

    /**
     * Méthode permettant de vérifier le déplacement d'un cavalier
     *
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
    private String validerDeplacementCavalier(int col, int ligne, int colActuelle, int ligneActuelle) {
        int deltaX = Math.abs(col - colActuelle);
        int deltaY = Math.abs(ligne - ligneActuelle);
        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return "true";
        }
        return "false";
    }

    /**
     * Méthode permettant de vérifier le déplacement d'un fou
     *
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
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

    /**
     * Méthode permettant de vérifier le déplacement d'une reine
     *
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
    private String validerDeplacementReine(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (col == colActuelle || ligne == ligneActuelle) {
            return validerDeplacementTour(col, ligne, colActuelle, ligneActuelle);
        } else if (Math.abs(col - colActuelle) == Math.abs(ligne - ligneActuelle)) {
            return validerDeplacementFou(col, ligne, colActuelle, ligneActuelle);
        }
        return "false";
    }

    /**
     * Méthode permettant de vérifier le déplacement d'un roi
     *
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
    private String validerDeplacementRoi(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (Math.abs(col - colActuelle) <= 1 && Math.abs(ligne - ligneActuelle) <= 1) {
            return "true";
        }
        return "false";
    }

    /**
     * Méthode permettant de vérifier si il y a une pièce à une position donnée
     *
     * @param col
     * @param ligne
     * @return
     */
    public Piece getPieceAt(int col, int ligne) {
        for (Piece p : pions) {
            if (p.getX() == ligne && p.getY() == col) return p;
        }
        return null;
    }




    //Partie du code contenant les méthode de vérification de fin de partie et de mise en échec


    /**
     * Méthode permettant de vérifier si un roi est en échec
     *
     * @param equipe
     * @return
     */
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

    /**
     * Méthode permettant de vérifier si un déplacement est possible sans mettre le roi en échec
     *
     * @param piece
     * @param col
     * @param ligne
     * @param colActuelle
     * @param ligneActuelle
     * @return
     */
    public boolean deplacementPossibleSansEchec(Piece piece, int col, int ligne, int colActuelle, int ligneActuelle) {
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

    /**
     * Méthode permettant de vérifier si le chemin est libre entre l position initiale et la position d'arrivée d'une pièce (plus efficace que la version implémentée au dessus pour couvrir des larges vérifications comme l'échec et mat)
     *
     * @param piece
     * @param nouvelleX
     * @param nouvelleY
     * @return
     */
    private boolean cheminLibre(Piece piece, int nouvelleX, int nouvelleY) {
        int x = piece.getX();
        int y = piece.getY();

        // Calculer les directions de déplacement
        int dx = Integer.compare(nouvelleX, x); // 1, -1 ou 0
        int dy = Integer.compare(nouvelleY, y); // 1, -1 ou 0

        // Avancer jusqu'à la case cible
        x += dx;
        y += dy;
        while (x != nouvelleX || y != nouvelleY) {
            if (getPieceAt(x, y) != null) {
                return false; // Il y a une pièce sur le chemin
            }
            x += dx;
            y += dy;
        }
        return true; // Aucun obstacle trouvé
    }

    /**
     * Méthode permettant de trouver l'ensemble des mouvements légaux pour une pièce de type roi
     *
     * @param roi
     * @return
     */
    private List<int[]> genererMouvementsLegauxRoi(Piece roi) {
        List<int[]> mouvementsLegaux = new ArrayList<>();

        if (roi.getType().equals("KING")) {
            for (int[] mouvement : roi.genererMouvementsPossibles()) {
                int nouvelleX = mouvement[0];
                int nouvelleY = mouvement[1];
                Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);
                if (autrePieceSelectionnee == null || !autrePieceSelectionnee.getEquipe().equals(roi.getEquipe())) {
                    String valide = deplacementPieceValide(roi, nouvelleX, nouvelleY, roi.getX(), roi.getY());
                    if (!valide.equals("false")) {
                        if (deplacementPossibleSansEchec(roi, nouvelleY, nouvelleX, roi.getY(), roi.getX())) {
                            mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                        }
                    }
                }
            }
        }
        return mouvementsLegaux;
    }

    /**
     * Méthode permettant de trouver l'ensemble des mouvements légaux pour une pièce de type reine
     *
     * @param reine
     * @return
     */
    private List<int[]> genererMouvementsLegauxReine(Piece reine) {
        List<int[]> mouvementsLegaux = new ArrayList<>();

        if (reine.getType().equals("QUEEN")) {
            for (int[] mouvement : reine.genererMouvementsPossibles()) {
                int nouvelleX = mouvement[0];
                int nouvelleY = mouvement[1];
                Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);

                if (autrePieceSelectionnee == null || !autrePieceSelectionnee.getEquipe().equals(reine.getEquipe())) {
                    if (cheminLibre(reine, nouvelleX, nouvelleY)) {
                        String valide = deplacementPieceValide(reine, nouvelleX, nouvelleY, reine.getX(), reine.getY());
                        if (!valide.equals("false")) {
                            if (deplacementPossibleSansEchec(reine, nouvelleY, nouvelleX, reine.getY(), reine.getX())) {
                                mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        }
        return mouvementsLegaux;
    }

    /**
     * Méthode permettant de trouver l'ensemble des mouvements légaux pour une pièce de type fou
     *
     * @param fou
     * @return
     */
    private List<int[]> genererMouvementsLegauxFou(Piece fou) {
        List<int[]> mouvementsLegaux = new ArrayList<>();

        if (fou.getType().equals("BISHOP")) {
            for (int[] mouvement : fou.genererMouvementsPossibles()) {
                int nouvelleX = mouvement[0];
                int nouvelleY = mouvement[1];
                Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);

                if (autrePieceSelectionnee == null || !autrePieceSelectionnee.getEquipe().equals(fou.getEquipe())) {
                    if (cheminLibre(fou, nouvelleX, nouvelleY)) {
                        String valide = deplacementPieceValide(fou, nouvelleX, nouvelleY, fou.getX(), fou.getY());
                        if (!valide.equals("false")) {
                            if (deplacementPossibleSansEchec(fou, nouvelleY, nouvelleX, fou.getY(), fou.getX())) {
                                mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        }
        return mouvementsLegaux;
    }

    /**
     * Méthode permettant de trouver l'ensemble des mouvements légaux pour une pièce de type tour
     *
     * @param tour
     * @return
     */
    private List<int[]> genererMouvementsLegauxTour(Piece tour) {
        List<int[]> mouvementsLegaux = new ArrayList<>();

        if (tour.getType().equals("ROOK")) {
            for (int[] mouvement : tour.genererMouvementsPossibles()) {
                int nouvelleX = mouvement[0];
                int nouvelleY = mouvement[1];
                Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);

                if (autrePieceSelectionnee == null || !autrePieceSelectionnee.getEquipe().equals(tour.getEquipe())) {
                    if (cheminLibre(tour, nouvelleX, nouvelleY)) {
                        String valide = deplacementPieceValide(tour, nouvelleX, nouvelleY, tour.getX(), tour.getY());
                        if (!valide.equals("false")) {
                            if (deplacementPossibleSansEchec(tour, nouvelleY, nouvelleX, tour.getY(), tour.getX())) {
                                mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        }
        return mouvementsLegaux;
    }


    /**
     * Méthode permettant de trouver l'ensemble des mouvements légaux pour une pièce de type pion
     *
     * @param pion
     * @return
     */
    private List<int[]> genererMouvementsLegauxPion(Piece pion) {
        List<int[]> mouvementsLegaux = new ArrayList<>();

        if (pion.getType().equals("PAWN")) {
            for (int[] mouvement : pion.genererMouvementsPossibles()) {
                int nouvelleX = mouvement[0];
                int nouvelleY = mouvement[1];
                Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);

                // Vérifier si la case est vide
                if (autrePieceSelectionnee == null) {
                    String valide = deplacementPieceValide(pion, nouvelleX, nouvelleY, pion.getX(), pion.getY());
                    if (!valide.equals("false")) {
                        if (deplacementPossibleSansEchec(pion, nouvelleY, nouvelleX, pion.getY(), pion.getX())) {
                            mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                        }
                    }
                } else {
                    // Vérifier si la case contient une pièce adverse
                    if (!autrePieceSelectionnee.getEquipe().equals(pion.getEquipe())) {
                        String valide = deplacementPieceValide(pion, nouvelleX, nouvelleY, pion.getX(), pion.getY());
                        if (!valide.equals("false") && valide.equals("CAPTURE")) {
                            if (deplacementPossibleSansEchec(pion, nouvelleY, nouvelleX, pion.getY(), pion.getX())) {
                                mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        }

        return mouvementsLegaux;
    }

    /**
     * Méthode permettant de trouver l'ensemble des mouvements légaux pour une pièce de type cavalier
     *
     * @param cavalier
     * @return
     */
    private List<int[]> genererMouvementsLegauxCavalier(Piece cavalier) {
        List<int[]> mouvementsLegaux = new ArrayList<>();

        if (cavalier.getType().equals("KNIGHT")) {
            for (int[] mouvement : cavalier.genererMouvementsPossibles()) {
                int nouvelleX = mouvement[0];
                int nouvelleY = mouvement[1];
                Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);

                // Vérifier si la case est vide ou contient une pièce adverse
                if (autrePieceSelectionnee == null || !autrePieceSelectionnee.getEquipe().equals(cavalier.getEquipe())) {
                    String valide = deplacementPieceValide(cavalier, nouvelleX, nouvelleY, cavalier.getX(), cavalier.getY());
                    if (!valide.equals("false")) {
                        if (deplacementPossibleSansEchec(cavalier, nouvelleY, nouvelleX, cavalier.getY(), cavalier.getX())) {
                            mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                        }
                    }
                }
            }
        }
        return mouvementsLegaux;
    }

    /**
     * Méthode permettant de trouver le roi d'une équipe donnée sur le plateau
     *
     * @param equipe
     * @return
     */
    private Piece trouverRoi(String equipe) {
        for (Piece piece : pions) {
            if (piece.getType().equals("KING") && piece.getEquipe().equals(equipe)) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Méthode permettant de vérifer si un roi donné est en situation d'échec et mat
     *
     * @param roi
     * @return
     */
    private boolean estEchecEtMat(Piece roi) {
        if (!roiEnEchec(roi.getEquipe())) {
            return false;
        }
        List<int[]> mouvementsLegaux = genererMouvementsLegauxRoi(roi);
        if(mouvementsLegaux.isEmpty()) {
            List<Piece> copiePions = new ArrayList<>(pions);
            List<List<int[]>> mouvementsLegaux2 = new ArrayList<>(List.of());
            for (Piece piece : copiePions) {
                if (piece.getEquipe().equals(roi.getEquipe())) {
                    if (piece.getType().equals("KNIGHT")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxCavalier(piece));
                    }
                    if (piece.getType().equals("ROOK")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxTour(piece));
                    }
                    if (piece.getType().equals("BISHOP")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxFou(piece));
                    }
                    if (piece.getType().equals("QUEEN")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxReine(piece));
                    }
                    if (piece.getType().equals("PAWN")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxPion(piece));
                    }
                }
            }
            for (int i = 0; i < mouvementsLegaux2.size(); i++) {
                if (mouvementsLegaux2.get(i).isEmpty()) {
                    mouvementsLegaux2.remove(i);
                    i--; // Décrémenter i, car la taille de la liste a changé
                }
            }
            return mouvementsLegaux2.isEmpty() ;
        }
        return false;
    }


    /**
     * Méthode permettant de vérifier si une équipe donnée est en situation de pat
     *
     * @param equipe
     * @return
     */
    private boolean estPat(String equipe) {

        if (roiEnEchec(equipe)) {
            return false;
        }
        List<int[]> mouvementsLegaux = genererMouvementsLegauxRoi(Objects.requireNonNull(trouverRoi("BLACK")));
        if(mouvementsLegaux.isEmpty()) {
            List<Piece> copiePions = new ArrayList<>(pions);
            List<List<int[]>> mouvementsLegaux2 = new ArrayList<>(List.of());
            for (Piece piece : copiePions) {
                if (piece.getEquipe().equals(equipe)) {
                    if (piece.getType().equals("KNIGHT")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxCavalier(piece));
                    }
                    if (piece.getType().equals("ROOK")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxTour(piece));
                    }
                    if (piece.getType().equals("BISHOP")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxFou(piece));
                    }
                    if (piece.getType().equals("QUEEN")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxReine(piece));
                    }
                    if (piece.getType().equals("PAWN")) {
                        mouvementsLegaux2.add(genererMouvementsLegauxPion(piece));
                    }
                }
            }

            for (int i = 0; i < mouvementsLegaux2.size(); i++) {
                if (mouvementsLegaux2.get(i).isEmpty()) {
                    mouvementsLegaux2.remove(i);
                    i--; // Décrémenter i, car la taille de la liste a changé
                }
            }
            return mouvementsLegaux2.isEmpty();
        }
        return false;
    }





    //Partie du code dédiée au méthode de gestion d'alertes de fin de partie


    /**
     * Méthode permettant de générer une vbox affichant le résultat d'une partie en cas d'échec et mat
     *
     * @param tourBlanc
     */
    public void showAlert(boolean tourBlanc) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EchecEtMat.fxml"));
            VBox alertRoot = fxmlLoader.load();
            echecEtMatController controller = fxmlLoader.getController();
            controller.setMessage((tourBlanc ? "Les Blancs" : "Les Noirs") + " gagnent.");
            Stage alertStage = new Stage();
            alertStage.setTitle("Échec et mat");
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.setScene(new Scene(alertRoot));
            alertStage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Méthode permettant de générer une vbox affichant le résultat d'une partie en cas de pat
     */
    public void showAlert2() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("estPat.fxml"));
            VBox alertRoot = fxmlLoader.load();
            Stage alertStage = new Stage();
            alertStage.setTitle("Pat");
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.setScene(new Scene(alertRoot));
            alertStage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


