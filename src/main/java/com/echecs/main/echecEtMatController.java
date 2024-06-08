package com.echecs.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class echecEtMatController {
    @FXML
    private Label messageLabel2;

    @FXML
    private Label messageLabel;


    public void setMessage(String message) {
        messageLabel2.setText(message);
    }
    public void setMessage2(String message) {
        messageLabel.setText(message);
    }

    @FXML
    private void handleOk() {
        // Fermer la boîte de dialogue
        Stage stage = (Stage) messageLabel2.getScene().getWindow();
        stage.close();
    }
}
