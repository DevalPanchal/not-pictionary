package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main server class. Handles the flow of the program.
 *
 * Connects to MAX_PLAYERS before beginning a game
 */
public class PictionaryServer {
    //Connection information
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;
    protected final List<Player> players = new ArrayList<>();

    //Constructor (Also insertion point)
    public PictionaryServer(){
        try{

            //Start connection thread
            Runnable runnable = new ConnectClientThread(MAX_PLAYERS, players);
            Thread connect = new Thread(runnable);
            connect.start();

            //Wait until there are enough players
            synchronized (players){
                while(players.size() < MIN_PLAYERS){
                    players.wait();
                }
            }

            //Game time
            System.out.println("Enough players for a game");
            GameLogic game = new GameLogic(players);
            ArrayList<String> newMsgs = new ArrayList<>();

            Player drawer = null;
            boolean newRound = false;

            //Main game loop
            while(true){
                //pick the next player
                drawer = game.chooseDrawer();

                // Set random word
                game.setRandomWord();
                System.out.println("THE CURRENT WORD = " + game.getCurrentWord());
                drawer.setWord(game.getCurrentWord());
                drawer.sendCurrentWord();

                // go through each player
                for(Player player : players){
                    //tell the players which role they have,
                    player.sendRole();
                }
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        PictionaryServer server = new PictionaryServer();
    }
}
