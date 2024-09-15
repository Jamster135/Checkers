package Core;

public class CheckersComputerPlayer {
    private CheckersLogic logic;

    /**
     * @name CheckersComputer
     * @param logic
     */
    public CheckersComputerPlayer(CheckersLogic logic) {
        this.logic = logic;

    }

    /**
     * @name MoveForComputer
     * @return Move made from the computer
     */
    public String moveForComputer() {
        String move = "";
        char[][] tempBoard = this.logic.getBoard();

        // Iterates through the whole board to find all the o pieces
        for(int i = 0; i < tempBoard.length; i++) {
            for(int j = 0; j < tempBoard[i].length; j++) {
                if(tempBoard[i][j] == 'o') {
                    // Checks if the piece can capture a piece or make a move
                    if(validComputerMove(j,i,j+2,i+2)) {
                        move = logic.convertToMove(j,i,j+2, i+2);
                        return move;

                    }
                    else if(validComputerMove(j,i,j-2,i+2)) {
                        move = logic.convertToMove(j,i,j-2,i+2);
                        return move;

                    }
                    else if(validComputerMove(j,i,j+1,i+1)) {
                        move = logic.convertToMove(j,i,j+1,i+1);
                        return move;

                    }
                    else if(validComputerMove(j,i,j-1,i+1)) {
                        move = logic.convertToMove(j,i,j-1,i+1);
                        return move;

                    }

                }

            }

        }

        return "";

    }

    /**
     * @name validComputerMove
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     * @return If the computer produced a valid move
     */
    private boolean validComputerMove(int xStart, int yStart, int xEnd, int yEnd) {
        char[][] tempBoard = logic.getBoard();

        // Check if the move is within bounds and the target cell is empty.
        if (xStart < 0 || xStart >= 8 || yStart < 0 || yStart >= 8 || xEnd < 0 || xEnd >= 8 || yEnd < 0 || yEnd >= 8) {return false;}

        // Is the coordinate the piece wants to move to empty
        if (tempBoard[yEnd][xEnd] != '_') {return false;}

        // Check if the piece is moving only 1 step in any direction
        if (Math.abs(xStart - xEnd) == 1 && Math.abs(yStart - yEnd) == 1) {
            if ((xEnd - xStart) == 1){return true;}
        }

        // Check for capturing move.
        if (Math.abs(xStart - xEnd) == 2 && Math.abs(yStart - yEnd) == 2) {
            int xMiddle = (xStart + xEnd) / 2;
            int yMiddle = (yStart + yEnd) / 2;

            if (tempBoard[yMiddle][xMiddle] == 'x') {

                return true;

            }
        }

        return false;

    }


}
