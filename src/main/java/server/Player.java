package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class to hold the relevant information for a player
 *
 * Keeps track of the Player's username, gamestatus (ie. if the player is drawing or not),
 * and connections for the drawing and chat
 *
 *   Should eventually include wrappers for the threads to allow the main class to easier control what's going on
 */
public class Player {
    //Player properties
    private String username;
    private boolean drawer = false;
    private String word;

    // Connection information
    public PictionaryServerThread pictionaryThread = null;
    private Socket socket = null;

    private BufferedReader in = null;
    private PrintWriter out = null;

    private static PrintWriter output = null;

    //Communication queues
    BlockingQueue<String> guesses;      //holds a queue of guesses the player has made
    BlockingQueue<String> coordinates;  //holds a queue of coordinates to be sent to the client

    //Drawing settings
    private volatile boolean clear = false;

    //Constructors
    public Player(Socket socket) throws IOException {
        //setup the output streams
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Testing /////////////////////////////////////////////////////////////////
        Player.output = new PrintWriter(socket.getOutputStream(), true);

        //start the listener thread
        this.pictionaryThread = new PictionaryServerThread(this);
        this.socket = socket;

        //initialize queues for guesses and coordinates
        this.guesses = new LinkedBlockingQueue<>();
        this.coordinates = new LinkedBlockingQueue<>();
    }

    //Communication functions

    /**
     * method to send the player coordinates that need to be drawn
     * @param newCoords new coordinates to be drawn.
     */
    public synchronized void sendCoords(ArrayList<String> newCoords) {
        for(String coord : newCoords) {
            String msg = "COORD " + coord;
            out.println(msg);
        }

    }

    public synchronized void sendPlayerNames(List<Player> newPlayers) {
        String msg = "PLAYERNAMES ";
        for (Player player: newPlayers) {
            msg += player.getUsername() + " ";
        }
        out.println(msg);
    }

    /**
     * Method to send player name
     */
    public void sendPlayerName(ArrayList<String> players) {
        String msg = "PLAYERNAMES " + players;
        out.println(msg);
        System.out.println("list of players: " + players);
//        for (String player: players) {
//            String msg = "PLAYERNAMES " + player;
//            output.println(msg);
//        }
    }

    public void sendPlayerNames(String name) {
        String msg = "PLAYERNAMES " + name;
        out.println(msg);
    }

    /**
     * method to send new messages to the client
     * @param newMsg List of new messages to be sent
     */
    public synchronized void sendMsg(ArrayList<String> newMsg){
        for(String msg : newMsg){
            out.println("MSG " + msg);
        }
    }

    /**
     * Method to send a command to the player to clear their screen
     */
    public void sendClear(){
        String msg = "CLEAR";
        out.println(msg);
    }

    /**
     * Method to notify the client which role they are to play
     */
    public void sendRole() {
        String msg = "ROLE ";
        if(this.getDrawer()){
            msg += "DRAWER";
        }else{
            msg += "GUESSER";
        }
        System.out.println(this.getUsername() + ": " + msg);
        out.println(msg);
    }

    public void sendCurrentWord(){
        String msg = "WORD ";
        if(this.getDrawer()){
            msg += word;
        }
        out.println(msg);
    }

    public void sendCensoredWord(String Word){
        String msg = "CENSORED ";
        msg += Word.replaceAll("[A-Za-z]", "*");
        out.println(msg);
    }

    //setters

    public void setUsername(String username){
        this.username = username;
    }

    public void setDrawer(boolean drawer){
        this.drawer = drawer;
    }

    public synchronized void setClear(boolean clear) {
        this.clear = clear;
    }

    public void setNetworkReader(BufferedReader in) {
        this.in = in;
    }

    public void setWord(String word){ this.word = word; }

    //getters

    public String getUsername(){
        return this.username;
    }

    public boolean getDrawer(){
        return this.drawer;
    }

    public synchronized boolean isClear() {
        return clear;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getNetworkReader() {
        return in;
    }

    public PrintWriter getNetworkWriter(){
        return out;
    }

    public String getWord(){ return word; }
}