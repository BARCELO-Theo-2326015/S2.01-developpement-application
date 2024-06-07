package com.echecs.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class mainController {
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

<<<<<<< Updated upstream
    private List<String> joueurs;
=======
    private List<Joueur> joueursListe = new ArrayList<>();
    private int joueursSize = 2;
>>>>>>> Stashed changes

    private List<Joueur> joueursPartie = new ArrayList<>();

    private Joueur joueur1Actuel;
    private Joueur joueur2Actuel;

    private List<Joueur> nextPartieJoueurs = new ArrayList<>();

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
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

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



    // Méthode pour terminer la partie
    private void terminerPartie(String message) {
        // Afficher un message indiquant la fin de la partie
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de la partie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        // Arrêter le timer
        // (facultatif : vous pouvez également ajouter d'autres logiques de nettoyage ou de réinitialisation ici)
    }

    @FXML
    private void jouerClicked() {
        if(joueursListe.size() < joueursSize) return;

        tourBlanc = true; // Les blancs commencent toujours
        // Récupérer le temps initial sélectionné dans le ComboBox
        String selectedTemps = tempsComboBox.getValue();
        tempsInitialBlancs = Integer.parseInt(selectedTemps) * 60;
        tempsInitialNoirs = Integer.parseInt(selectedTemps) * 60;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();
        reinitialiserTimer(); // Démarrer le timer lorsque le jeu commence
    }

    @FXML
    // jouerTournoi est appelé lorsque le bouton "Jouer Tournoi" est cliqué
    // cela permet de lancer un tournoi entre les joueurs sélectionnés
    // on va se servir de setJoueursPartieTournoi pour charger les parties qui doivent être jouées
    private void jouerTournoi() {
        if(joueursListe.size() < joueursSize) return;
        tournoi = true;
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
        runPartieTournoi(joueursPartie);
    }

    private void runPartieTournoi(List<Joueur> joueursPartie) {
        joueur1Actuel = joueursPartie.get(0);
        joueur2Actuel = joueursPartie.get(1);

        // Récupérer le temps initial sélectionné dans le ComboBox
        String selectedTemps = tempsComboBox.getValue();

        tempsInitialBlancs = Integer.parseInt(selectedTemps) * 60;
        tempsInitialNoirs = Integer.parseInt(selectedTemps) * 60;

        // on modifie les labels pour afficher les noms des joueurs
        joueur1.setText(joueur1Actuel.getNomJoueur());
        joueur2.setText(joueur2Actuel.getNomJoueur());

        joueursPartie.remove(0);
        joueursPartie.remove(1);

        runJeuTournoi();
    }

    private void runJeuTournoi() {
        reinitialiserPlateau();
        configurerPieces();
        mettreAJourPieces();
        updateGameSize();
        reinitialiserTimer(); // Démarrer le timer lorsque le jeu commence
    }

    private void finPartie() {
        // on filtre dans la liste des joueurs ceux qui ont gagné, donc ceux qui sont aussi dans nextPartieJoueurs
        // on ajoute les joueurs gagnants dans la liste des joueurs pour la prochaine partie
        runPartieTournoi(nextPartieJoueurs);
    }

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

    //méthode nécéssaire car celle de base beug de façon inexpliquée avec la méthode de vérification de tous les mouvements légaux
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

    private List<int[]> genererTousLesMouvementsLegaux(String equipe) {
        List<int[]> mouvementsLegaux = new ArrayList<>();
        List<Piece> copiePions = new ArrayList<>(pions); // Créer une copie de la liste des pièces pour éviter la modification concurrente
        for (Piece piece : copiePions) {
            if (piece.getEquipe().equals(equipe)) {
                // Pour chaque pièce, générer les mouvements possibles
                for (int[] mouvement : piece.genererMouvementsPossibles()) {
                    int nouvelleX = mouvement[0];
                    int nouvelleY = mouvement[1];
                    Piece autrePieceSelectionnee = getPieceAt(nouvelleY, nouvelleX);

                    if (piece.getType().equals("KNIGHT") || cheminLibre(piece, nouvelleX, nouvelleY)) {
                        if (autrePieceSelectionnee == null) {
                            String valide = deplacementPieceValide(piece, nouvelleX, nouvelleY, piece.getX(), piece.getY());
                            if (!valide.equals("false")) {
                                mouvementsLegaux.add(mouvement);
                            }

                            if (deplacementPossibleSansEchec(piece, nouvelleY, nouvelleX, piece.getY(), piece.getX())) {
                                mouvementsLegaux.add(new int[]{nouvelleX, nouvelleY});
                            }
                        }
                    }
                }
            }
        }

        return mouvementsLegaux;
    }


    // l'ajout d'une fonction de vérification de mouvement légaux pour chaque type de pièce a été nécéssaire dans un souci de débogage premièrement puis ont été laissé de manière permanente
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



    private Piece trouverRoi(String equipe) {
        for (Piece piece : pions) {
            if (piece.getType().equals("KING") && piece.getEquipe().equals(equipe)) {
                return piece;
            }
        }
        return null;
    }

    private boolean estPat(String equipe) {
        if (roiEnEchec(equipe)) {
            return false;
        }

        List<int[]> mouvementsLegaux = genererTousLesMouvementsLegaux(equipe);
        return mouvementsLegaux.isEmpty();
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
            timeline.stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Échec et mat");
            alert.setHeaderText(null);
            alert.setContentText("Echec et mat ! " + (tourBlanc ? "Les Blancs" : "Les Noirs") + " gagnent.");
            alert.showAndWait();
        }
        else if (estPat(tourBlanc ? "BLACK" : "WHITE")) {
            timeline.stop();
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
        tempsComboBox.setItems(FXCollections.observableArrayList("10", "3", "1"));
        tempsComboBox.getSelectionModel().selectFirst();
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

        launchPlayer("J1");
    }

    public void setPlayer(Joueur j) {
        if(j == null) return;
        joueursListe.add(j);
        selectStage.close();
        if(joueursListe.size() >= joueursSize) {
            //jouerClicked();
            // joueur 1 blanc
            joueur1.setText(joueursListe.get(0).getNomJoueur());
            // joueur 2 noir
            joueur2.setText(joueursListe.get(1).getNomJoueur());
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
        } catch (Exception e) {};
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

    @FXML
    private void selectPartie() {
        selectPartie.setStyle("-fx-background-color: black");
        selectReplay.setStyle("");

        selectedPartie.setVisible(true);
        selectedReplay.setVisible(false);
    }

    @FXML
    private void selectReplay() {
        selectReplay.setStyle("-fx-background-color: black");
        selectPartie.setStyle("");

        selectedReplay.setVisible(true);
        selectedPartie.setVisible(false);
    }
}

