package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main server class. Handles the flow of the program.
 *
 * Connects to MAX_PLAYERS before beginning a game
 */
public class PictionaryServer {
    //Connection information
    private static final int SERVER_PORT = 9000;
    private static final int MAX_PLAYERS = 2;
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected Player[] players = null;
    protected int numPlayers = 0;

    protected ArrayList<Player[]> playerList = new ArrayList<Player[]>();

    //Constructor (Also insertion point)
    public PictionaryServer(){
        try{
            //Start the server
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("SERVER IS RUNNING...");
            System.out.println("LISTENING ON PORT " + SERVER_PORT);

            players = new Player[MAX_PLAYERS];

            //Listen for incoming connections until the max number of players is reached
            while(numPlayers != MAX_PLAYERS){
                clientSocket = serverSocket.accept();

                // Create a new Player object
                players[numPlayers] = new Player(clientSocket);
                players[numPlayers].pictionaryThread.start();
                numPlayers++;

                //Check for disconnected players and remove them from the list
                for(int i=0; i<players.length; i++){
                    if(players[i] != null && !players[i].pictionaryThread.isAlive()){
                        players[i].pictionaryThread.join();
                        for(int j=i; j<players.length-1; j++){
                            players[j] = players[j+1];
                        }
                        i=0;
                        numPlayers--;
                    }
                }

                System.out.println("CONNECTED PLAYERS " + numPlayers);
            }

            //Game time
            System.out.println("Enough players for a game");
            GameLogic game = new GameLogic(Arrays.asList(players));
            ArrayList<String> newMsgs = new ArrayList<>();
            ArrayList<String> nameOfPlayer = new ArrayList<>();
//            playerList.forEach(player -> {});

            Player drawer = null;
            boolean newRound = false;

            //Main game loop
            while(true){
                //pick the next player
                drawer = game.chooseDrawer();

                // go through each player
                for(Player player : players) {
                    //tell the players which role they have,
                    player.sendRole();
                    // tell player name

//                    names = player.getUsername();
//                    player.sendPlayerNames(names);
                    nameOfPlayer.add(player.getUsername());
//                    player.sendPlayerName(playerList);
                }

//                Player.sendPlayerName(playerList);
                //System.out.println("players in the game: " + playerList);

                //game loop for each round
                while(!newRound) {
                    //clear the screen if instructed
                    if(drawer.isClear()){
                        for(Player player : players){
                            player.sendClear();
                        }
                        drawer.setClear(false);
                    }else {
                        //get the drawing coordinates from the drawer
                        ArrayList<String> newCoords = new ArrayList<>();
                        drawer.coordinates.drainTo(newCoords);

                        //Get the new messages sent by the players
                        //TODO Update newRound somewhere in here with the game.isCorrectWord() function
                        for(Player player : players){
                            String newMsg;
                            while(!player.guesses.isEmpty()){
                                newMsg = player.guesses.take();
                                newMsgs.add(player.getUsername() + ": " + newMsg);
                            }
                            player.sendPlayerName(nameOfPlayer);
                            //Send all the guessers the drawing coordinates
                            if (!player.getDrawer()) {
                                player.sendCoords(newCoords);
                            }
                        }

                        //
                        for (Player player : players) {
                            player.sendMsg(newMsgs);
                        }
                        //Clear the new messages for the next time around
                        newMsgs.clear();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        PictionaryServer server = new PictionaryServer();
    }
}
