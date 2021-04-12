package server;

import java.util.*;

/**
 * Main server class. Handles the flow of the program.
 *
 * Connects to MIN_PLAYERS before beginning a game
 */
public class PictionaryServer {
    //Game information
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

            Player drawer;
            boolean newRound;

            //Main game loop
            while(true){
                newRound = false;

                //pick the next player
                drawer = game.chooseDrawer();

                // Set random word and send it to the drawer
                game.setRandomWord();
                System.out.println("THE CURRENT WORD = " + game.getCurrentWord());
                String word = game.getCurrentWord();
                drawer.setWord(word);
                drawer.sendCurrentWord();

                //inform each player of their role and the length of the word
                for(Player player : players){
                    //tell the players which role they have,
                    player.sendRole();

                    // show the censored version of the word
                    if (!player.getDrawer()){
                        player.sendCensoredWord();
                    }
                }

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
                        for(Player player : players){
                            String newMsg;
                            while(!player.guesses.isEmpty()){
                                newMsg = player.guesses.take();
                                newMsgs.add(player.getUsername() + ": " + newMsg);

                                if(!player.getDrawer()) {
                                    //check if the word was guessed
                                    newRound = game.isCorrectWord(newMsg);

                                    //Move onto the next round
                                    if (newRound) {
                                        newMsgs.add(player.getUsername() + " guessed the correct word!");
                                        newMsgs.add("Time for a new round");
                                        game.incRound();
                                    }
                                }
                            }

                            //Send all the guessers the drawing coordinates
                            if (!player.getDrawer()) {
                                player.sendCoords(newCoords);
                            }
                        }

                        //send all the new messages to the players
                        for (Player player : players) {
                            player.sendMsg(newMsgs);

                            //clear the screen if there is a new round
                            if(newRound){
                                player.sendClear();
                            }
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
        new PictionaryServer();
    }
}
