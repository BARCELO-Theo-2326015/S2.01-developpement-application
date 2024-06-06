package com.echecs.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class AccueilController {
    @FXML
    Button playPlayer;

    @FXML
    Button playComputer;

    Stage theStage;

    @FXML
    private void playPlayer() throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        mainController controller = fxmlLoader.getController();
        stage.setOnShown(controller::setResizeEvents);

        stage.setTitle("Echecs");
        stage.setScene(scene);
        stage.show();
        theStage.close();
    }

    @FXML
    private void playComputer() throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("bot.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        BotController controller = fxmlLoader.getController();
        stage.setOnShown(controller::setResizeEvents);

        stage.setTitle("Echecs");
        stage.setScene(scene);
        stage.show();
        theStage.close();
    }

    void setClose(WindowEvent windowEvent) {
        theStage = (Stage) playPlayer.getScene().getWindow();
    }
}
