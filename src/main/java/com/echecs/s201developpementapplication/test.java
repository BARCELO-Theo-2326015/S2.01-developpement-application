package com.echecs.s201developpementapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class test extends Application {

    private static final int SIZE = 8;
    private static final int TILE_SIZE = 80;

    private GridPane board = new GridPane();
    private Circle selectedPiece = null;

    @Override
    public void start(Stage primaryStage) {
        drawBoard();
        Scene scene = new Scene(board, SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        scene.setOnMouseClicked(event -> handleClick(event.getX(), event.getY()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jeu d'échecs");
        primaryStage.show();
    }

    private void drawBoard() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                Circle piece = null;
                if (y == 1) {
                    piece = createPiece(Color.BLACK); // Pions noirs
                } else if (y == 6) {
                    piece = createPiece(Color.WHITE); // Pions blancs
                } else if ((y == 0 || y == 7) && (x == 0 || x == 7)) {
                    piece = createPiece(y == 0 ? Color.BLACK : Color.WHITE); // Tours
                } else if ((y == 0 || y == 7) && (x == 1 || x == 6)) {
                    piece = createPiece(y == 0 ? Color.BLACK : Color.WHITE); // Cavaliers
                } else if ((y == 0 || y == 7) && (x == 2 || x == 5)) {
                    piece = createPiece(y == 0 ? Color.BLACK : Color.WHITE); // Fous
                } else if ((y == 0 || y == 7) && x == 3) {
                    piece = createPiece(y == 0 ? Color.BLACK : Color.WHITE); // Reine
                } else if ((y == 0 || y == 7) && x == 4) {
                    piece = createPiece(y == 0 ? Color.BLACK : Color.WHITE); // Roi
                }
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                if ((x + y) % 2 == 0) {
                    tile.setFill(Color.BEIGE);
                } else {
                    tile.setFill(Color.DARKGREEN);
                }
                board.add(tile, x, y);
                if (piece != null) {
                    board.add(piece, x, y);
                }
            }
        }
    }

    private Circle createPiece(Color color) {
        Circle piece = new Circle(TILE_SIZE / 3);
        piece.setFill(color);
        return piece;
    }

    private void handleClick(double x, double y) {
        int col = (int) (x / TILE_SIZE);
        int row = (int) (y / TILE_SIZE);

        if (selectedPiece == null) {
            selectPiece(col, row);
        } else {
            movePiece(selectedPiece, col, row);
        }
    }

    private void selectPiece(int col, int row) {
        if (col < 0 || col >= SIZE || row < 0 || row >= SIZE) {
            return;
        }
        for (int i = 0; i < board.getChildren().size(); i++) {
            if (board.getChildren().get(i) instanceof Circle) {
                Circle piece = (Circle) board.getChildren().get(i);
                if (GridPane.getColumnIndex(piece) == col && GridPane.getRowIndex(piece) == row) {
                    selectedPiece = piece;
                    return;
                }
            }
        }
    }

    private void movePiece(Circle piece, int col, int row) {
        if (col < 0 || col >= SIZE || row < 0 || row >= SIZE) {
            return;
        }
        board.getChildren().remove(piece);
        board.add(piece, col, row);
        selectedPiece = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
