/**
 *
 * @author {Name: Ankita Dhar, ID: 1154197, Username: addh}
 * The NimPlayer class is the abstract base class for all player classes
 * It implements properties of player and specifies (declares) removeStone and advancedMove method
 */

abstract public class NimPlayer  {
    /**
     *  @props username is the unique identifier of player, once set cannot be changed
     *  @props givenName is the first name of the player can be edited
     *  @props familyName is the last name of the player can be edited
     *  @props gameCount indicates total number of games played by the player, 
     *          updates automatically, can be reset to 0 by the user while resetting all stats.
     *  @props gameWonCount indicates total number of games won by the player, 
     *          updates automatically, can be reset to 0 by the user while resetting all stats.
     *  @props winPercent represents win percentage of the player, 
     *          updates automatically, can be reset to 0 by the user while resetting all stats.
     *  @props isAI is set to true to distinguish AI players from human players.
     *  @props LOWER_VAL & UPPER_VAL are the variables to indicate the 2 valid inputs 
     *          of stone removal for the advanced NimGame
     */
    private final String USERNAME;
    private String givenName;
    private String familyName;
    private int gameCount;
    private int gameWonCount;
    private float winPercent;
    protected Boolean isAI;
    protected static final int LOWER_VAL = 1;
    protected static final int UPPER_VAL = 2;
    
    /**
     * @param username is the username of the player chosen by user
     * @param familyName is the last name of the player
     * @param givenName is the first name of the player
     * parameterised constructor to create a player with inputs from user
     */
    NimPlayer(String username, String familyName, String givenName) {
        this.USERNAME = username;
        this.givenName = givenName;
        this.familyName = familyName;
    }
    
    /**
     * @param username is the username of the player chosen by user
     * @param familyName is the last name of the player
     * @param givenName is the first name of the player
     * @param gameCount is the number of games played by the player
     * @param gameWonCount is the number of games won by the player
     * parameterised constructor to create a player with inputs on reading from file
     */
    NimPlayer(String username, String familyName, String givenName,
            int gameCount, int gameWonCount) {
        this.USERNAME = username;
        this.givenName = givenName;
        this.familyName = familyName;
        this.gameCount = gameCount;
        this.gameWonCount = gameWonCount;
        this.calcWinPercent();
    }
    
    // constructor for testing games with AI player
    NimPlayer() {
        this.USERNAME = "aiPlayer";
    }
    
    // START : getters and setters of all attributes
    // some of the mutator functions are not coded to avoid privacy leak
    public String getUsername() {
        return this.USERNAME;
    }
    
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGivenName() {
        return this.givenName;
    }
    
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public int getGameCount() {
        return this.gameCount;
    }

    public int getGameWonCount() {
        return this.gameWonCount;
    }
    
    public float getWinPercent() {
        return this.winPercent;
    }
    
    public Boolean getIsAI() {
        return this.isAI;
    }
    // END : getters and setters of all attributes
    
    // START : functions to update game stats for player
    public void updateGameWonCount() {
        this.gameWonCount = this.gameWonCount + 1;
    }
    
    public final void calcWinPercent() {
        if(this.gameCount > 0) {
            this.winPercent = ((float)this.gameWonCount * 100) / this.gameCount;
        }
    }
    
    public void updateGameCount() {
        this.gameCount = this.gameCount + 1;
    }
    
    public void resetStats() {
        this.gameCount = 0;
        this.gameWonCount = 0;
        this.winPercent = 0;
    }
    // END : functions to update game stats for player
    
    /*
     * toString() overridden to return instance details in a readable format
     */
    @Override
    public String toString() {
        return this.USERNAME+","+this.familyName+","+this.givenName+","+
                this.gameCount+","+this.gameWonCount+","+this.isAI;
    }
    
    /**
     * player types that extend to this class has to provide the 
     * function definition for removeStone
     * @param currentStoneCount indicates the total number of stones left to be played with
     * @param upperBound indicates the upper-limit of stone removal count
     * @return number of stones to be removed
     */
    abstract public int removeStone(int currentStoneCount, int upperBound);
    
    /**
     * player types that extend to this class has to provide the 
     * function definition for advancedMove for advanced NimGame
     * @param available indicates the positions of the stones left to be played with
     * @param lastMove indicates the most recent move of the game
     * @return the move decided by the player to play in the format: 
     *          POSITION_OF_STONE<SPACE>NUBER_OF_STONES_TO_REMOVE
     */
    abstract public String advancedMove(boolean[] available, String lastMove);
}