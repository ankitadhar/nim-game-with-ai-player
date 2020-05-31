/**
 *
 * @author {Name: Ankita Dhar, ID: 1154197, Username: addh}
 * The NimAIPlayer class implements the AI players and their features
 * in the game - Nim: A Game of Strategy
 * This class inherits properties from the abstract class NimPlayer and
 * defines the methods advancedMove and removeStone for auto winning movements
 * This class implements interface Testable for external testing
 */

import java.util.ArrayList;
import java.util.Collections;

public class NimAIPlayer extends NimPlayer implements Testable {
    
    // the moves and the streaks of the contiguous stones are stored in the string format: "0 5"
    // where 0 is the index where the streak starts and 5 is the length of the streak
    // thus when they are split an array [0,5] is generated, where 0th index gives the index where
    // the streak starts and 1st index gives the stone count
    private static final int STONE_INDEX = 0;
    private static final int STONE_COUNT = 1;

    /**
     * @param username is the username of the AI player chosen by user
     * @param familyName is the last name of the AI player
     * @param givenName is the first name of the AI player
     * parameterised constructor to create an AI player with inputs from user
     */
    NimAIPlayer(String username, String familyName, String givenName) {
        super(username, familyName, givenName);
        this.isAI = true; // isAI is set to true to indicate an AI player
    }

    /**
     * @param username is the username of the AI player chosen by user
     * @param familyName is the last name of the AI player
     * @param givenName is the first name of the AI player
     * @param gameCount is the number of games played by the AI player
     * @param gameWonCount is the number of games won by the AI player
     * parameterised constructor to create an AI player with inputs on reading from file
     */
    NimAIPlayer(String username, String familyName, String givenName,
            int gameCount, int gameWonCount) {
        super(username,familyName,givenName,gameCount,gameWonCount);
        this.isAI = true; // isAI is set to true to indicate an AI player
    }
    
    // constructor for testing games with AI player
    NimAIPlayer() {
        super();
    }

    /**
     * This function auto generates AI player's next move for the advance Nim Game
     * This function also has the implementation of the victory guaranteed strategy
     * The strategy is to always provide the rival with even number of different
     * positions to take stones out from i.e. in the best case rival will have 2 
     * positions to choose from, and this defeats the aim of being the last player to
     * take out stones from last position (to win the game) for the rival.
     * @param available indicates the positions of the stones left to be played with
     *              typically in form of boolean array [true,false,true,...]; where
     *              true indicates that stone is available and false already taken
     * @param lastMove indicates the most recent move of the game
     * @return the move decided by the function to play will be in the format: 
     *          POSITION_OF_STONE<SPACE>NUBER_OF_STONES_TO_REMOVE
     */
    @Override
    public String advancedMove(boolean[] available, String lastMove) {
        String move = null; // stores the move created in this function
        int consecutiveStoneCount = 0; // counter to check steak of available stones
        int indexToRemoveFrom = 0;  // stores the index of the stone 
                                        // from where to remove the stone(s)
        int startIndexStoneCount = 0; // stores the start index of a streak of stones
        int currentStoneCount = 0;      // keeps track of available stone count
        String stonePositionDisplay = "";
        ArrayList<String> consecutiveStoneCountList; // List of <index><space><stone count> 
           // indicating streaks of  consecutive stones (i.e. stones placed in adjacent positions)
           //  with start positions; for example if out of 6 stones only 3rd stone is removed then
           // consecutiveStoneCountList will look like {"0 2","3 3"}; indexing begin at 0
        consecutiveStoneCountList = new ArrayList<>();

        int max = Integer.MIN_VALUE; // holds value of max (longest) streak value
        int maxStoneIndex = 0;      // stores index of max (longest) streak
        ArrayList<Integer> stoneStreaks = new ArrayList<>(); // stores only the streak values
                    // which occur in odd numbers; i.e. if consecutiveStoneCountList has
                    // {"0 2", " 4 2", "7 2", "10 1"} then stoneStreaks will have {2, 1}

        for(int i = 0; i < available.length; i++) {
            if(available[i]) {
                consecutiveStoneCount++;
                stonePositionDisplay += " <" + (i + 1) + ",*>";
                currentStoneCount++; // counting the streak of consecutive stones available
            } else {
                if(consecutiveStoneCount > 0)   {
                    consecutiveStoneCountList.add(startIndexStoneCount + " " 
                            + consecutiveStoneCount); // building the streak list
                }
                startIndexStoneCount = i + 1; // updating start index of next streak
                consecutiveStoneCount = 0; // resetting streak count to 0
                stonePositionDisplay += " <" + (i + 1) + ",x>";
            }
        }
        
        System.out.printf("%d stones left:%s", currentStoneCount, stonePositionDisplay);

        if(consecutiveStoneCount > 0) {
            // adding the last counted streak
            consecutiveStoneCountList.add(startIndexStoneCount + " " + consecutiveStoneCount);
        }
        System.out.println();
        System.out.printf("%s\'s turn - which to remove?\n",this.getGivenName());

        
        for(String _item : consecutiveStoneCountList) {
            consecutiveStoneCount = Integer.parseInt(_item.split(" ")[STONE_COUNT]);
            if (stoneStreaks.contains(consecutiveStoneCount)) {
                // removing any streak count that occurs in even numbers
                stoneStreaks.remove(Integer.valueOf(consecutiveStoneCount));
            } else {
                // adding any streak count that occurs in odd numbers
                stoneStreaks.add(consecutiveStoneCount);
            }
            if(consecutiveStoneCount > max) { 
                // calculating max streak length and it's starting index
                max = consecutiveStoneCount; 
                maxStoneIndex = Integer.parseInt(_item.split(" ")[STONE_INDEX]);
            }
        }
        
        // sorting stoneStreaks list in descending order
        Collections.sort(stoneStreaks, Collections.reverseOrder());
        
        // note: stoneStreaks.get(0) = max
        
        if(0 == consecutiveStoneCountList.size() % 2) {
            // when the consecutiveStoneCountList is even then, the move to make should be inclined
            // towards keeping the number of streaks (elements)in consecutiveStoneCountList as it 
            // is (even)
            if(0 == stoneStreaks.size()) {
                // when all the streaks are of same value i.e. stoneStreaks list is empty;
                // take 1 out of longest (max) streak trying to preseve number of streaks
                move = (maxStoneIndex + 1) + " " + LOWER_VAL;
            } else {
                // when streaks are of different values; then, move should be such that 
                // if possible two of the streak values become same 
                // without disturbing the consecutiveStoneCountList size

                if((max - stoneStreaks.get(1)) == UPPER_VAL || 
                        (max - stoneStreaks.get(1)) == LOWER_VAL) {
                    int diffVal = max - stoneStreaks.get(1);
                    move = (maxStoneIndex + 1) + " " + diffVal;
                } else if((max - stoneStreaks.get(1)) > UPPER_VAL) {
                    // otherwise; break the longest streak in such a way that if we consider
                    // the second longest streak and the 2 new streaks then sum of two streaks 
                    // equals the third streak
                    // for example if streaks currently are {5 2}; then we need to break 5;
                    // so we get {1 3 2}
                    move = findComplexMove(max,stoneStreaks.get(1),LOWER_VAL,maxStoneIndex);
                    if (null == move) {
                        move = findComplexMove(max,stoneStreaks.get(1),UPPER_VAL,maxStoneIndex);
                    } else if (null == move) {
                        // otherwise; simply take out as many stones possible from max streak
                        move = (maxStoneIndex + 1) + " " + UPPER_VAL;
                    }
                }
            }
        } else {
            // when consecutiveStoneCountList (number of streaks) is odd, the move to make should
            // be inclined towards making the number of elements in consecutiveStoneCountList
            // as even

            // if minimum of 3 streaks are available then; try to break the longest streak in a
            // way such that 2 pairs of equal length streaks are formed
            // for example: if streaks currently are {5 2 1}; then we need to break 5;
            // so we get {2 1 2 1}; i.e 
            if(stoneStreaks.size() >= 3 && 
                    ((max - (stoneStreaks.get(1) + stoneStreaks.get(2))) == UPPER_VAL ||
                    (max - (stoneStreaks.get(1) + stoneStreaks.get(2))) == LOWER_VAL)) {
                int diffVal = max - (stoneStreaks.get(1) + stoneStreaks.get(2));

                move = (maxStoneIndex + stoneStreaks.get(1) + 1) + " " + diffVal; 
            } else if(stoneStreaks.size() > 0) {
                // when stone streaks are of different lengths, i.e. stoneStreaks has elements
                // then try to take out all the stones from the minimum streak length (which 
                // will be at the end of the stoneStreaks list) if possible
                // such move will reduce the number of streaks by 1 (making it even)
                if(stoneStreaks.get(stoneStreaks.size() - 1) >= LOWER_VAL && 
                        stoneStreaks.get(stoneStreaks.size() - 1) <= UPPER_VAL) {
                    for(String _item : consecutiveStoneCountList) {
                        consecutiveStoneCount = Integer.parseInt(_item.split(" ")[STONE_COUNT]);
                        if (stoneStreaks.get(stoneStreaks.size() - 1).equals(consecutiveStoneCount)) {
                            indexToRemoveFrom = Integer.parseInt(_item.split(" ")[STONE_INDEX]);
                        }
                    }
                    move = (indexToRemoveFrom + 1) + " " + stoneStreaks.get(stoneStreaks.size() - 1);
                }
            }
            // if such a move is not found where all the stones of a streak can be removed
            // then, the longest streak should be broken in such a way that 2 equal streaks are
            // produced
            if(null == move) {
                if(0 == max % 2) { 
                    // when longest streak is even, take middle two stones of the streak out
                    indexToRemoveFrom = ((max / 2) - 1) + maxStoneIndex;
                    move = (indexToRemoveFrom + 1) + " " + UPPER_VAL;
                } else {
                    // when longest streak is odd, take middle one stones of the streak out
                    indexToRemoveFrom = ((int) Math.ceil((float)max / 2) - 1) + maxStoneIndex;
                    move = (indexToRemoveFrom + 1) + " " + LOWER_VAL;
                }
            }
        }
        System.out.printf(move + "\n\n");
        return move;
    }
    
    private String findComplexMove(int num_big, int num_small, int reductionVal, int stoneIndex) {
        String move = null;
        int num = num_big - reductionVal;
        int left = num/2;
        int right = num - left;
        boolean correctMove = isCorrectMove(left, right, num_small);
        
        while (left > 1 && !correctMove) {
            correctMove = isCorrectMove(--left, ++right, num_small);
        }
        
        if(correctMove) {
            move = (stoneIndex + left + 1) + " " + reductionVal;
        }
        return move;
    }
    
    private boolean isCorrectMove(int num1, int num2, int num3) {
        if(num1 == num2 + num3) {
            return true;
        }
        if(num2 == num1 + num3) {
            return true;
        }
        if(num3 == num2 + num1) {
            return true;
        }
        return false;
    }
    
    /**
     * This function auto generates AI player's next move to play Nim Game
     * The strategy here is to ensure that the rival player is always left with 
     * k(M + 1) + 1 stones, where M is M is the maximum number of stones that can be removed at a 
     * time. Also, the commencing condition should satisfy that the rival player first move and
     * there are k(M + 1) + 1 stones or alternatively, in every winning player's turn there are 
     * some number of stones which cannot be expressed as k(M+1)+1
     * @param currentStoneCount indicates the total number of stones left to be played with
     * @param upperBound indicates the upper-limit of stone removal count
     * @return number of stones to be removed
     */
    @Override
    public int removeStone(int currentStoneCount, int upperBound) {
        int stonesToRemove = 0;// stores the number of stones to be removed by AI player
        // upperLimit of stone removal is calculated for valid move
        int upperLimit = (currentStoneCount > upperBound ? upperBound : currentStoneCount);
        int k = 0;
        System.out.printf("%d stones left:",currentStoneCount);
        for(int i = 0; i < currentStoneCount; i++) {
            System.out.print(" *");
        }
        System.out.println();

        System.out.printf("%s\'s turn - remove how many?\n",this.getGivenName());

        // finding the largest value of k for which stonesToRemove will be valid; 
        // i.e in range [1 to upperLimit]
        while (stonesToRemove > upperLimit || 0 == stonesToRemove) {
            stonesToRemove = currentStoneCount - (((upperLimit + 1) * k) + 1);
            k++;
        }

        // if such value of k is not found then the number of stones are checked for if
        // they are with in range [2 to upperLimit + 1]; if so, all the stones except 1 are 
        // removed. Forcing rival to pick out the last stone and lose the match
        if (stonesToRemove < 1) {
            if(upperLimit <= upperBound && upperLimit > 1) {
                stonesToRemove = upperLimit - 1;
            } else {
                // for the very rare case when none of the above conditions hold good,
                // a number is picked randomly from [1 to upperLimit - 1]
                stonesToRemove = 1 + (int)((upperLimit - 1) * Math.random());
            }
        }
        System.out.println();
        return stonesToRemove;
    }
}
