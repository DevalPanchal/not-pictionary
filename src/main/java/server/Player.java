package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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

    // Connection information
    public PictionaryServerThread pictionaryThread = null;
    public Socket socket = null;

    private BufferedReader in = null;
    private PrintWriter out = null;

    //Communication queues
    BlockingQueue<String> guesses;      //holds a queue of guesses the player has made
    BlockingQueue<String> coordinates;  //holds a queue of coordinates to be sent to the client

    //Drawing settings
    private boolean erasing = false;
    private volatile boolean clear = false;

    //Constructors
    public Player(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.pictionaryThread = new PictionaryServerThread(out, in, this);
        this.socket = socket;

        this.guesses = new LinkedBlockingQueue<>();
        this.coordinates = new LinkedBlockingQueue<>();
    }

    //Communication functions

    /**
     * method to send the player coordinates that need to be drawn
     * @param newCoords new coordinates to be drawn.
     */
    public synchronized void sendCoords(ArrayList<String> newCoords) {
        for(String coord : newCoords){
            String msg = "COORD " + coord;
            out.println(msg);
        }
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

    public void sendClear(){
        String msg = "CLEAR";
        out.println(msg);
    }

    //setters
    public void setUsername(String username){
        this.username = username;
    }

    public void setDrawer(boolean drawer){
        this.drawer = drawer;
    }

    //getters
    public String getUsername(){
        return this.username;
    }

    public boolean getDrawer(){
        return this.drawer;
    }

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

    public synchronized boolean isClear() {
        return clear;
    }

    public synchronized void setClear(boolean clear) {
        this.clear = clear;
    }
}