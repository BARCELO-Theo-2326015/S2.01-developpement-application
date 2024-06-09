package com.echecs.main;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import java.util.List;

public class PromotionDialogController {

    @FXML
    private ChoiceBox<String> choiceBox;

    private String selectedPiece;

    /**
     * Méthode permettant d'initialiser les éléments du fxml nécéssaires au controller
     */
    @FXML
    public void initialize() {
        List<String> choices = List.of("QUEEN", "ROOK", "BISHOP", "KNIGHT");
        choiceBox.getItems().addAll(choices);
        choiceBox.setValue("QUEEN");
    }

    /**
     * méthode permettant de fermer la fenêtre quand le bouton "confirmer" est cliqué
     */
    @FXML
    private void onConfirm() {
        selectedPiece = choiceBox.getValue();
        Stage stage = (Stage) choiceBox.getScene().getWindow();
        stage.close();
    }

    /**
     * méthode permettant de fermet la fenêtre quand le bouton "annuler" est cliqué
     */
    @FXML
    private void onCancel() {
        selectedPiece = null;
        Stage stage = (Stage) choiceBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Méthode permettant de récupérer la pièce sélectionnée lors de la promotion
     * @return selectedPiece
     */
    public String getSelectedPiece() {
        return selectedPiece;
    }
}
