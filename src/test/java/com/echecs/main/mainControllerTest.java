package com.echecs.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

public class mainControllerTest {

    @InjectMocks
    private mainController mainController;

    @Mock
    private Piece pieceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de type roi
    @Test
    public void testValiderDeplacementRoi() {
        //test
        String result = mainController.validerDeplacementRoi(5,7,4,7);
        assertEquals("true", result);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de type cavalier
    @Test
    public void testValiderDeplacementCavalier() {
        //test
        String result = mainController.validerDeplacementCavalier(0,2,1,0);
        assertEquals("true", result);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de type tour
    @Test
    public void testValiderDeplacementTour() {
        //test
        String result = mainController.validerDeplacementTour(0,6,0,0);
        assertEquals("true", result);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de type fou
    @Test
    public void testValiderDeplacementFou() {
        //test
        String result = mainController.validerDeplacementFou(3,1,2,0);
        assertEquals("true", result);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de type reine
    @Test
    public void testValiderDeplacementReine() {
        //test
        String result = mainController.validerDeplacementReine(6,0,4,0);
        assertEquals("true", result);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de type pion
    @Test
    public void testValiderDeplacementPion() {
        // Arrange
        Piece pion = new Piece("PAWN", "BLACK",4,0);
        //test
        String result = mainController.validerDeplacementPion(pion,0,3,0,4);
        assertEquals("AVANCE", result);
    }

    //test pour vérifier la méthode de vérification d'un déplacement d'une pièce de n'importe quel type
    @Test
    public void testValiderDeplacementPiece() {
        // Arrange
        Piece pion = new Piece("PAWN", "BLACK",4,0);
        //test
        String result = mainController.deplacementPieceValide(pion,0,3,0,4);
        assertEquals("AVANCE", result);
    }

    //test pour vérifier le formatage du temps
    @Test
    public void testValiderFormatageTemps() {
        int temps = 120 ;
        String result = mainController.formaterTemps(temps);
        assertEquals(String.format("%02d:%02d", 2, 0), result);
    }


}
