package com.echecs.main;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class mainController {

    //partie contenant la déclaration de l'ensemble des variables (attributs) utilisés dans cette classe

    private Stage stage;

    private Stage selectStage;

    @FXML
    private GridPane jeu;

    @FXML
    private Button boutonJouer;
    @FXML
    private Button boutonTournoi;

    @FXML
    private Button playComputer;

    @FXML
    private Button selectPartie;
    @FXML
    private Button selectReplay;
    @FXML
    private Button selectStats;

    @FXML
    private VBox selectedPartie;
    @FXML
    private VBox selectedReplay;

    @FXML
    private Label labelTempsBlancs; // Label pour afficher le temps restant pour les blancs

    @FXML
    private Label labelTempsNoirs; // Label pour afficher le temps restant pour les noirs

    @FXML
    private ComboBox<String> tempsComboBox;

    @FXML
    private Label joueur1;
    @FXML
    private Label joueur2;

    private boolean tournoi = false;

    private int tempsInitialBlancs ;// Temps initial en secondes pour les blancs

    private int tempsInitialNoirs ; // Temps initial en secondes pour les noirs (10 minutes)

    private int tempsRestantBlancs = tempsInitialBlancs; // 10 minutes en secondes

    private int tempsRestantNoirs = tempsInitialNoirs; // 10 minutes en secondes

    private Timeline timeline ;

    public List<Piece> pions = new ArrayList<>();

    private boolean tourBlanc = true; // true si c'est le tour des blancs, false si c'est le tour des noirs

    private Piece selectedPiece = null;

    private Double height = 0.0;

    private Double width = 0.0;

    private final List<Joueur> joueursListe = new ArrayList<>();

    private int joueursSize = 2;

    private List<Joueur> joueursPartie = new ArrayList<>();

    private Joueur joueur1Actuel;

    private Joueur joueur2Actuel;

    private final List<Joueur> nextPartieJoueurs = new ArrayList<>();

    private File logFile;

    private String data = " ";

    private static String DIRECTORY_PATH = "src/main/resources/com/echecs/main/partyFiles/";

    private List<Coordinate> coordinates;

    private String fileName;

    @FXML
    private VBox selectedStats;


    //méthode permettant de switcher sr l'interface de partie avec un bot
    @FXML
    private void playComputer() throws IOException {
        Stage stg = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("bot.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        BotController controller = fxmlLoader.getController();
        stg.setOnShown(controller::setResizeEvents);

        stg.setTitle("Echecs");
        stg.setScene(scene);
        stg.show();

        stage.close();
        selectStage.close();
    }


    //Partie de code gérant la mise en place du timer dans le jeu


    //Méthode permettant de démarrer le timer au début d'une partie
    private void demarrerTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Décrémente le temps restant pour chaque équipe
            if (tourBlanc) {
                tempsRestantBlancs--;
                // Met à jour l'affichage du temps restant pour les blancs
                // (par exemple, en mettant à jour un label dans votre interface utilisateur)
                // Supposons que vous ayez un label nommé labelTempsBlancs pour afficher le temps restant pour les blancs
                labelTempsBlancs.setText(formaterTemps(tempsRestantBlancs));
                if (tempsRestantBlancs <= 0) {
                    terminerPartie("Les Noirs remportent la partie par temps écoulé !");
                    timeline.stop();

                    joueur2Actuel.setNbPartiesGagne(joueur2Actuel.getNbPartiesGagne() + 1);
                    joueur1Actuel.setNbParties((joueur1Actuel.getNbParties() + 1));
                    joueur2Actuel.setNbParties((joueur2Actuel.getNbParties() + 1));

                    // load players, modify players to update the number of games played and games won, save players
                    ArrayList<Joueur> players = loadPlayers();
                    players.forEach(j -> {
                        if(j.getNomJoueur().equals(joueur1Actuel.getNomJoueur())) {
                            j.setNbParties(joueur1Actuel.getNbParties());
                            j.setNbPartiesGagne(joueur1Actuel.getNbPartiesGagne());
                        }
                        if(j.getNomJoueur().equals(joueur2Actuel.getNomJoueur())) {
                            j.setNbParties(joueur2Actuel.getNbParties());
                            j.setNbPartiesGagne(joueur2Actuel.getNbPartiesGagne());
                        }
                    });
                    savePlayers(players);

                    if(tournoi) {
                        nextPartieJoueurs.add(joueur2Actuel);
                        finPartie();
                    }
                }
            } else {
                tempsRestantNoirs--;
                // Met à jour l'affichage du temps restant pour les noirs
                // (par exemple, en mettant à jour un label dans votre interface utilisateur)
                // Supposons que vous ayez un label nommé labelTempsNoirs pour afficher le temps restant pour les noirs
                labelTempsNoirs.setText(formaterTemps(tempsRestantNoirs));
                if (tempsRestantNoirs <= 0) {
                    terminerPartie("Les Blancs remportent la partie par temps écoulé !");
                    timeline.stop();

                    joueur1Actuel.setNbPartiesGagne(joueur2Actuel.getNbPartiesGagne() + 1);
                    joueur1Actuel.setNbParties((joueur1Actuel.getNbParties() + 1));
                    joueur2Actuel.setNbParties((joueur2Actuel.getNbParties() + 1));

                    // load players, modify players to update the number of games played and games won, save players
                    ArrayList<Joueur> players = loadPlayers();
                    players.forEach(j -> {
                        if(j.getNomJoueur().equals(joueur1Actuel.getNomJoueur())) {
                            j.setNbParties(joueur1Actuel.getNbParties());
                            j.setNbPartiesGagne(joueur1Actuel.getNbPartiesGagne());
                        }
                        if(j.getNomJoueur().equals(joueur2Actuel.getNomJoueur())) {
                            j.setNbParties(joueur2Actuel.getNbParties());
                            j.setNbPartiesGagne(joueur2Actuel.getNbPartiesGagne());
                        }
                    });
                    savePlayers(players);

                    if(tournoi) {
                        nextPartieJoueurs.add(joueur1Actuel);
                        finPartie();
                    }
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    //Méthode permettantréorganisation complète de mainController et gestion de quelques bugs de réniatiliser le timer du jeu à la fin d'une partie
    private void reinitialiserTimer() {
        // Arrête la timeline si elle est en cours
        if (timeline != null) {
            timeline.stop();
        }

        // Réinitialise les temps restants pour les blancs et les noirs (si nécessaire)
        tempsRestantBlancs = tempsInitialBlancs;
        tempsRestantNoirs = tempsInitialNoirs;

        // Met à jour l'affichage des temps restants (si nécessaire)
        labelTempsBlancs.setText(formaterTemps(tempsRestantBlancs));
        labelTempsNoirs.setText(formaterTemps(tempsRestantNoirs));

        // Redémarre la timeline
        demarrerTimer();
    }

    // Méthode pour formater le temps restant en format minutes:secondes
    private String formaterTemps(int tempsRestant) {
        int minutes = tempsRestant / 60;
        int secondes = tempsRestant % 60;
        return String.format("%02d:%02d", minutes, secondes);
    }



    // Méthode permettant d'afficher un message de fin de partie quand le chrono 'une des deux équipes atteint zéro
    private void terminerPartie(String message) {
        // Afficher un message indiquant la fin de la partie
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }




    //Partie du code permettant de gérer l'initialisation correcte de la fenêtre et le lancement correct d'une partie




    @FXML
    private void initialize() {
        tempsComboBox.setItems(FXCollections.observableArrayList("10", "3", "1"));
        tempsComboBox.getSelectionModel().selectFirst();


        fileList = FXCollections.observableArrayList();
        listView.setItems(fileList);
        watchDir = Paths.get(System.getProperty("user.dir"), DIRECTORY_PATH);

        // Handle double-click events on the ListView items
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // Double click
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Call your method to read and replay the chess game
                    try {
                        playMovesFromFile(DIRECTORY_PATH + selectedItem);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        new Thread(this::initializeWatchService).start();
    }

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

    public void setPlayer(Joueur j) {
        if(j == null) return;
        joueursListe.add(j);
        selectStage.close();
        if(joueursListe.size() >= joueursSize) {
            // joueur 1 blanc
            joueur1.setText(joueursListe.get(0).getNomJoueur());
            joueur1Actuel = joueursListe.get(0);
            // joueur 2 noir
            joueur2.setText(joueursListe.get(1).getNomJoueur());
            joueur2Actuel = joueursListe.get(1);
            if(!tournoi) jouerClicked();
            else jouerTournoi();
        } else launchPlayer("J"+(joueursListe.size()+1));
    }

    public void launchPlayer(String nomJoueur) {
        try {
            if(selectStage != null) selectStage.close();
            selectStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("PlayerSelection.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 500);
            PlayerSelectionController controller = fxmlLoader.getController();
            controller.setMain(this);

            selectStage.setTitle(nomJoueur);
            selectStage.setScene(scene);
            selectStage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    //Méthode permettant de lancer une partie quand on clique sur le bouton jouer
    @FXML
    private void jouerClicked() {
        //appel la fonction pour créer le fichier

        if(joueursListe.size() < joueursSize) {
            tournoi = false;
            joueursSize = 2;
            launchPlayer("J1");
            return;
        }

        createLogFile();
        tourBlanc = true; // Les blancs commencent toujours
        // Récupérer le temps initial sélectionné dans le ComboBox
        String selectedTemps = tempsComboBox.getValue();
        tempsInitialBlancs = Integer.parseInt(selectedTemps) * 60;
        tempsInitialNoirs = Integer.parseInt(selectedTemps) * 60;

        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();
        reinitialiserTimer(); // Démarrer le timer lorsque le jeu commence
    }




    //Partie de code pour la génération du plateau et des pièces


    //méthode permettant de réniatiliser le plateau à chaque fin de partie
    private void reinitialiserPlateau() {
        jeu.getChildren().clear();
        pions.clear();
        selectedPiece = null;
        for (int ligne = 0; ligne < 8; ++ligne) {
            for (int col = 0; col < 8; ++col) {
                VBox caseRect = createCase(ligne, col);
                jeu.add(caseRect, col, ligne);
            }
        }
    }

    //Méthode créeant les différentes cases du plateau sous forme de VBox
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

    //Méthode configurant les pièces à leur position initiales sur le plateau
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




    //Partie du code contenant des méthodes de gestions de sélection des pièces sur le plateau, de déplacement de celles-ci et également de promotion de celles-ci dans les cas qui s'y prêtent




    //Méthode permettant de récupérer les information d'un clique sur une case pour ainsi pouvoir sélectionner et déplacer une pièce dans une autre case
    private void clickEvent(Node rect) {
        int nouvelleLigne = GridPane.getRowIndex(rect);
        int nouvelleCol = GridPane.getColumnIndex(rect);

        if (selectedPiece == null) {
            selectionnerPiece(nouvelleLigne, nouvelleCol);
        } else {
            deplacerPiece(nouvelleLigne, nouvelleCol);

        }
    }

    //méthode permettant de sélectionner une pièce
    public void selectionnerPiece(int ligne, int col) {
        for (Piece pion : pions) {
            if (pion.getX() == ligne && pion.getY() == col) {
                if ((tourBlanc && "WHITE".equals(pion.getEquipe())) || (!tourBlanc && "BLACK".equals(pion.getEquipe()))) {
                    selectedPiece = pion;

                    //ajoute les coordonnée dans le fichier texte
                    if(!tournoi) {
                        data = selectedPiece.getX() + "," + selectedPiece.getY() + "\n";
                        ajoutDataToFile(data);
                        coordinates = readCoordinatesFromFile();
                    }
                    VBox selectedCase = (VBox) jeu.getChildren().get(selectedPiece.getX() * 8 + selectedPiece.getY());
                    selectedCase.setStyle("-fx-border-color: green; -fx-border-style: solid; -fx-border-width: 10;");
                }
                break;
            }
        }
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

    //Méthode permettant de déplacer une pièce en respectant toutes les règles des échecs tout en vérifiant les différentes connditions de fins de partie permettant de mettre fin à celle-ci après un mouvement décisif
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
            timeline.stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Échec et mat");
            alert.setHeaderText(null);
            alert.setContentText("Echec et mat ! " + (tourBlanc ? "Les Blancs" : "Les Noirs") + " gagnent.");
            alert.show();

            // update the number of games played and games won for the players
            joueur1Actuel.setNbParties(joueur1Actuel.getNbParties() + 1);
            joueur2Actuel.setNbParties(joueur2Actuel.getNbParties() + 1);
            if(tourBlanc) joueur1Actuel.setNbPartiesGagne(joueur1Actuel.getNbPartiesGagne() + 1);
            else joueur2Actuel.setNbPartiesGagne(joueur2Actuel.getNbPartiesGagne() + 1);

            // load players, modify players to update the number of games played and games won, save players
            ArrayList<Joueur> players = loadPlayers();
            players.forEach(j -> {
                if(j.getNomJoueur().equals(joueur1Actuel.getNomJoueur())) {
                    j.setNbParties(joueur1Actuel.getNbParties());
                    j.setNbPartiesGagne(joueur1Actuel.getNbPartiesGagne());
                }
                if(j.getNomJoueur().equals(joueur2Actuel.getNomJoueur())) {
                    j.setNbParties(joueur2Actuel.getNbParties());
                    j.setNbPartiesGagne(joueur2Actuel.getNbPartiesGagne());
                }
            });
            savePlayers(players);

            if(tournoi) {
                nextPartieJoueurs.add(tourBlanc ? joueur1Actuel : joueur2Actuel);
                finPartie();
            }
        }
        else if (estPat(tourBlanc ? "BLACK" : "WHITE")) {
            timeline.stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pat");
            alert.setHeaderText(null);
            alert.setContentText("Pat ! La partie est nulle.");
            alert.show();

            // on retente la partie (ajout des 2 joueurs au tout debut et on retente)
            if(tournoi) {
                joueursPartie.addLast(joueur2Actuel);
                joueursPartie.addFirst(joueur1Actuel);
                runPartieTournoi();
            }
        }
        if(!tournoi) {
            //ajoute les coordonnée dans le fichier texte
            data = selectedPiece.getX() + "," + selectedPiece.getY() + "\n";
            ajoutDataToFile(data);
            coordinates = readCoordinatesFromFile();
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



    //Partie du code contenant des méthodes de vérification de mouvements pour tout type de pièce



    //Méthode permettant de vérifier si le déplacement d'une pièce quelconque vers une position donnée est possible
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

    //Méthode permettant de vérifier si le céplacement d'un pion est valide
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

    //Méthode permettant de vérifier si le céplacement d'une tour est valide
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

    //Méthode permettant de vérifier si le déplacement d'un cavalier est valide
    private String validerDeplacementCavalier(int col, int ligne, int colActuelle, int ligneActuelle) {
        int deltaX = Math.abs(col - colActuelle);
        int deltaY = Math.abs(ligne - ligneActuelle);
        if ((deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2)) {
            return "true";
        }
        return "false";
    }

    //Méthode permettant de vérifier si le déplacement d'un fou est valide
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

    //Méthode permettant de vérifier si le déplacement d'une reine est valide
    private String validerDeplacementReine(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (col == colActuelle || ligne == ligneActuelle) {
            return validerDeplacementTour(col, ligne, colActuelle, ligneActuelle);
        } else if (Math.abs(col - colActuelle) == Math.abs(ligne - ligneActuelle)) {
            return validerDeplacementFou(col, ligne, colActuelle, ligneActuelle);
        }
        return "false";
    }

    //Méthode permettant de vérifier si le déplacement d'un roi est valide
    private String validerDeplacementRoi(int col, int ligne, int colActuelle, int ligneActuelle) {
        if (Math.abs(col - colActuelle) <= 1 && Math.abs(ligne - ligneActuelle) <= 1) {
            return "true";
        }
        return "false";
    }

    //Méthode permettant de vérifier si une pièce se trouve aux positions données
    public Piece getPieceAt(int col, int ligne) {
        for (Piece p : pions) {
            if (p.getX() == ligne && p.getY() == col) return p;
        }
        return null;
    }





    //Partie du code contenant des méthodes gérant la vérification des conditions de fin d'une partie sous forme d'échec et mat ou pat





    //Méthode permettant de trouver un roi d'une équipe renseignée sur le plateau
    private Piece trouverRoi(String equipe) {
        for (Piece piece : pions) {
            if (piece.getType().equals("KING") && piece.getEquipe().equals(equipe)) {
                return piece;
            }
        }
        return null;
    }

    //Méthode permettant de vérifier si un roi d'une équipe renseigné est en échec
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

    //Méthode permettant de vérifier si le déplacement d'une pièce est possible sans mettre en échec ou maintenir la mise en échec du roi de son équipe
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

    //méthode nécéssaire car celle utilisé pour les pièces précédémment dans le code créé des pertes mémoires vues qu'elles sont utilisées sur un ensemble de mouvements légaux sur une pièce qui peut être relativement grand
    //Par ailleurs, cette méthode vérifie si une pièce se situe entre la position acutelle et la position à atteindre d'une picèce
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

    //Méthode permettant de générer tous les mouvements légaux dune pièce de type roi
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

    //Méthode permettant de générer tous les mouvements légaux dune pièce de type reine
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

    //Méthode permettant de générer tous les mouvements légaux dune pièce de type fou
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

    //Méthode permettant de générer tous les mouvements légaux dune pièce de type tour
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

    //Méthode permettant de générer tous les mouvements légaux dune pièce de type pion
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

    //Méthode permettant de générer tous les mouvements légaux dune pièce de type cavalier
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

    //Méthode permettant de vérifier si une pièce Roi est dans une situation d'échec et mat
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
                    i--; // Décrémenter i car la taille de la liste a changé
                }
            }
            return mouvementsLegaux2.isEmpty();
        }
        return false;
    }


    //Méthode permettant de vérifier si une équipe est en situation de pat
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
                    i--; // Décrémenter i car la taille de la liste a changé
                }
            }
            return mouvementsLegaux2.isEmpty();
        }
        return false;
    }





//Partie du code contenant la gestion de l'implémentation d'un mode de tournoi à 8 joueurs sur le jeu
    //Beug de d'alternation de couleur d'équipe qui commencent la partie à gérer





    @FXML
    // jouerTournoi est appelé lorsque le bouton "Jouer Tournoi" est cliqué
    // cela permet de lancer un tournoi entre les joueurs sélectionnés
    // on va se servir de setJoueursPartieTournoi pour charger les parties qui doivent être jouées
    private void jouerTournoi() {
        if(joueursListe.size() < joueursSize) {
            tourBlanc = true;
            tournoi = true;
            joueursSize = 8;
            launchPlayer("J1");
            return;
        }
        tourBlanc = true;
        setJoueursPartieTournoi();
    }

    // fonction appelée dans jouerTournoi qui permet de charger les parties qui doivent être jouéer dans une partie du tournoi
    // ainsi si on est sur la premiere partie d'un tournoi 8 joueurs, on charge dans la liste 2 parties, donc les 4 premiers joueurs
    // si on était dans une partie 16 joueurs, on chargerait 4 parties, donc les 8 premiers joueurs
    // ensuite on appelle la fontion runPartieTournoi qui permet de jouer la partie actuelle
    private void setJoueursPartieTournoi() {
        // on charge les joueurs de la partie actuelle
        for(int i = 0; i < joueursSize; i++) {
            joueursPartie.add(joueursListe.get(i));
        }
        runPartieTournoi();
    }

    //méthode permettant de lancer une partie d'un tournoi
    private void runPartieTournoi() {
        joueur1Actuel = joueursPartie.get(0);
        joueur2Actuel = joueursPartie.get(1);

        // Récupérer le temps initial sélectionné dans le ComboBox
        String selectedTemps = tempsComboBox.getValue();

        tempsInitialBlancs = Integer.parseInt(selectedTemps) * 60;
        tempsInitialNoirs = Integer.parseInt(selectedTemps) * 60;

        // on modifie les labels pour afficher les noms des joueurs
        joueur1.setText(joueur1Actuel.getNomJoueur());
        joueur2.setText(joueur2Actuel.getNomJoueur());

        joueursPartie.removeFirst();
        joueursPartie.removeFirst();
        tourBlanc = true ;
        runJeuTournoi();
    }

    //Méthode permettant de générer le plateau correctement lors de changement de partie automatique d'un tournoi
    private void runJeuTournoi() {
        tourBlanc = true ;
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();
        reinitialiserTimer(); // Démarrer le timer lorsque le jeu commence
    }

    //Méthode récupérant les gagnants et les perdants à la fin d'une partie d'un tournoi (pouvant mettre également fin au tournoi quand il ne reste qu'un seul joueur)
    private void finPartie() {
        // on filtre dans la liste des joueurs ceux qui ont gagné, donc ceux qui sont aussi dans nextPartieJoueurs
        // on ajoute les joueurs gagnants dans la liste des joueurs pour la prochaine partie
        if(joueursPartie.size() <= 1) {
            joueursPartie = nextPartieJoueurs;
            if(nextPartieJoueurs.size() <= 1) {
                // si il ne reste qu'un joueur, on affiche un message de fin de tournoi
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fin du tournoi");
                alert.setHeaderText(null);
                alert.setContentText("Le tournoi est terminé ! " + nextPartieJoueurs.getFirst().getNomJoueur() + " a gagné le tournoi !");
                alert.show();
                return;
            }
            runPartieTournoi();
        } else {
            tourBlanc = true ;
            runPartieTournoi();
        }
    }




    //Partie du code contenant des méthodes de gestions du mode de rediffusion
    //Le mode étant en phase de BETA test, n'est efficient que pour des parties simples même si les conditions d'enregistrement pour un tournoi sont inclues (elles ne tournent pas pour l'instant)




    //
    @FXML
    private void selectPartie() {
        selectPartie.setStyle("-fx-background-color: black");
        selectReplay.setStyle("");
        selectStats.setStyle("");

        selectedPartie.setVisible(true);
        selectedReplay.setVisible(false);
        selectedStats.setVisible(false);

        selectedPartie.setManaged(true);
        selectedReplay.setManaged(false);
        selectedStats.setManaged(false);
    }

    @FXML
    private void selectReplay() {
        selectReplay.setStyle("-fx-background-color: black");
        selectPartie.setStyle("");
        selectStats.setStyle("");

        selectedReplay.setVisible(true);
        selectedPartie.setVisible(false);
        selectedStats.setVisible(false);

        selectedReplay.setManaged(true);
        selectedPartie.setManaged(false);
        selectedStats.setManaged(false);

        BoutonRediffClicked();
    }
    //Méthode permettant de créer un fichier texte où seront stocké les coups joués
    private void createLogFile() {
        // Chemin et nom du fichier, ici le fichier est nommé avec la date et l'heure actuelles
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        fileName = joueur1Actuel.getNomJoueur()+" contre "+joueur2Actuel.getNomJoueur() +" "+ LocalDateTime.now().format(formatter) ;
        // Combine le chemin et le nom du fichier
        File directory = new File(DIRECTORY_PATH);
        if(!directory.exists()) directory.mkdir();
        logFile = new File(directory, fileName);

    }

    //Méthode gérant l'écriture du fichier texte de la partie en cours
    private void ajoutDataToFile(String fileName) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de l'écriture des données.");
            e.printStackTrace();
        }
    }

    //Méthode permettant la lecture de coordonnées depuis un fichier texte
    private List<Coordinate> readCoordinatesFromFile() {
        List<Coordinate> coordinates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches("\\d+,\\d+")) { // Vérifie si la ligne correspond au format x,y
                    String[] parts = line.split(",");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    coordinates.add(new Coordinate(x, y));
                }
            }
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la lecture des données.");
            e.printStackTrace();
        }
        return coordinates;
    }

    //méthode
    private void BoutonRediffClicked() {
    }

    @FXML
    private ListView<String> listView;
    private ObservableList<String> fileList;
    private Path watchDir;

    private void initializeWatchService() {
        // add already existing files to the list
        File directory = new File(DIRECTORY_PATH);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                }
            }
        }

        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            watchDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // This will print the newly created file name
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path newPath = watchDir.resolve((Path) event.context());
                        Platform.runLater(() -> {
                            fileList.add(newPath.getFileName().toString());
                        });
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Méthode permettant la lecture d'un fichier texte
    private void playMovesFromFile(String file) throws IOException {
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        AtomicInteger cpt = new AtomicInteger();
        AtomicInteger fromX = new AtomicInteger();
        AtomicInteger fromY = new AtomicInteger();
        AtomicInteger toY = new AtomicInteger();
        AtomicInteger toX = new AtomicInteger();

        int i = 0;
        while ((line = reader.readLine()) != null) {
            ++i;
            PauseTransition pause = new PauseTransition(Duration.seconds(i));

            String finalLine = line;
            pause.setOnFinished(event -> {
                String[] parts = finalLine.split(",");
                if (cpt.get() == 0) {
                    fromX.set(Integer.parseInt(parts[0]));
                    fromY.set(Integer.parseInt(parts[1]));
                    cpt.set(1);
                } else {
                    toX.set(Integer.parseInt(parts[0]));
                    toY.set(Integer.parseInt(parts[1]));
                    cpt.set(0);
                }
                selectionnerPieceForRediff(fromX.get(), fromY.get());
                if(selectedPiece != null) deplacerPiecePourRediff(toX.get(), toY.get());
            });
            pause.play();
        }
        reader.close();
    }

    //méthode permettant de sélectionner une pièce sur le plateau par le fichier texte en mode rediffusion
    public void selectionnerPieceForRediff(int ligne, int col) {
        for (Piece pion : pions) {
            if (pion.getX() == ligne && pion.getY() == col) {
                selectedPiece = pion;
                break;
            }
        }
    }

    //méthode permettant de déplacer une pièce sélectionnées en mode rediffusion
    public void deplacerPiecePourRediff(int nouvelleLigne, int nouvelleCol) {
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
            alert.show();

            if(tournoi) {
                nextPartieJoueurs.add(tourBlanc ? joueur1Actuel : joueur2Actuel);
                finPartie();
            }
        }
        else if (estPat(tourBlanc ? "BLACK" : "WHITE")) {
            timeline.stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pat");
            alert.setHeaderText(null);
            alert.setContentText("Pat ! La partie est nulle.");
            alert.show();

            // on retente la partie (ajout des 2 joueurs au tout debut et on retente)
            if(tournoi) {
                joueursPartie.addLast(joueur2Actuel);
                joueursPartie.addFirst(joueur1Actuel);
                runPartieTournoi();
            }
        }

        selectedPiece = null;
        //change le tour
        tourBlanc = !tourBlanc;
    }





    private ArrayList<Joueur> loadPlayers() {
        ArrayList<Joueur> players = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("players.csv"))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String name = data[0];
                int nbParties = Integer.parseInt(data[1]);
                int nbPartiesGagne = Integer.parseInt(data[2]);
                Joueur player = new Joueur(name, nbParties, nbPartiesGagne);
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return players;
    }

    private void savePlayers(ArrayList<Joueur> players) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("players.csv"))) {
            for (Joueur player : players) {
                writer.println(player.getNomJoueur() + "," + player.getNbParties() + "," + player.getNbPartiesGagne());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectStats() {
        selectStats.setStyle("-fx-background-color: #262522");
        selectPartie.setStyle("");
        selectReplay.setStyle("");

        selectedStats.setVisible(true);
        selectedPartie.setVisible(false);
        selectedReplay.setVisible(false);

        selectedStats.setManaged(true);
        selectedPartie.setManaged(false);
        selectedReplay.setManaged(false);

        // load all stats from all Joueurs with loadPlayers function and show it
        ArrayList<Joueur> players = loadPlayers();
        selectedStats.getChildren().clear();
        for (Joueur player : players) {
            Label statLabel = new Label(player.getNomJoueur() + " : " + player.getNbParties() + " parties, " + player.getNbPartiesGagne() + " parties gagnées");
            statLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white");
            selectedStats.getChildren().add(statLabel);
        }
    }

}
