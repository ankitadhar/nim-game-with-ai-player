/**
 *
 * @author {Name: Ankita Dhar, ID: 1154197, Username: addh}
 * The NimGame class implements rules of the game - Nim; with the players specified by the user
 */

import java.util.Arrays;


class NimGame {

    /**
     *  @props currentStoneCount represents the number of stones left on the table at a time
     *  @props upperBound indicates the upper-limit of stone removal count for a game
     *  @props player1 and player2 represent players of the game
     *  @props stonesAvailable indicates the stones available along with their position
     */
    private int currentStoneCount;
    private int upperBound;
    private NimPlayer player1;
    private NimPlayer player2;
    private boolean[] stonesAvailable;
    
    /**
     * @param currentStoneCount is the total number of stones given by user to begin the game with
     * @param upperBound is the upper-limit of stone removal count given by the user
     * @param player1 and player2 are the 2 players specified by the user to play the game
     * parameterised constructor to create a game with inputs from user
     */
    NimGame(int currentStoneCount, int upperBound, NimPlayer player1, NimPlayer player2) {
        // condition to check valid input for upper bound and 
        // current stone count of initial number of stones to begin the game with
        if(currentStoneCount < 1 || upperBound < 1) {
            System.out.println("Please enter positive value (more than 0) for initial number of "
                    + "stones to begin with and the upperbound of stones for removal!\n");
            return;
        }
        this.currentStoneCount = currentStoneCount;
        this.upperBound = upperBound;
        this.player1 = player1;
        this.player2 = player2;
        // updating game count of the players as they begin playing a new game
        this.player1.updateGameCount();
        this.player2.updateGameCount();
        this.startGame(); // let the Nim game begin
    }
    
    /**
     * @param currentStoneCount is the total number of stones given by user to begin the game with
     * @param player1 and player2 are the 2 players specified by the user to play the game
     * parameterised constructor to create an advanced game with inputs from user
     */
    NimGame(int currentStoneCount, NimPlayer player1, NimPlayer player2) {
        // condition to check valid input for initial number of stones to begin the game with
        if(currentStoneCount < 1) {
            System.out.println("Please enter positive value (more than 0) for initial number of "
                    + "stones to begin with!\n");
            return;
        }
        this.currentStoneCount = currentStoneCount;
        // creates a position array for the stones and make all of them available,
        // i.e, fill them with true values
        this.stonesAvailable = new boolean[currentStoneCount];
        Arrays.fill(stonesAvailable, Boolean.TRUE);
        this.player1 = player1;
        this.player2 = player2;
        // updating game count of the players as they begin playing a new game
        this.player1.updateGameCount();
        this.player2.updateGameCount();
        this.startAdvanceGame(); // let the advanced Nim game begin
    }

    // START : getters and setters of all attributes
    private void setCurrentStoneCount(int currentStoneCount) {
        this.currentStoneCount = currentStoneCount;
    }

    public int getCurrentStoneCount() {
        return this.currentStoneCount;
    }

    public int getUpperBound() {
        return this.upperBound;
    }
    
    public NimPlayer getPlayer1() {
        return player1;
    }
    
    public NimPlayer getPlayer2() {
        return player2;
    }
    // END : getters and setters of all attributes
    
    /** 
     *  startGame implements the logic of the game
     *  game rules : each player has to remove stones [1-upperbound] from the 
     *         total number of stones present rotating their turns to do so.
     *         Whoever has to take out the last stone loses, the other wins.
     */
    private void startGame() {
        NimPlayer turn = this.getPlayer1(); // var turn represents turn of player to play the game
        int stonesToRemove; // var stonesToRemove stores the stone removal count for a player
        System.out.println();
        
        // displaying game details
        System.out.println("Initial stone count: "+this.currentStoneCount);
        System.out.println("Maximum stone removal: "+this.upperBound);
        System.out.printf("Player 1: %s %s\n",this.player1.getGivenName(),
                this.player1.getFamilyName());
        System.out.printf("Player 2: %s %s\n",this.player2.getGivenName(),
                this.player2.getFamilyName());
        System.out.println();

        // till the total number of stones left on the table are more than 0 
        // turn of players keep rotating
        while (this.currentStoneCount > 0) {
            // fetch input to remove stones from player
            stonesToRemove = turn.removeStone(this.currentStoneCount, this.upperBound);
            // update game attributes
            this.setCurrentStoneCount(currentStoneCount - stonesToRemove);
            if(turn == player1) {
                turn = player2;
            } else {
                turn = player1;
            }
        }
        
        System.out.println("Game Over");
        System.out.printf("%s %s wins!", turn.getGivenName(), turn.getFamilyName());
        // update player stats
        turn.updateGameWonCount();
        player1.calcWinPercent();
        player2.calcWinPercent();
        System.out.println("\n");
    }
    
    /** 
     *  startAdvanceGame implements the logic of the advanced nim game
     *  game rules : each player has to remove stones [1 or 2 adjacent placed stones] from the 
     *         total number of stones present rotating their turns to do so.
     *         Whoever takes out the last stone wins.
     */
    private void startAdvanceGame() {
        NimPlayer turn = this.getPlayer2(); // var turn represents turn of player to play the game
        String lastMove = "0 0"; // latest move is stored in lastMove
        int stonesToRemove; // var stonesToRemove stores the stone removal count for a player
        int stonesToRemoveFromIndex; // stores the index of the stone 
                                        // from where to remove the stone(s)
        System.out.println();
        
        // displaying game details
        System.out.println("Initial stone count: "+this.currentStoneCount);
        System.out.print("Stones display:");
        for(int i = 0; i < stonesAvailable.length; i++) {
            System.out.print(" <" + (i + 1) + ",*>");
        }
        System.out.println();
        System.out.printf("Player 1: %s %s\n",this.player1.getGivenName(),
                this.player1.getFamilyName());
        System.out.printf("Player 2: %s %s\n",this.player2.getGivenName(),
                this.player2.getFamilyName());
        System.out.println();
        
        // till the total number of stones left on the table are more than 0 
        // turn of players keep rotating
        while (this.currentStoneCount > 0) {
            if(turn == player1) {
                turn = player2;
            } else {
                turn = player1;
            }
            // fetch input to remove stones from player
            lastMove = turn.advancedMove(stonesAvailable, lastMove);
            stonesToRemoveFromIndex = Integer.parseInt(lastMove.split(" ")[0]);
            stonesToRemove = Integer.parseInt(lastMove.split(" ")[1]);
            stonesAvailable[stonesToRemoveFromIndex - 1] = false;
            if(2 == stonesToRemove) {
                stonesAvailable[stonesToRemoveFromIndex] = false;
            }
            // update game attributes
            this.setCurrentStoneCount(currentStoneCount - stonesToRemove);
        }
        
        System.out.println("Game Over");
        System.out.printf("%s %s wins!", turn.getGivenName(), turn.getFamilyName());
        // update player stats
        turn.updateGameWonCount();
        player1.calcWinPercent();
        player2.calcWinPercent();
        System.out.println("\n");
    }
}