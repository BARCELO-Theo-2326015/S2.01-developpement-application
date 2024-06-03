package com.echecs.s201developpementapplication;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class test extends Application {

    private static final int SIZE = 8;
    private static final int TILE_SIZE = 80;
    private Circle selectedPiece = null;
    private Rectangle selectedRect = null;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        Rectangle[][] rectangles = new Rectangle[SIZE][SIZE];
        Circle[][] pieces = new Circle[SIZE][SIZE];

        // Définir les contraintes des colonnes et des lignes pour redimensionner la GridPane
        for (int i = 0; i < SIZE; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints(TILE_SIZE);
            colConstraints.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(colConstraints);

            RowConstraints rowConstraints = new RowConstraints(TILE_SIZE);
            rowConstraints.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rowConstraints);
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((row + col) % 2 == 0) {
                    rect.setFill(Color.WHITE);
                } else {
                    rect.setFill(Color.BLACK);
                }

                // Stocker les rectangles dans une matrice
                rectangles[row][col] = rect;

                // Ajouter un événement de clic de souris pour chaque case
                rect.setOnMouseClicked(event -> {
                    if (selectedPiece != null) {
                        int newRow = GridPane.getRowIndex(rect);
                        int newCol = GridPane.getColumnIndex(rect);
                        if (pieces[newRow][newCol] == null) {
                            // Déplacer la pièce
                            GridPane.setColumnIndex(selectedPiece, newCol);
                            GridPane.setRowIndex(selectedPiece, newRow);
                            pieces[newRow][newCol] = selectedPiece;
                            pieces[GridPane.getRowIndex(selectedPiece)][GridPane.getColumnIndex(selectedPiece)] = null;
                            selectedPiece = null;  // Désélectionner la pièce après déplacement
                            if (selectedRect != null) {
                                selectedRect.setStroke(null);
                            }
                            selectedRect = null;
                        }
                    }
                });

                grid.add(rect, col, row);

                // Ajouter des cercles pour simuler des pièces
                Circle circle = new Circle(TILE_SIZE / 2 - 10);
                circle.setStrokeWidth(3);
                if (row < 2) {
                    circle.setFill(Color.BLACK);
                    circle.setStroke(Color.WHITE);  // Contour blanc pour pièces noires
                    pieces[row][col] = circle;
                } else if (row > 5) {
                    circle.setFill(Color.WHITE);
                    circle.setStroke(Color.BLACK);  // Contour noir pour pièces blanches
                    pieces[row][col] = circle;
                } else {
                    circle.setFill(Color.TRANSPARENT);
                    pieces[row][col] = null;
                }

                // Ajouter un événement de clic de souris pour chaque pièce
                if (circle.getFill() != Color.TRANSPARENT) {
                    circle.setOnMouseClicked(event -> {
                        if (selectedRect != null) {
                            selectedRect.setStroke(null); // Enlever le surlignage précédent
                        }
                        selectedPiece = circle;
                        selectedRect = rect;
                        selectedRect.setStroke(Color.YELLOW); // Surligner la case en jaune
                        selectedRect.setStrokeWidth(3);
                        event.consume();  // Empêcher la propagation de l'événement à la case
                    });
                }

                GridPane.setRowIndex(circle, row);
                GridPane.setColumnIndex(circle, col);
                grid.getChildren().add(circle);
                grid.setPrefHeight(600);
            }
        }

        Scene scene = new Scene(grid);
        primaryStage.setTitle("Chess Board with Movable Pieces");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
