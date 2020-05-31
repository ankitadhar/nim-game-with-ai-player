/**
 *
 * @author {Name: Ankita Dhar, ID: 1154197, Username: addh}
 * The Nimsys program implements a system to play the game - Nim: A Game of Strategy
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * The Nimsys class implements the main function of the program
 */
public class Nimsys {
    // following naming convention of constant for constant scanner object
    private static final Scanner KEYIN = new Scanner(System.in);
    private static final String FILE_NAME = "players.dat"; // file to store players' data
    private static ArrayList<NimPlayer> players = new ArrayList<NimPlayer>();
    private NimGame game;   // instance of the NimGame to be created when a game is to be played

    Nimsys() {
    }
    
    public Scanner getScanner() {
        return KEYIN;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // declaration of required local variables
        String username;
        String command; // indicates command as entered by the user
        String gameAttr[];
        NimPlayer player1; // instance of Nimplayer to store the player1 of the game
        NimPlayer player2; // instance of Nimplayer to store the player2 of the game
        Boolean sortDescFlag; // to determine ascending/descending order of displaying rankings
        Nimsys system = new Nimsys(); // object created of the class 
                                         // to access non-static methods from static main method
        FileOutputStream outStream = null;
        File file = null;
        try {
            // create a players.dat file in case not already present in the file system
            file = new File(FILE_NAME);
        } catch (Exception e) {
            System.out.println("Error while fetching file");
        }
        
        // to restore all the players already added to the system
        system.restoreSystemState();

        // system initializes
        System.out.println("Welcome to Nim");
        System.out.println("");
        
        // system stays active and expects input till 'exit' command is not entered
        while(true) {
            
            System.out.print("$");
            command = KEYIN.nextLine();
            
            StringTokenizer cmdTokens = new StringTokenizer(command);
            
            if(cmdTokens.hasMoreTokens()) {
                command = cmdTokens.nextToken();
                switch (command) {
                    // command to add player begins with addplayer
            // syntax: addplayer <username>,<family name>,<given name>
                    case "addplayer":
                        if(cmdTokens.hasMoreTokens()) {
                            system.addPlayer(cmdTokens.nextToken(), false);
                        } else {
                            // catches exception in case of fewer arguments
                            System.out.println("Incorrect number of arguments supplied to command.\n");
                        }   break;
                    // command to add AI player begins with addaiplayer
            // syntax: addaiplayer <username>,<family name>,<given name>
                    case "addaiplayer":
                        if(cmdTokens.hasMoreTokens()) {
                            system.addPlayer(cmdTokens.nextToken(), true);
                        } else {
                            System.out.println("Incorrect number of arguments supplied to command.\n");
                        }   break;
                    // command to remove a player begins with removeplayer
            // syntax: removeplayer <username>
                    case "removeplayer":
                        if(cmdTokens.hasMoreTokens()) {
                            username = cmdTokens.nextToken();
                        } else {
                            // if no username is entered then empty string is passed to the function
                            username = "";
                        }   system.removePlayer(username);
                        break;
                    // command to edit a player begins with editplayer
            // syntax: editplayer <username>,<new family name>,<new given name>
                    case "editplayer":
                        if(cmdTokens.hasMoreTokens()) {
                            system.editPlayer(cmdTokens.nextToken());
                        } else {
                            System.out.println("Incorrect number of arguments supplied to command.\n");
                        }   break;
                    // command to reset stats of a player begins with resetstats
            // syntax: resetstats <username>
                    case "resetstats":
                        if(cmdTokens.hasMoreTokens()) {
                            username = cmdTokens.nextToken();
                        } else {
                            // if no username is entered then empty string is passed to the function
                            username = "";
                        }   system.resetStats(username);
                        break;
                    // command to display a player begins with displayplayer
            // syntax: displayplayer <username>
                    case "displayplayer":
                        if(cmdTokens.hasMoreTokens()) {
                            username = cmdTokens.nextToken();
                        } else {
                            // if no username is entered then empty string is passed to the function
                            username = "";
                        }   system.displayPlayer(username);
                        break;
                    // command to display rankings of players begin with rankings
            // syntax: rankings <"asc"/"desc">
                    case "rankings":
                        if(cmdTokens.hasMoreTokens()) {
                            String argument = cmdTokens.nextToken().split(",")[0];
                            sortDescFlag = !argument.equals("asc");
                        } else {
                            // if 'asc' or 'desc' not specified then
                            // sort in descending order by default
                            sortDescFlag = true;
                        }   system.displayRangkings(sortDescFlag);
                        break;
                    // command to start a game begins with startgame
            // syntax: startgame <no. of stones to start>,<upper bound>,<username1>,<username2>
                    case "startgame":
                        if(cmdTokens.hasMoreTokens()) {
                            try {
                                command = cmdTokens.nextToken();
                                gameAttr = command.split(",");
                                player1 = system.findPlayer(gameAttr[2]);
                                player2 = system.findPlayer(gameAttr[3]);
                                // condition to check if both the players exist in the list of players
                                if(!(player1 == null || player2 == null)) {
                                    // initializing game (instance of NimGame) to start playing the game.
                                    system.game = new NimGame(Integer.parseInt(gameAttr[0]),
                                            Integer.parseInt(gameAttr[1]), player1, player2);
                                } else {
                                    System.out.println("One of the players does not exist.\n");
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // catches exception in case of fewer arguments
                                System.out.println("Incorrect number of arguments "
                                        + "supplied to command.\n");
                            }
                        }   break;
                    // command to start a game begins with startadvancedgame
            // syntax: startadvancedgame <no. of stones to start>,<username1>,<username2>
            // for this game a player can remove either 1 stone or 2 stones in adjacent positions
                    case "startadvancedgame":
                        if(cmdTokens.hasMoreTokens()) {
                            try {
                                command = cmdTokens.nextToken();
                                gameAttr = command.split(",");
                                player1 = system.findPlayer(gameAttr[1]);
                                player2 = system.findPlayer(gameAttr[2]);
                                // condition to check if both the players exist in the list of players
                                if(!(player1 == null || player2 == null)) {
                                    // initializing game (instance of NimGame) to start playing the game.
                                    system.game = new NimGame(Integer.parseInt(gameAttr[0]), 
                                            player1, player2);
                                } else {
                                    System.out.println("One of the players does not exist.\n");
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                // catches exception in case of fewer arguments
                                System.out.println("Incorrect number of arguments "
                                        + "supplied to command.\n");
                            }
                        }   break;
                    // command to exit the system is exit
                    case "exit":
                        System.out.println();
                        try {
                            // store player details in file before exiting
                            outStream = new FileOutputStream(file);
                            outStream.write(players.toString().getBytes());
                        } catch(FileNotFoundException e) {
                            System.out.println(FILE_NAME + " file not found");
                        } finally {
                            if(outStream != null) {
                                outStream.close();
                            }
                        }
                        System.exit(0);
                    default:
                        // in case none of the commands are matched, an error will be shown
                        System.out.printf("'%s' is not a valid command.%n%n",command);
                        break;
                }
            }
        }
    }
    
    /**
     * The function restores players' details into system from file
     */
    public void restoreSystemState() {
        String fileContent = "";
        File file;
        NimPlayer player;
        
        try {
            file = new File(FILE_NAME);
            if(file.exists()) {
                StringBuilder playerData = new StringBuilder();
                BufferedReader bufferReader = new BufferedReader(new FileReader(file));
                // reading file line by line to fetch all players' data
                String streamCurLine;
                while ((streamCurLine = bufferReader.readLine()) != null) 
                {
                    playerData.append(streamCurLine).append("\n");
                }
                fileContent = playerData.toString();
                    // excluding '[' & ']'
                fileContent = fileContent.substring(1, fileContent.length()-2); 
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Error while fetching file.");
        }
        
        // creating an array from the details read from file
        StringTokenizer playerList = new StringTokenizer(fileContent);

        // instantiating appropriate player class for each player based on the player type
        // i.e. based on isAI flag of NimPlayer object
        while(playerList.hasMoreTokens()) {
            String playerStr = playerList.nextToken();
            String playerDetails[] = playerStr.split(",");
            if (true == Boolean.parseBoolean(playerDetails[5])) {
                player = new NimAIPlayer(playerDetails[0], playerDetails[1], playerDetails[2],
                    Integer.parseInt(playerDetails[3]), Integer.parseInt(playerDetails[4]));
            } else {
                player = new NimHumanPlayer(playerDetails[0], playerDetails[1], playerDetails[2],
                    Integer.parseInt(playerDetails[3]), Integer.parseInt(playerDetails[4]));
            }
            players.add(player);
        }
    }
    
    /**
     * sortPlayers method ranks the players by considering their win percentage
     * @param sortDescFlag
     * @return player list to display for rankings
     */
    public ArrayList<NimPlayer> sortPlayers(Boolean sortDescFlag) {
        // using insertion sort algorithm for the almost sorted player list
        for(int i = 1; i < players.size(); i++) {
            NimPlayer key = players.get(i);
            int j = i - 1;
            float leftPercentage = players.get(j).getWinPercent();
            float rightPercentage = key.getWinPercent();
            if (!sortDescFlag) {
                leftPercentage = key.getWinPercent();
                rightPercentage = players.get(j).getWinPercent();
            }
  
            /* Move players in the range [0..i-1], that have greater win percent 
               than that of key, to one position ahead of their current position */
            while (j >= 0 && leftPercentage < rightPercentage) {
                players.set(j + 1, players.get(j));
                j = j - 1;
                if (j >= 0 && sortDescFlag) {
                    leftPercentage = players.get(j).getWinPercent();
                } else if (j >= 0) {
                    rightPercentage = players.get(j).getWinPercent();
                }
            }
            /* Using the same logic for players with same win percent 
                to sort players alphabetically always in ascending order */
            while (j >= 0 && players.get(j).getWinPercent() == key.getWinPercent()
                    && players.get(j).getUsername().compareTo(key.getUsername()) > 0) {
                players.set(j + 1, players.get(j));
                j = j - 1; 
            }
            
            players.set(j + 1, key);
        }
        return players;
    }
    
    /**
     * findPlayer confirms existence of a player in the players list
     * @param username is the username of the player to be searched in the players list
     * @return player if found in the players list else return null
     */
    private NimPlayer findPlayer(String username) {
        NimPlayer player = null;
        for(int i = 0; i < players.size(); i++) {
            if (username.equals(players.get(i).getUsername())) {
                player = players.get(i);
                break;
            }
        }
        return player;
    }
    
    /**
     * addPlayer method adds an appropriate new player to the players list
     * @param playerDetails stores details of the new player
     * @param isAI flag indicates if the player is AI or human kind
     *          value true implies AI player
     */
    public void addPlayer(String playerDetails, Boolean isAI) {
        String playerData[] = playerDetails.split(",");
        try {
            NimPlayer player = findPlayer(playerData[0]); // to test if the player already exists
            if(player != null) {
                System.out.println("The player already exists.");
            }
            else if(isAI) {
                player = new NimAIPlayer(playerData[0], playerData[1], playerData[2]);
                players.add(player);
            } else {
                player = new NimHumanPlayer(playerData[0], playerData[1], playerData[2]);
                players.add(player);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // catches exception in case of fewer arguments
            System.out.println("Incorrect number of arguments supplied to command.");
        }
        System.out.println();
    }
    
    /**
     * removePlayer method removes player from players list if username is provided
     *  if username is not provided empty the players list after user's confirmation
     * @param username is the player's username to be removed
     */
    public void removePlayer(String username) {
        String removeAllFlag; // flag to confirm if all players are to be removed
        NimPlayer player;
        if (username.equals("")) {
            System.out.println("Are you sure you want to remove all players? (y/n)");
            removeAllFlag = KEYIN.nextLine();
            if (removeAllFlag.equalsIgnoreCase("y")) {
                players.clear();
            }
            System.out.println();
        } else {
            username = username.split(",")[0];
            player = findPlayer(username); // to test if the player exists
            if(null == player) {
                System.out.println("The player does not exist.");
            } else {
                players.remove(player);
            }
            System.out.println();
        }
    }
    
    /**
     * editPlayer method edits player details (family name, given name)
     * @param playerString  stores edited details of the player
     */
    public void editPlayer(String playerString) {
        String playerData[] = playerString.split(",");
        try {
            NimPlayer player = findPlayer(playerData[0]); // to test if the player exists
            if(null == player) {
                System.out.println("The player does not exist.");
            } else {
                player.setGivenName(playerData[2]);
                player.setFamilyName(playerData[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Incorrect number of arguments supplied to command.");
        }
        System.out.println();
    }
    
    /**
     *  resetStats method resets player statistics (games played, games won) to 0
     *  if username is not provided reset statistics of all the players after user's confirmation
     *  @param username is the player's username whose statistics are to be reset.
     */
    public void resetStats(String username) {
        String resetAllFlag; // flag to confirm if all players stats are to be reset
        NimPlayer player;
        if (username.equals("")) {
            System.out.println("Are you sure you want to reset all player statistics? (y/n)");
            resetAllFlag = KEYIN.nextLine();
            if (resetAllFlag.equalsIgnoreCase("y")) {
                for(int i = 0; i < players.size(); i++) {
                    players.get(i).resetStats();
                }
            }
            System.out.println();
        } else {
            username = username.split(",")[0];
            player = findPlayer(username); // to test if the player exists
            if(null == player) {
                System.out.println("The player does not exist.");
            } else {
                player.resetStats();
            }
            System.out.println();
        }
    }
    
    /**
     * displayPlayer method displays player's details
     * if username is not provided then display details of all the players
     * @param username is the player's username whose details are to be displayed
     */
    public void displayPlayer(String username) {
        NimPlayer player;
        if (username.equals("")) {
            for(int i = 0; i < players.size(); i++) {
                player = players.get(i);
                System.out.printf("%s,%s,%s,%d games,%d wins\n", player.getUsername(), 
                            player.getGivenName(), player.getFamilyName(), player.getGameCount(),
                            player.getGameWonCount());
            }
        } else {
            username = username.split(",")[0];
            player = findPlayer(username); // to test if the player exists
            if(null == player) {
                System.out.println("The player does not exist.");
            } else {
                System.out.printf("%s,%s,%s,%d games,%d wins\n", player.getUsername(), 
                            player.getGivenName(), player.getFamilyName(), player.getGameCount(),
                            player.getGameWonCount());
            }
        }
        System.out.println();
    }

    /**
     * displayRangkings method displays player's in order of their rankings (win percentage)
     *                  in case of tie in win percentage the list is sorted by username
     * @param sortDescFlag determines if the top 10 players are to be shown or bottom 10
     *          "asc" -> bottom 10 players to be shown from low scorer to high
     *          "desc" -> top 10 players to be shown from high scorer to low
     */
    public void displayRangkings(Boolean sortDescFlag) {
        NimPlayer player;
        int length = players.size();
        String gameWinPercentage;
        if(players.size() > 10) {
            length = 10;
        }
        sortPlayers(sortDescFlag);
        for(int i = 0; i < length ; i++) {
            player = players.get(i);
            gameWinPercentage = Math.round(player.getWinPercent()) + "%";
            System.out.printf("%-4s | %02d games | %s %s\n", gameWinPercentage, player.getGameCount(), 
                        player.getGivenName(), player.getFamilyName());
        }
        System.out.println();
    }
}
