package test;

import Core.CheckersComputerPlayer;
import Core.CheckersLogic;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This classes tests the functions of the computer class
 *
 * @author Jacob Barrios
 * @version 1.0, 9/30/2024
 */
public class CheckersComputerPlayerTest {
	private CheckersComputerPlayer testComputer;
	private CheckersLogic testLogic;
	
	/**
	 * Initialize the logic and computer class to test
	 */
	@BeforeEach
	public void setupBeforeTest() {
		testLogic = new CheckersLogic();
		testComputer = new CheckersComputerPlayer(testLogic);
		
	}
	
	/**
	 * Tests only if the computer generates a move and recognizes when it doesn't have any left
	 */
	@Test
	public void generatesAMove() {
		clearBoard();
		testLogic.changeTurn();
		String moveFromComputer;
		
		testLogic.getBoard()[2][3] = 'o';
		
		moveFromComputer = testComputer.moveForComputer();
		assertEquals("6d-5e", moveFromComputer, "Move should produce 6d-5e");
		
		testLogic.getBoard()[3][4] = 'x';
		testLogic.getBoard()[4][5] = 'x';
		
		moveFromComputer = testComputer.moveForComputer();
		assertEquals("6d-5c", moveFromComputer, "Move should produce 6d-5c");
		
		testLogic.getBoard()[3][2] = 'x';
		testLogic.getBoard()[4][1] = 'x';
		
		moveFromComputer = testComputer.moveForComputer();
		assertEquals("No more moves", moveFromComputer, "Message should say no more moves");
		
		
	}
	
	/**
	 * Tests if the computer generates a capture move and recognizes when it can't capture any pieces
	 */
	@Test
	public void generateCaptureMove() {
		clearBoard();
		testLogic.changeTurn();
		String moveFromComputer;
		
		testLogic.getBoard()[2][3] = 'o';
		testLogic.getBoard()[3][4] = 'x';
		
		moveFromComputer = testComputer.moveForComputer();
		assertEquals("6d-4f", moveFromComputer, "Move should produce 6d-4f");
		
		testLogic.getBoard()[4][5] = 'x';
		testLogic.getBoard()[3][2] = 'x';
		
		moveFromComputer = testComputer.moveForComputer();
		assertEquals("6d-4b", moveFromComputer, "Move should produce 6d-4b");
		
		testLogic.getBoard()[4][1] = 'x';
		
		moveFromComputer = testComputer.moveForComputer();
		assertEquals("No more moves", moveFromComputer, "Message should say no more moves");
		
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
