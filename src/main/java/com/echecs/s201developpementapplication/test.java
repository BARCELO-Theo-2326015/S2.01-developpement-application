package com.echecs.s201developpementapplication;

import javafx.application.Application;
import javafx.scene.Node;
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

    private enum PieceType {
        PAWN_BLACK(Color.BLACK),
        PAWN_WHITE(Color.WHITE),
        ROOK_BLACK(Color.BLACK),
        ROOK_WHITE(Color.WHITE),
        KNIGHT_BLACK(Color.BLACK),
        KNIGHT_WHITE(Color.WHITE),
        BISHOP_BLACK(Color.BLACK),
        BISHOP_WHITE(Color.WHITE),
        QUEEN_BLACK(Color.BLACK),
        QUEEN_WHITE(Color.WHITE),
        KING_BLACK(Color.BLACK),
        KING_WHITE(Color.WHITE);

        private final Color color;

        PieceType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        initializeBoard();
        Scene scene = new Scene(board, SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        scene.setOnMouseClicked(event -> handleClick(event.getX(), event.getY()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jeu d'échecs");
        primaryStage.show();
    }

    private void initializeBoard() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                Rectangle tile = createTile(x, y);
                Circle piece = createPiece(x, y);
                placePiece(tile, piece, x, y);
            }
        }
    }

    private Rectangle createTile(int x, int y) {
        Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
        tile.setFill((x + y) % 2 == 0 ? Color.BEIGE : Color.DARKGREEN);
        return tile;
    }

    private Circle createPiece(int x, int y) {
        PieceType pieceType = getPieceType(x, y);
        if (pieceType == null) return null;
        Circle piece = new Circle(TILE_SIZE / 3);
        piece.setFill(pieceType.getColor());
        piece.setUserData(pieceType); // Store the piece type in the Circle's user data
        return piece;
    }

    private PieceType getPieceType(int x, int y) {
        if (y == 1) return PieceType.PAWN_BLACK;
        if (y == 6) return PieceType.PAWN_WHITE;
        if ((y == 0 || y == 7) && (x == 0 || x == 7)) return y == 0 ? PieceType.ROOK_BLACK : PieceType.ROOK_WHITE;
        if ((y == 0 || y == 7) && (x == 1 || x == 6)) return y == 0 ? PieceType.KNIGHT_BLACK : PieceType.KNIGHT_WHITE;
        if ((y == 0 || y == 7) && (x == 2 || x == 5)) return y == 0 ? PieceType.BISHOP_BLACK : PieceType.BISHOP_WHITE;
        if ((y == 0 || y == 7) && x == 3) return y == 0 ? PieceType.QUEEN_BLACK : PieceType.QUEEN_WHITE;
        if ((y == 0 || y == 7) && x == 4) return y == 0 ? PieceType.KING_BLACK : PieceType.KING_WHITE;
        return null;
    }

    private void placePiece(Rectangle tile, Circle piece, int x, int y) {
        board.add(tile, x, y);
        if (piece != null) {
            GridPane.setHalignment(piece, javafx.geometry.HPos.CENTER);
            GridPane.setValignment(piece, javafx.geometry.VPos.CENTER);
            board.add(piece, x, y);
        }
    }

    private void handleClick(double x, double y) {
        int col = (int) (x / TILE_SIZE);
        int row = (int) (y / TILE_SIZE);

        if (selectedPiece == null) {
            selectPiece(col, row);
        } else {
            if (movePieceIsValid(selectedPiece, col, row)) {
                movePiece(selectedPiece, col, row);
            } else {
                System.out.println("Mouvement invalide !");
                selectedPiece = null; // Reset selectedPiece on invalid move
            }
        }
    }

    private void selectPiece(int col, int row) {
        if (col < 0 || col >= SIZE || row < 0 || row >= SIZE) {
            return;
        }
        for (Node node : board.getChildren()) {
            if (node instanceof Circle) {
                Circle piece = (Circle) node;
                if (GridPane.getColumnIndex(piece) == col && GridPane.getRowIndex(piece) == row) {
                    selectedPiece = piece;
                    return;
                }
            }
        }
    }

    private boolean movePieceIsValid(Circle piece, int col, int row) {
        int currentCol = GridPane.getColumnIndex(piece);
        int currentRow = GridPane.getRowIndex(piece);

        if (col < 0 || col >= SIZE || row < 0 || row >= SIZE) {
            return false;
        }

        for (Node node : board.getChildren()) {
            if (node instanceof Circle) {
                Circle otherPiece = (Circle) node;
                int otherPieceCol = GridPane.getColumnIndex(otherPiece);
                int otherPieceRow = GridPane.getRowIndex(otherPiece);
                if (otherPieceCol == col && otherPieceRow == row && pieceFillEquals(piece, otherPiece)) {
                    return false;
                }
            }
        }

        PieceType pieceType = (PieceType) piece.getUserData();

        // Déplacement des pions
        if (pieceType == PieceType.PAWN_BLACK) {
            if (col == currentCol && row == currentRow + 1) {
                return true; // Avance d'une case
            } else if (currentRow == 1 && col == currentCol && row == currentRow + 2) {
                return true; // Premier mouvement spécial de deux cases
            } else if (Math.abs(col - currentCol) == 1 && row == currentRow + 1) {
                return true; // Capture en diagonale vers l'avant
            }
        } else if (pieceType == PieceType.PAWN_WHITE) {
            if (col == currentCol && row == currentRow - 1) {
                return true; // Avance d'une case
            } else if (currentRow == 6 && col == currentCol && row == currentRow - 2) {
                return true; // Premier mouvement spécial de deux cases
            } else if (Math.abs(col - currentCol) == 1 && row == currentRow - 1) {
                return true; // Capture en diagonale vers l'avant
            }
        }

        // Déplacement des tours
        if (pieceType == PieceType.ROOK_BLACK || pieceType == PieceType.ROOK_WHITE) {
            if (col == currentCol || row == currentRow) {
                return true; // Déplacement horizontal ou vertical
            }
        }

        // Déplacement des cavaliers
        if (pieceType == PieceType.KNIGHT_BLACK || pieceType == PieceType.KNIGHT_WHITE) {
            if ((Math.abs(col - currentCol) == 2 && Math.abs(row - currentRow) == 1) ||
                    (Math.abs(col - currentCol) == 1 && Math.abs(row - currentRow) == 2)) {
                return true; // Déplacement en forme de L
            }
        }

        // Déplacement des fous
        if (pieceType == PieceType.BISHOP_BLACK || pieceType == PieceType.BISHOP_WHITE) {
            if (Math.abs(col - currentCol) == Math.abs(row - currentRow)) {
                return true; // Déplacement en diagonale
            }
        }

        // Déplacement des reines
        if (pieceType == PieceType.QUEEN_BLACK || pieceType == PieceType.QUEEN_WHITE) {
            if (col == currentCol || row == currentRow || Math.abs(col - currentCol) == Math.abs(row - currentRow)) {
                return true; // Déplacement horizontal, vertical ou en diagonale
            }
        }

        // Déplacement des rois
        if (pieceType == PieceType.KING_BLACK || pieceType == PieceType.KING_WHITE) {
            if (Math.abs(col - currentCol) <= 1 && Math.abs(row - currentRow) <= 1) {
                return true; // Déplacement d'une case dans n'importe quelle direction
            }
        }

        return false;
    }

    private boolean pieceFillEquals(Circle piece1, Circle piece2) {
        return piece1.getFill().equals(piece2.getFill());
    }

    private void movePiece(Circle piece, int col, int row) {
        board.getChildren().remove(piece);
        board.add(piece, col, row);
        selectedPiece = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
