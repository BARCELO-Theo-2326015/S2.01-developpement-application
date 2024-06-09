package com.echecs.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Classe qui gère les pat
 */
public class estPatController {

    @FXML
    private Label messageLabel;

    /**
     * Méthode qui modifie le méssage a l'affichage de la fenêtre de pat
     *
     * @param message
     */
    public void setMessagePat(String message) {
        messageLabel.setText(message);
    }

    /**
     * Méthode qui ferme la fenêtre
     */
    @FXML
    private void handleOk() {
        // Fermer la boîte de dialogue
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}