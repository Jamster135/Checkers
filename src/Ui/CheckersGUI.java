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
    private static final int TILE_SIZE = 60;
    private static final int BOARD_SIZE = 8;
    private GridPane grid = new GridPane();
    private Circle[][] pieces = new Circle[BOARD_SIZE][BOARD_SIZE];
    private CheckersLogic logic = new CheckersLogic();
    private char[][] board;
    private CheckersComputerPlayer computer;
    private static boolean isComputerOn = false;

    /**
     * @name start
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        this.computer = new CheckersComputerPlayer(logic);

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

    /**
     * @name handleMove
     * @param move
     */
    private void handleMove(String move) {
        // Process the move from the text field
        if (logic.processMove(move)) {
            this.board = logic.getBoard();
            updateCircleArray();
            checkGameOver();

            // Makes a computer move if computer is on
            if(isComputerOn && !logic.isPlayerXTurn()) {
                String computerMove = computer.moveForComputer();
                if(computerMove.equals("No more moves")) {
                    System.out.println("Player X");

                }

                // Process computer move
                if(logic.processMove(computerMove)) {
                    updateCircleArray();
                    checkGameOver();

                }

            }

        }
        else {
            System.out.println("Invalid move!");

        }

    }

    /**
     * @name updateCircleArray
     */
    private void updateCircleArray() {
        // Iterate through the circle array to see where all the pieces are
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Current board piece ('o', 'x', or '_')
                char boardPiece = this.board[i][j];

                // Compare the board array to the circle array to check if they both match and update it they don't
                if (boardPiece == 'o') {
                    // Check if o piece matches with red circle in the circle array
                    if (pieces[i][j] == null || pieces[i][j].getFill() != Color.RED) {
                        removePiece(i, j);
                        addPiece(i, j, Color.RED);

                    }

                }
                else if (boardPiece == 'x') {
                    // Check if x piece matches with black circle in the circle array
                    if (pieces[i][j] == null || pieces[i][j].getFill() != Color.BLACK) {
                        removePiece(i, j);
                        addPiece(i, j, Color.BLACK);

                    }

                }
                // if the board piece is null, make sure it's null on the circle array
                else {
                    removePiece(i, j);

                }

            }

        }

    }

    /**
     * @name removePiece
     * @param row
     * @param col
     */
    private void removePiece(int row, int col) {
        // Get the stack in the grid pane
        StackPane stack = getNodeFromGridPane(grid, col + 1, row + 1);

        if (stack != null && pieces[row][col] != null) {
            // Make content in stack null
            stack.getChildren().remove(pieces[row][col]);
            // Make content in board array null
            pieces[row][col] = null;

        }

    }

    /**
     * @name CreateBoard
     */
    private void createBoard() {
        // Clear board
        grid.getChildren().clear();

        // Create tiles in a grid
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);

                if ((row + col) % 2 == 0) {
                    tile.setFill(Color.BLACK);

                }
                else {
                    tile.setFill(Color.WHITE);

                }

                // Add stack to a part of the grid
                StackPane stack = new StackPane(tile);
                grid.add(stack, col + 1, row + 1);

            }

        }

    }

    /**
     * @name initializePieces
     */
    private void initializePieces() {
        // Create the pieces on top of the tiles
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i < 3 && (i + j) % 2 == 1) {
                    addPiece(i, j, Color.RED);

                }
                else if (i > 4 && (i + j) % 2 == 1) {
                    addPiece(i, j, Color.BLACK);

                }

            }

        }

    }

    /**
     * @name addPiece
     * @param row
     * @param col
     * @param color
     */
    private void addPiece(int row, int col, Color color) {
        // Create the piece
        Circle piece = new Circle(TILE_SIZE / 2 - 5);
        piece.setFill(color);

        // Create a stack pane to center the circle piece
        StackPane stack = getNodeFromGridPane(grid, col + 1, row + 1);

        if (stack != null) {
            stack.getChildren().add(piece);

        }

        pieces[row][col] = piece;

    }

    /**
     * @name getNodeFromGridPane
     * @param gridPane
     * @param col
     * @param row
     * @return
     */
    private StackPane getNodeFromGridPane(GridPane gridPane, int col, int row) {
        // Get stack from the grid pane

        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (StackPane) node;

            }

        }

        return null;

    }

    /**
     * @name checkGameOver
     */
    private void checkGameOver() {
        // Use logic method to check for with on the board array
        if (logic.checkWin()) {
            String winner = logic.isPlayerXTurn() ? "Player O" : "Player X";
            System.out.println(winner);

        }

    }

    /**
     * @name setComputerOn
     * @param computerOn
     */
    public static void setComputerOn(boolean computerOn) {
        // Set the status of the computer
        isComputerOn = computerOn;

    }

}
