package com.echecs.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Classe qui gère l'échec et mat
 */
public class echecEtMatController {
    @FXML
    private Label messageLabel2;

    @FXML
    private Label messageLabel;

    /**
     * Méthode qui modifie le méssage a l'affichage de la fenêtre d'echec et mat
     *
     * @param message
     */
    public void setMessage(String message) {
        messageLabel2.setText(message);
    }

    /**
     * Méthode qui modifie le méssage a l'affichage de la fenêtre d'echec et mat
     *
     * @param message
     */
    public void setMessage2(String message) {
        messageLabel.setText(message);
    }

    /**
     * Méthode qui ferme la boite de dialogue
     */
    @FXML
    private void handleOk() {
        // Fermer la boîte de dialogue
        Stage stage = (Stage) messageLabel2.getScene().getWindow();
        stage.close();
    }
}
