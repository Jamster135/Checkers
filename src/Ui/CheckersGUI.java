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

/**
 * This class launches a GUI of the checkers board and lets players submit
 * there move in a text box, and submit it with a button
 *
 * @author Jacob Barrios
 * @version 1.0, 9/16/24
 */
public class CheckersGUI extends Application {
	private static final int TILE_SIZE = 60;
	private static final int BOARD_SIZE = 8;
	private final GridPane grid = new GridPane();
	private final Circle[][] pieces = new Circle[BOARD_SIZE][BOARD_SIZE];
	private final CheckersLogic logic = new CheckersLogic();
	private char[][] board;
	private CheckersComputerPlayer computer;
	private static boolean isComputerOn = false;
	
	/**
	 * Starts the GUI for the Checkers board
	 *
	 * @param primaryStage The stage where the board will be displayed
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
	 * Handler method to check the input of the player after clicking the submit button
	 *
	 * @param move The move the player wants to make in String form
	 */
	private void handleMove(String move) {
		// Process the move from the text field
		if(logic.processMove(move)) {
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
	 * Updates the array of circles so it matches the updated board array
	 * Help to update the board on the GUI too
	 *
	 */
	private void updateCircleArray() {
		// Iterate through the circle array to see where all the pieces are
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				// Current board piece ('o', 'x', or '_')
				char boardPiece = this.board[i][j];
				
				// Compare the board array to the circle array to check if they both match and update it they don't
				if(boardPiece == 'o') {
					// Check if o piece matches with red circle in the circle array
					if(pieces[i][j] == null || pieces[i][j].getFill() != Color.RED) {
						removePiece(i, j);
						addPiece(i, j, Color.RED);
						
					}
					
				}
				else if(boardPiece == 'x') {
					// Check if x piece matches with black circle in the circle array
					if(pieces[i][j] == null || pieces[i][j].getFill() != Color.BLACK) {
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
	 * Creates either a black or red piece depending on where the iteration is at the start of the game
	 *
	 */
	private void initializePieces() {
		// Create the pieces on top of the tiles
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				if(i < 3 && (i + j) % 2 == 1) {
					addPiece(i, j, Color.RED);
					
				}
				else if(i > 4 && (i + j) % 2 == 1) {
					addPiece(i, j, Color.BLACK);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Creates the board in the GUI
	 *
	 */
	private void createBoard() {
		// Clear board
		grid.getChildren().clear();
		
		// Create tiles in a grid
		for(int row = 0; row < BOARD_SIZE; row++) {
			for(int col = 0; col < BOARD_SIZE; col++) {
				Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
				
				if((row + col) % 2 == 0) {
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
	 * Removes a piece that is taken
	 *
	 * @param row The row the piece is on
	 * @param col The column the piece is on
	 */
	private void removePiece(int row, int col) {
		// Get the stack in the grid pane
		StackPane stack = getNodeFromGridPane(grid, col + 1, row + 1);
		
		if(stack != null && pieces[row][col] != null) {
			// Make content in stack null
			stack.getChildren().remove(pieces[row][col]);
			// Make content in board array null
			pieces[row][col] = null;
			
		}
		
	}
	
	/**
	 * Adds a piece in the circle array that represents the board
	 *
	 * @param row   Row to add the piece
	 * @param col   Column to add the piece
	 * @param color Color of the piece added
	 */
	private void addPiece(int row, int col, Color color) {
		// Create the piece
		Circle piece = new Circle((double) (TILE_SIZE / 2) - 5);
		piece.setFill(color);
		
		// Create a stack pane to center the circle piece
		StackPane stack = getNodeFromGridPane(grid, col + 1, row + 1);
		
		if(stack != null) {
			stack.getChildren().add(piece);
			
		}
		
		pieces[row][col] = piece;
		
	}
	
	/**
	 * Gets the node/piece in the stack pane within the grid
	 * A piece is added to a stack pane to center it and then added to the grid
	 *
	 * @param gridPane The grid pane shown in the GUI
	 * @param col      Column the stack pane is in
	 * @param row      Row the Stack pane is in
	 * @return Returns the node/piece in the stack pane
	 */
	private StackPane getNodeFromGridPane(GridPane gridPane, int col, int row) {
		// Get stack from the grid pane
		
		for(javafx.scene.Node node : gridPane.getChildren()) {
			if(GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return (StackPane) node;
				
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * Checks if any player won the game
	 * Prints whoever the winner is
	 *
	 */
	private void checkGameOver() {
		// Use logic method to check for with on the board array
		if(logic.checkWin()) {
			String winner = logic.isPlayerXTurn() ? "Player O" : "Player X";
			System.out.println(winner);
			
		}
		
	}
	
	/**
	 * Updates the status of the computer based on the answer in the terminal before launching GUI
	 *
	 * @param computerOn Status of the computer
	 */
	public static void setComputerOn(boolean computerOn) {
		// Set the status of the computer
		isComputerOn = computerOn;
		
	}
	
}
