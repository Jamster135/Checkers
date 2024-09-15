package Ui;

import Core.CheckersLogic;
import Core.CheckersComputerPlayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CheckersGUI extends Application {

    private static final int TILE_SIZE = 60;  // Tile size
    private static final int BOARD_SIZE = 8;
    private GridPane grid = new GridPane();
    private Circle[][] pieces = new Circle[BOARD_SIZE][BOARD_SIZE];  // to track piece positions
    private CheckersLogic logic = new CheckersLogic();
    private char[][] board;
    private CheckersComputerPlayer computer;
    private boolean computerOn;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create checkerboard
        createBoard();

        // Initialize pieces
        initializePieces();

        // Text box for move input
        TextField moveInput = new TextField();

        // Button to submit move
        Button moveButton = new Button("Make Move");
        moveButton.setOnAction(e -> handleMove(moveInput.getText()));

        // Layout the input box and button in an HBox with padding
        HBox bottomPane = new HBox(10);
        bottomPane.getChildren().addAll(moveInput, moveButton);
        bottomPane.setStyle("-fx-padding: 10;");  // Adds padding around the text box and button

        // Set the layout in BorderPane
        root.setCenter(grid);
        root.setBottom(bottomPane);

        // Resize the scene to fit the board and input controls
        Scene scene = new Scene(root, TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE + 50);  // Added space for the input area
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checkers");
        primaryStage.show();


    }

    private void handleMove(String move) {
        if (logic.processMove(move)) {
            this.board = logic.getBoard();
            updateCircleArray();
            checkGameOver();

        } else {
            System.out.println("Invalid move!");
        }

    }

    private void updateCircleArray() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                char boardPiece = this.board[i][j]; // Current board piece ('o', 'x', or '_')

                if (boardPiece == 'o') {
                    if (pieces[i][j] == null || pieces[i][j].getFill() != Color.RED) {
                        removePiece(i, j);
                        addPiece(i, j, Color.RED);
                    }
                } else if (boardPiece == 'x') {
                    if (pieces[i][j] == null || pieces[i][j].getFill() != Color.BLACK) {
                        removePiece(i, j);
                        addPiece(i, j, Color.BLACK);
                    }
                } else {
                    removePiece(i, j);
                }
            }
        }
    }

    private void removePiece(int row, int col) {
        StackPane stack = (StackPane) getNodeFromGridPane(grid, col + 1, row + 1);
        if (stack != null && pieces[row][col] != null) {
            stack.getChildren().remove(pieces[row][col]);
            pieces[row][col] = null;
        }
    }

    private void createBoard() {
        grid.getChildren().clear();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);

                if ((row + col) % 2 == 0) {
                    tile.setFill(Color.BLACK);
                } else {
                    tile.setFill(Color.WHITE);
                }

                StackPane stack = new StackPane(tile);
                grid.add(stack, col + 1, row + 1);
            }
        }
    }

    private void initializePieces() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i < 3 && (i + j) % 2 == 1) {
                    addPiece(i, j, Color.RED);
                } else if (i > 4 && (i + j) % 2 == 1) {
                    addPiece(i, j, Color.BLACK);
                }
            }
        }
    }

    private void addPiece(int row, int col, Color color) {
        Circle piece = new Circle(TILE_SIZE / 2 - 5);
        piece.setFill(color);

        StackPane stack = (StackPane) getNodeFromGridPane(grid, col + 1, row + 1);
        if (stack != null) {
            stack.getChildren().add(piece);
        }

        pieces[row][col] = piece;
    }

    private StackPane getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (StackPane) node;
            }
        }
        return null;
    }

    public void setComputer(CheckersComputerPlayer computer, boolean computerOn) {
        this.computer = computer;
        this.computerOn = computerOn;
    }

    private void checkGameOver() {
        if (logic.checkWin()) {
            String winner = logic.isPlayerXTurn() ? "Player O" : "Player X";
            System.out.println(winner);
        }
    }
}
