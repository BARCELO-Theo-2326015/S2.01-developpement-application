package com.echecs.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Accueil extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("accueil.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        AccueilController controller = fxmlLoader.getController();
        primaryStage.setOnShown(controller::setClose);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Jeu d'échecs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}