package Core;

/**
 * This class handles all the logic of the game
 *
 * @author Jacob Barrios
 * @version 4.0, 9/30/2024
 */
public class CheckersLogic {
	private char[][] board;
	private boolean playerXTurn;
	private boolean takingMove = true;
	
	/**
	 * Constructor for the logic class
	 */
	public CheckersLogic() {
		this.board = new char[8][8];
		newGameBoard();
		this.playerXTurn = true;
		
	}
	
	/**
	 * Creates a new board array and all the pieces are represented by a 'x' or 'o'
	 */
	public void newGameBoard() {
		this.board = new char[8][8];
		
		//Repeat throughout the board
		for(int i = 0; i < this.board.length; i++) {
			for(int j = 0; j < this.board.length; j++) {
				//Setup player o's side
				if(i < 3 && (i + j) % 2 == 1) {
					board[i][j] = 'o';
					
				}
				//Setup player x's side
				else if(i > 4 && (i + j) % 2 == 1) {
					board[i][j] = 'x';
					
				}
				else {
					this.board[i][j] = '_';
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Gets the board array
	 *
	 * @return 2d array of the board
	 */
	public char[][] getBoard() {
		return board;
		
	}
	
	/**
	 * Processes the move by passing it into a method to validate the move
	 * and another method that updates the board
	 *
	 * @param move from player
	 * @return if move is valid
	 */
	public boolean processMove(String move) {
		// converts move string to index of the array
		int yStart = 8 - Character.getNumericValue((move.charAt(0)));
		int xStart = move.charAt(1) - 'a';
		int yEnd = 8 - Character.getNumericValue((move.charAt(3)));
		int xEnd = move.charAt(4) - 'a';
		
		if(validMove(xStart, yStart, xEnd, yEnd)) {
			makeMove(xStart, yStart, xEnd, yEnd);
			
			return true;
			
		}
		
		return false;
		
	}
	
	/**
	 * Checks if the move is valid by checking
	 * If there is a piece at the end of the move
	 * If a piece exists where is starts
	 *
	 * @param xStart x coordinate of piece
	 * @param yStart y coordinate of piece
	 * @param xEnd   x coordinate of new move
	 * @param yEnd   y coordinate of new move
	 * @return if move is valid
	 */
	private boolean validMove(int xStart, int yStart, int xEnd, int yEnd) {
		int moveDirection;
		char opponentPiece;
		char piece;
		
		// Check if the move is within bounds and the target cell is empty.
		if(xStart < 0 || xStart >= 8 || yStart < 0 || yStart >= 8 || xEnd < 0 || xEnd >= 8 || yEnd < 0 || yEnd >= 8) {
			return false;
			
		}
		
		//
		piece = board[yStart][xStart];
		if(board[yEnd][xEnd] != '_' || piece == '_') {
			return false;
		}
		if(playerXTurn && piece != 'x' || !playerXTurn && piece != 'o') {
			return false;
		}
		
		// Check if the move is forward and diagonal.
		if(playerXTurn) {
			moveDirection = -1;
			
		}
		else {
			moveDirection = 1;
		}
		
		
		if(Math.abs(yStart - yEnd) == 1 && Math.abs(xStart - xEnd) == 1) {
			if((yEnd - yStart) == moveDirection) {
				return true;
			}
			
		}
		// Check for capturing move.
		if(Math.abs(xStart - xEnd) == 2 && Math.abs(yStart - yEnd) == 2) {
			int xMiddle = (xStart + xEnd) / 2;
			int yMiddle = (yStart + yEnd) / 2;
			
			if(playerXTurn) {
				opponentPiece = 'o';
			}
			else {
				opponentPiece = 'x';
			}
			
			if(board[yMiddle][xMiddle] == opponentPiece) {
				if(this.takingMove) {
					board[yMiddle][xMiddle] = '_'; // Capture the piece
					
				}
				
				return true;
				
			}
		}
		
		return false;
		
	}
	
	/**
	 * Updates the board with the new move
	 *
	 * @param xStart x coordinate of piece
	 * @param yStart y coordinate of piece
	 * @param xEnd   x coordinate of new move
	 * @param yEnd   y coordinate of new move
	 */
	private void makeMove(int xStart, int yStart, int xEnd, int yEnd) {
		board[yEnd][xEnd] = board[yStart][xStart];
		
		board[yStart][xStart] = '_';
		
		changeTurn();
		
	}
	
	/**
	 * Checks if one of the players have pieces left, and moves left
	 *
	 * @return players still have pieces and moves
	 */
	public boolean checkWin() {
		boolean xExists = false;
		boolean oExists = false;
		
		boolean xHasMoves = false;
		boolean oHasMoves = false;
		
		boolean ogTurn = isPlayerXTurn();
		
		// Check if each player has pieces left and can make a valid move
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board.length; j++) {
				char piece = board[i][j];
				
				if(piece == 'x') {
					playerXTurn = true;
					
					xExists = true;
					
					if(hasValidMoves(j, i, piece)) {
						xHasMoves = true;
						
					}
					
				}
				else if(piece == 'o') {
					playerXTurn = false;
					oExists = true;
					
					if(hasValidMoves(j, i, piece)) {
						
						oHasMoves = true;
						
					}
					
				}
				
			}
			
		}
		
		playerXTurn = ogTurn;
		
		// Return true if either player has no pieces or no valid moves
		return !xExists || !xHasMoves || !oExists || !oHasMoves;
		
	}
	
	/**
	 * Checks if one of the players can make a valid move
	 *
	 * @param xStart    x coordinate of the piece
	 * @param yStart    y coordinate of the piece
	 * @param pieceType type of the piece ('x' or 'o')
	 * @return true if there are valid moves for the given piece
	 */
	private boolean hasValidMoves(int xStart, int yStart, char pieceType) {
		this.takingMove = false;
		int moveDirection; // Player 'x' moves up, 'o' moves down
		
		if(pieceType == 'x') {
			moveDirection = -1;
			
		}
		else {
			moveDirection = 1;
			
		}
		
		// Check normal move (1 step diagonal)
		for(int i = -1; i <= 1; i += 2) { // Check both left (-1) and right (+1)
			if(validMove(xStart, yStart, xStart + i, yStart + (moveDirection))) {
				this.takingMove = true;
				
				return true; // If any valid move is found
			}
		}
		
		// Check for capture moves (2 steps diagonal)
		for(int i = -2; i <= 2; i += 4) { // Check both left (-2) and right (+2)
			if(validMove(xStart, yStart, xStart + i, yStart + (2 * moveDirection))) {
				this.takingMove = true;
				
				return true; // If any valid capturing move is found
			}
		}
		
		this.takingMove = true;
		
		return false; // No valid moves found
		
	}
	
	/**
	 * Converts the move from the computer to a move notation to pass through the processMove method
	 *
	 * @param xStart x coordinate of the piece
	 * @param yStart y coordinate of the piece
	 * @param xEnd   x coordinate of new move
	 * @param yEnd   y coordinate of new move
	 * @return The move in coordinate form
	 */
	public String convertToMove(int xStart, int yStart, int xEnd, int yEnd) {
		String newMove;
		
		// Convert move into coordinate form so that logic can process the move correctly
		yStart = 8 - yStart;
		yEnd = 8 - yEnd;
		char xStartChar = (char) ('a' + xStart);
		char xEndChar = (char) ('a' + xEnd);
		
		newMove = String.format("%d%c-%d%c", yStart, xStartChar, yEnd, xEndChar);
		
		return newMove;
		
	}
	
	/**
	 * Checks if it's player 1's turn
	 *
	 * @return if it is player x's turn
	 */
	public boolean isPlayerXTurn() {
		return playerXTurn;
	}
	
	/**
	 * Manually changing the players turn
	 */
	public void changeTurn() {
		playerXTurn = !playerXTurn;
		
	}
	
}
