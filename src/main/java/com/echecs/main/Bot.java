package com.echecs.main;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Bot {
    private static final int TAILLE = 8;
    private GridPane jeu;
    private String equipe;
    private boolean estHumain;
    private Random random = new Random();
    private List<Piece> pionsNoirs;
    private Piece pieceADeplacer;
    Piece piece;
    private BotController controller;

    public Bot(GridPane echiquier, String equipe, boolean estHumain, BotController controller) {
        this.jeu = echiquier;
        this.equipe = equipe;
        this.estHumain = estHumain;
        this.controller = controller;
    }

    public void jouer() {

        if(!estHumain) deplacerAleatoirement();
    }

    public void jouerEchec( List<int[]> moove) {

        if(!estHumain) deplacerAleatoirementEchec(moove);
    }

    public void deplacerAleatoirement() {
        pionsNoirs = controller.pions.stream()
                .filter(piece -> piece.getEquipe().equals("BLACK"))
                .collect(Collectors.toList());
        if(!pionsNoirs.isEmpty()) {
            int indexAleatoire = random.nextInt(pionsNoirs.size());
            pieceADeplacer = pionsNoirs.get(indexAleatoire);
            List<int[]> mouvementsValides = obtenirMouvementsValides(pieceADeplacer);

            int tests = 0;
            while(mouvementsValides.isEmpty() && tests < pionsNoirs.size()) {
                indexAleatoire = random.nextInt(pionsNoirs.size());
                pieceADeplacer = pionsNoirs.get(indexAleatoire);
                mouvementsValides = obtenirMouvementsValides(pieceADeplacer);
                tests++;
            }


            if (!mouvementsValides.isEmpty()) {
                int[] mouvement = mouvementsValides.get(random.nextInt(mouvementsValides.size()));


                controller.selectionnerPiece(pieceADeplacer.getX(), pieceADeplacer.getY());

                int botCol = mouvement[0];
                int botRow = mouvement[1];

                // Déplacer la pièce aléatoirement

                // Mettre à jour la liste des pièces dans la classe de contrôleur
                controller.deplacerPiece(botRow, botCol);

                pieceADeplacer = null;
            }
        }
    }

    public void deplacerAleatoirementEchec( List<int[]> moove) {
        pionsNoirs = controller.pions.stream()
                .filter(piece -> piece.getEquipe().equals("BLACK"))
                .collect(Collectors.toList());
        if(!pionsNoirs.isEmpty()) {
            int indexAleatoire = random.nextInt(pionsNoirs.size());
            pieceADeplacer = pionsNoirs.get(indexAleatoire);
            List<int[]> mouvementsValides = moove ;

            int tests = 0;
            while(mouvementsValides.isEmpty() && tests < pionsNoirs.size()) {
                indexAleatoire = random.nextInt(pionsNoirs.size());
                pieceADeplacer = pionsNoirs.get(indexAleatoire);
                mouvementsValides = obtenirMouvementsValides(pieceADeplacer);
                tests++;
            }


            if (!moove.isEmpty()) {
                int[] mouvement = mouvementsValides.get(random.nextInt(mouvementsValides.size()));


                controller.selectionnerPiece(pieceADeplacer.getX(), pieceADeplacer.getY());

                int botCol = mouvement[0];
                int botRow = mouvement[1];

                // Déplacer la pièce aléatoirement

                // Mettre à jour la liste des pièces dans la classe de contrôleur
                controller.deplacerPiece(botRow, botCol);

                pieceADeplacer = null;
            }
        }
    }

    private List<int[]> obtenirMouvementsValides(Piece piece) {
        List<int[]> mouvementsValides = new ArrayList<>();
        int botCol = pieceADeplacer.getY();
        int botRow = pieceADeplacer.getX();
        for (int col = 0; col < TAILLE; col++) {
            for (int row = 0; row < TAILLE; row++) {
                if(controller.deplacementPossibleSansEchec(pieceADeplacer,col,row,botCol,botRow)) {
                    String mouvementPossible = controller.deplacementPieceValide(pieceADeplacer, botCol, botRow, col, row);
                    if (!mouvementPossible.equals("false")) {
                        if ((mouvementPossible.equals("true")) ||
                                (mouvementPossible.equals("AVANCE") && controller.getPieceAt(col, row) == null) ||
                                (mouvementPossible.equals("CAPTURE") && controller.getPieceAt(col, row) != null)) {
                            if (controller.getPieceAt(col, row) != null) {
                                if (controller.getPieceAt(col, row).getEquipe() != pieceADeplacer.getEquipe())
                                    mouvementsValides.add(new int[]{col, row});
                            } else mouvementsValides.add(new int[]{col, row});
                        }
                    }
                }
            }
        }
        return mouvementsValides;
    }

}