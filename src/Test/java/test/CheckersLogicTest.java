package test;

import Core.CheckersLogic;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The class test all the functions of the logic class
 *
 * @author Jacob Barrios
 * @version 1.0, 9/30/2024
 */
class CheckersLogicTest {
	private static CheckersLogic testLogic;
	
	/**
	 * Initialize the class to test before starting test
	 */
	@BeforeAll
	public static void setupBeforeClass() {
		testLogic = new CheckersLogic();
		
	}
	
	/**
	 * Before each test create a new board
	 */
	@BeforeEach
	public void setUpBeforeNextTest() {
		testLogic = new CheckersLogic();
		testLogic.newGameBoard();
		
	}
	
	/**
	 * Set the class equal to null after tests are completed
	 */
	@AfterAll
	public static void tearDownAfterClass() {
		testLogic = null;
		
	}
	
	/**
	 * Test to create a new board and to make sure it's setup properly
	 */
	@Test
	public void newGameBoardTest() {
		char[][] board = testLogic.getBoard();
		
		// Check dimensions
		assertEquals(8, board.length, "Board should have 8 rows.");
		for(char[] row : board) {
			assertEquals(8, row.length, "Each row should have 8 columns.");
		}
		
		// Validate initial setup for Player 'o' (top)
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 8; j++) {
				if((i + j) % 2 == 1) {
					assertEquals('o', board[i][j], "Player 'o' pieces should be in the top 3 rows on dark squares.");
				}
				else {
					assertEquals('_', board[i][j], "Non-playable squares should be empty.");
				}
			}
		}
		
		// Validate initial setup for Player 'x' (bottom)
		for(int i = 5; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if((i + j) % 2 == 1) {
					assertEquals('x', board[i][j], "Player 'x' pieces should be in the bottom 3 rows on dark squares.");
				}
				else {
					assertEquals('_', board[i][j], "Non-playable squares should be empty.");
				}
			}
		}
		
		// Validate middle rows are empty
		for(int i = 3; i < 5; i++) {
			for(int j = 0; j < 8; j++) {
				assertEquals('_', board[i][j], "Middle rows should be empty.");
			}
		}
		
	}
	
	/**
	 * Processes the move when it's valid
	 */
	@Test
	public void processMoveTest1() {
		String move = "3a-4b";
		char[][] board = testLogic.getBoard();
		
		boolean isValid = testLogic.processMove(move);
		
		//Checks if the move is valid
		assertTrue(isValid, "Cannot process move");
		
		//Checks if the move was made and updated the board
		assertEquals('x', board[4][1], "The piece should be at the new position (4, b).");
		assertEquals('_', board[5][0], "The original position (3, a) should now be empty.");
		
	}
	
	/**
	 * Processes the move when it's invalid
	 */
	@Test
	public void processMoveTest2() {
		String move = "3b-5c";
		
		boolean isValid = testLogic.processMove(move);
		
		//Checks if the move is valid
		assertFalse(isValid, "Cannot process move");
		
	}
	
	/**
	 * Tests a capture move for x
	 */
	@Test
	public void captureMoveTest1() {
		clearBoard();
		String move = "5c-7e";
		
		testLogic.getBoard()[3][2] = 'x';
		testLogic.getBoard()[2][3] = 'o';
		
		boolean capturedPiece = testLogic.processMove(move);
		
		char[][] board = testLogic.getBoard();
		
		assertTrue(capturedPiece, "Did not process capture move");
		
		assertEquals('x', board[1][4], "The piece should be at the new position (7, e).");
		assertEquals('_', board[2][3], "The captured piece should no longer be here");
		assertEquals('_', board[3][2], "The original position (5, c) should now be empty.");
		
	}
	/**
	 * Tests a capture move for o
	 */
	@Test
	public void captureMoveTest2() {
		clearBoard();
		testLogic.changeTurn();
		String move = "6d-4b";
		
		testLogic.getBoard()[3][2] = 'x';
		testLogic.getBoard()[2][3] = 'o';
		
		boolean capturedPiece = testLogic.processMove(move);
		
		char[][] board = testLogic.getBoard();
		
		assertTrue(capturedPiece, "Did not process capture move");
		
		assertEquals('o', board[4][1], "The piece should be at the new position (4, b).");
		assertEquals('_', board[2][3], "The piece should not be in the original position");
		assertEquals('_', board[3][2], "The captured piece should no longer be here");
		
	}
	
	/**
	 * Win condition with pieces existing, but no more moves
	 */
	@Test
	public void winCondition1() {
		clearBoard();
		
		testLogic.getBoard()[5][4] = 'x';
		testLogic.getBoard()[7][2] = 'o';
		
		assertTrue(testLogic.checkWin(), "There should be a winner");
		
		testLogic.getBoard()[5][4] = '_';
		testLogic.getBoard()[7][2] = '_';
		
		testLogic.changeTurn();
		testLogic.getBoard()[0][0] = 'x';
		testLogic.getBoard()[4][4] = 'o';
		
	}
	
	/**
	 * Win condition with no more pieces
	 */
	@Test
	public void winCondition2() {
		clearBoard();
		
		testLogic.getBoard()[4][4] = 'x';
		
		assertTrue(testLogic.checkWin(), "There should be a winner");
		
		testLogic.getBoard()[4][4] = 'o';
		
		assertTrue(testLogic.checkWin(), "There should be a winner");
		
	}
	
	/**
	 * Tests if the game continues if both players have pieces and valid moves
	 */
	@Test
	public void noWinConditio1() {
		clearBoard();
		
		testLogic.getBoard()[7][0] = 'x';
		testLogic.getBoard()[1][0] = 'o';
		
		assertFalse(testLogic.checkWin(), "Game should keep going");
		
		testLogic.changeTurn();
		
		assertFalse(testLogic.checkWin(), "Game should keep going");
		
	}
	/**
	 * Tests if the game continues if both player have pieces and a valid capture move
	 */
	@Test
	public void noWinCondition2() {
		clearBoard();
		
		testLogic.getBoard()[3][0] = 'x';
		testLogic.getBoard()[3][2] = 'x';
		testLogic.getBoard()[3][4] = 'x';
		testLogic.getBoard()[3][6] = 'x';
		testLogic.getBoard()[2][7] = 'o';
		testLogic.getBoard()[2][5] = 'o';
		testLogic.getBoard()[2][3] = 'o';
		testLogic.getBoard()[2][1] = 'o';
		
		assertFalse(testLogic.checkWin(), "Game should keep going");
		
		testLogic.changeTurn();
		
		assertFalse(testLogic.checkWin(), "Game should keep going");
		
	}
	
	/**
	 * Clears the board to edit
	 */
	public void clearBoard() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				testLogic.getBoard()[i][j] = '_';
				
			}
			
		}
		
	}
	
}