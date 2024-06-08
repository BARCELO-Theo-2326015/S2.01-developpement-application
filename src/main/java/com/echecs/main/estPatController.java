package com.echecs.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;



public class estPatController {

    @FXML
    private Label messageLabel;



    public void setMessagePat(String message) {
        messageLabel.setText(message);
    }

    @FXML
    private void handleOk() {
        // Fermer la boîte de dialogue
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}