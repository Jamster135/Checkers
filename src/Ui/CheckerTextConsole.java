package Ui;

import javafx.application.Application;
import Core.CheckersComputerPlayer;
import Core.CheckersLogic;
import java.util.Scanner;

/**
 * @author Jacob Barrios
 * @version 2.0, 9/9/2024
 */
public class CheckerTextConsole {
    private CheckersLogic logic;
    private CheckersComputerPlayer computer;
    private CheckersGUI GUI;
    private Scanner scanner;
    // Checks if the player chose to play with a computer or player
    private boolean computerOn;

    /**
     * @name CheckerTextConsole
     */
    public CheckerTextConsole() {
        this.logic = new CheckersLogic();
        this.computer = new CheckersComputerPlayer(logic);
        this.GUI = new CheckersGUI();
        this.scanner = new Scanner(System.in);

    }

    /**
     * @name Menu
     */
    public void Menu() {
        boolean valid = false;
        String playerInput = "";
        String interfaceInput = "";

        while(!valid) {
            displayBoard();
            // Asks if they want to play with a computer or another local player
            System.out.print("Begin Game. Enter ‘P’ if you want to play against another player; enter ‘C’ to play against\n" +
                    "computer: ");
            playerInput = scanner.nextLine();
            playerInput = playerInput.toUpperCase();

            // invalid input exception, throws any input that is not C or P
            try {
                valid = ValidateInput(playerInput);

            }
            catch(InvalidInput ex) {
                System.out.println(ex.getMessage());

            }

        }

        do {
            System.out.print("Enter 'G' if you want to use a GUI; enter 'T' to use Text console\n");

            interfaceInput = scanner.nextLine();
            interfaceInput = interfaceInput.toUpperCase();
            System.out.println(interfaceInput);


        } while(interfaceInput.equals("G") && interfaceInput.equals("T"));

        // Turns computer on if input is C
        if(playerInput.equals("C")) {this.computerOn = true;}

        if(interfaceInput.equals("G")) {
            // Launch GUI and set the computer player
            Application.launch(CheckersGUI.class);

        }
        else {
            startGame();

        }

    }

    /**
     * @name startGame
     */
    public void startGame() {
        String move = "";

        while (true) {
            boolean validInput = false;

            while(!validInput) {
                //Throws invalid move exception if the move is formatted incorrectly
                try {
                    displayBoard();
                    if(logic.isPlayerXTurn()) {System.out.println("Player X - turn.");}
                    else {System.out.println("Player O - turn.");}

                    System.out.print("Choose a cell position of piece to be moved and the new position (e.g., 3a-4b): ");
                    move = scanner.nextLine();
                    ValidateMove(move);
                    validInput = true;

                } catch (InvalidMove e) {
                    System.out.println(e.getMessage());

                }

            }

            // Process move and checks if a player won
            if (logic.processMove(move)) {
                if (logic.checkWin()) {
                    displayBoard();
                    System.out.println("Player " + (logic.isPlayerXTurn() ? "O" : "X") + " wins!");
                    break;
                }
            }
            // Invalid move if move can't be played
            else {System.out.println("Invalid move. Try again.");}

            // Checks if the computer is on
            // If it's on find a move for the computer
            if(logic.isPlayerXTurn() == false && computerOn) {
                String computerMove = computer.moveForComputer();

                if (logic.processMove(computerMove)) {
                    if (logic.checkWin()) {
                        displayBoard();
                        System.out.println("Player " + (logic.isPlayerXTurn() ? "O" : "X") + " wins!");
                        break;

                    }
                }
                else {
                    System.out.println("Computer entered invalid input");

                }

            }
        }
    }

    /**
     * @name displayBoard
     */
    private void displayBoard() {
        char[][] board = logic.getBoard();
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + "|");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    /**
     * @name InvalidInput
     */
    class InvalidInput extends Exception {
        public InvalidInput(String input) {
            super("Invalid input: " + input);

        }

    }

    /**
     * @name ValidateInput
     * @param input
     * @return If input is valid
     * @throws InvalidInput
     */
    public boolean ValidateInput(String input) throws InvalidInput {
        if(input.equals("C") || input.equals("P")) {
            return true;


        }
        else {
            throw new InvalidInput(input);

        }

    }

    /**
     * @name InvalidMove
     */
    class InvalidMove extends Exception {
        public InvalidMove(String move) {
            super("Invalid move: " + move);

        }

    }

    /**
     * @name ValidateMove
     * @param move
     * @return If the move is can be played
     * @throws InvalidMove
     */
    public boolean ValidateMove(String move) throws InvalidMove {
        if(move.length() != 5) {
            throw new InvalidMove(move);

        }

        return true;

    }

}
