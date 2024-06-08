package com.echecs.main;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.List;

public class PromotionDialogController {

    @FXML
    private ChoiceBox<String> choiceBox;

    private String selectedPiece;

    @FXML
    public void initialize() {
        List<String> choices = List.of("QUEEN", "ROOK", "BISHOP", "KNIGHT");
        choiceBox.getItems().addAll(choices);
        choiceBox.setValue("QUEEN");
    }

    @FXML
    private void onConfirm() {
        selectedPiece = choiceBox.getValue();
        Stage stage = (Stage) choiceBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        selectedPiece = null;
        Stage stage = (Stage) choiceBox.getScene().getWindow();
        stage.close();
    }

    public String getSelectedPiece() {
        return selectedPiece;
    }
}
