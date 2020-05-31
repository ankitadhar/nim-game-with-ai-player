/**
 *
 * @author {Name: Ankita Dhar, ID: 1154197, Username: addh}
 * The NimHumanPlayer class implements the human players of the game - Nim: A Game of Strategy
 * This class inherits properties from the abstract class NimPlayer and
 * defines the methods advancedMove and removeStone
 */

import java.util.Scanner;


public class NimHumanPlayer extends NimPlayer {

    // scanner is required to take inputs from the human player
    private static Nimsys system = new Nimsys();
    // following naming convention of constant for constant scanner object
    private static final Scanner KEYIN = system.getScanner();

    /**
     * @param username is the username of the player chosen by user
     * @param familyName is the last name of the player
     * @param givenName is the first name of the player
     * parameterised constructor to create a human player with inputs from user
     */
    NimHumanPlayer(String username, String familyName, String givenName) {
        super(username, familyName, givenName);
        this.isAI = false; // isAI is set to false to indicate a human player
    }

    /**
     * @param username is the username of the player chosen by user
     * @param familyName is the last name of the player
     * @param givenName is the first name of the player
     * @param gameCount is the number of games played by the player
     * @param gameWonCount is the number of games won by the player
     * parameterised constructor to create a human player with inputs on reading from file
     */
    NimHumanPlayer(String username, String familyName, String givenName,
            int gameCount, int gameWonCount) {
        super(username,familyName,givenName,gameCount,gameWonCount);
        this.isAI = false; // isAI is set to false to indicate a human player
    }
    
    /**
     * This function takes input from user to play advance Nim Game
     * @param available indicates the positions of the stones left to be played with
     *              typically in form of boolean array [true,false,true,...]; where
     *              true indicates that stone is available and false already taken
     * @param lastMove indicates the most recent move of the game
     * @return the move decided by the player to play in the format: 
     *          POSITION_OF_STONE<SPACE>NUBER_OF_STONES_TO_REMOVE
     */
    @Override
    public String advancedMove(boolean[] available, String lastMove) {
        String move;    // stores move as provided by the player in console
        int currentStoneCount;  // count of stones available
        int stonesToRemove;     // stores the number of stones to be removed by player
        int stonesToRemoveFromIndex;    // stores the index of the stone 
                                        // from where to remove the stone(s)
        String stonePositionDisplay;

        do {
            // display stones available with their positions
            currentStoneCount = 0;
            stonePositionDisplay = "";
            for(int i = 0; i < available.length; i++) {
                if(available[i]) {
                    stonePositionDisplay += " <" + (i + 1) + ",*>";
                    currentStoneCount++;
                } else {
                    stonePositionDisplay += " <" + (i + 1) + ",x>";
                }
            }

            System.out.printf("%d stones left:%s", currentStoneCount, stonePositionDisplay);

            System.out.println();
            System.out.printf("%s\'s turn - which to remove?\n",this.getGivenName());
            move = KEYIN.nextLine();    // fetching player's move from console
                               // in the format POSITION_OF_STONE<SPACE>NUBER_OF_STONES_TO_REMOVE
            System.out.println();
            
            try {
                // isolating stonesToRemoveFromIndex and stonesToRemove from player's input in move
                stonesToRemoveFromIndex = Integer.parseInt(move.split(" ")[0]);
                stonesToRemove = Integer.parseInt(move.split(" ")[1]);
                
                /*
                first set of conditions test:
                1. if the stonesToRemove is one of the valid inputs []
                2. if stonesToRemoveFromIndex is within range -> [1 to number-of-stone-positions]
                3. if number of stones to remove from  given position
                    (stonesToRemoveFromIndex) is possible
                */
                /*
                second and third coditions test if the stone(s) is(/are) available at the
                    position(s) (stonesToRemoveFromIndex & stonesToRemoveFromIndex + 1)
                    depending on how many stones player wants to remove.
                */
                if(!(stonesToRemove == LOWER_VAL || stonesToRemove == UPPER_VAL) || 
                    stonesToRemoveFromIndex < 1 || stonesToRemoveFromIndex > available.length ||
                        (stonesToRemoveFromIndex > (available.length - stonesToRemove + 1)) ) {
                    System.out.println("Invalid move.\n");
                } else if(!available[stonesToRemoveFromIndex - 1]) {
                    System.out.println("Invalid move.\n");
                } else if(UPPER_VAL == stonesToRemove && !available[stonesToRemoveFromIndex]) {
                    System.out.println("Invalid move.\n");
                } else {
                    move = (stonesToRemoveFromIndex) + " " + stonesToRemove;
                    break;
                }
            } catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
                // checks for invalid input type and invalid format of input
                System.out.println("Invalid move.\n");
            }
        } while (true); // loop runs till a valid input is provided by the player
        return move;
    }

    /**
     * This function takes input from user to play Nim Game
     * @param currentStoneCount indicates the total number of stones left to be played with
     * @param upperBound indicates the upper-limit of stone removal count
     * @return number of stones to be removed
     */
    @Override
    public int removeStone(int currentStoneCount, int upperBound) {
        int stonesToRemove; // stores the number of stones to be removed by player
        int upperLimit = (currentStoneCount > upperBound ? upperBound : currentStoneCount);
        
        do {
            // display stones available with their positions
            System.out.printf("%d stones left:",currentStoneCount);
            for(int i = 0; i < currentStoneCount; i++) {
                System.out.print(" *");
            }
            System.out.println();

            System.out.printf("%s\'s turn - remove how many?\n",this.getGivenName());
            stonesToRemove = KEYIN.nextInt(); // fetching player's input from console
            KEYIN.nextLine();
            System.out.println();

            // condition to check valid input for the sotnes to remove
            // stones to remove should be within the range of 
            // 1 to either current available stones or upper bound whichever is least
            if (stonesToRemove < 1 || stonesToRemove > upperLimit) {
                System.out.println("Invalid move. You must remove between "
                               + "1 and "+upperLimit+" stones.\n");
                continue;
            }
            break;
        } while(true); // loop runs till a valid input is provided by the player
        return stonesToRemove;
    }
}
