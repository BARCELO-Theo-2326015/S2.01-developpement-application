package com.echecs.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        mainController controller = fxmlLoader.getController();
        stage.setOnShown(controller::setResizeEvents);

        stage.setTitle("Echecs");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}