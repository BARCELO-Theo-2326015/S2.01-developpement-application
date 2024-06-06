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
    private mainController controller;

    public Bot(GridPane echiquier, String equipe, boolean estHumain, mainController controller) {
        this.jeu = echiquier;
        this.equipe = equipe;
        this.estHumain = estHumain;
        this.controller = controller;
    }

    public void jouer() {

        if(!estHumain) deplacerAleatoirement();
    }

    public void deplacerAleatoirement() {
        controller.configurerPieces();
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

            System.out.println("MouvementValide = " + mouvementsValides);

            if (!mouvementsValides.isEmpty()) {
                int[] mouvement = mouvementsValides.get(random.nextInt(mouvementsValides.size()));

                System.out.println("Ancien | x : " + pieceADeplacer.getX() + " | y : " + pieceADeplacer.getY());
                System.out.println("Nouveau | x : " + mouvement[1] + " | y : " + mouvement[0]);

                controller.selectionnerPiece(pieceADeplacer.getX(), pieceADeplacer.getY());

                int botCol = mouvement[0];
                int botRow = mouvement[1];

                // Déplacer la pièce aléatoirement
                System.out.println("deplacerAleatoirement");

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
                String mouvementPossible = controller.deplacementPieceValide(pieceADeplacer, botCol, botRow, col, row);
                if (!mouvementPossible.equals("false")) {
                    if((mouvementPossible.equals("true")) ||
                        (mouvementPossible.equals("AVANCE") && controller.getPieceAt(col, row) == null) ||
                        (mouvementPossible.equals("CAPTURE") && controller.getPieceAt(col, row) != null)) {
                        if(controller.getPieceAt(col, row) != null) {
                            if(controller.getPieceAt(col, row).getEquipe() != pieceADeplacer.getEquipe()) mouvementsValides.add(new int[]{col, row});
                        } else mouvementsValides.add(new int[]{col, row});
                    }
                }
            }
        }
        System.out.println("mouvementValide = "+mouvementsValides);
        return mouvementsValides;
    }

}