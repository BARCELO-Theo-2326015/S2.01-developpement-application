package com.echecs.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Page principal du jeu
 */
public class mainApplication extends Application {

    /**
     * Point d'entrée de l'application
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        mainController controller = fxmlLoader.getController();
        stage.setOnShown(controller::setResizeEvents);
        Image icon = new Image(getClass().getResourceAsStream("images/chesscom.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Echecs");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Méthode qui lance l'application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}