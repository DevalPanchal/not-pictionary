package server;

import java.net.Socket;

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
    private boolean drawer;

    // Connection information
    public PictionaryServerThread pictionaryThread = null;
    public Socket socket = null;

    //Constructors
    public Player(Socket socket){
        this.pictionaryThread = new PictionaryServerThread(socket, this);
        this.socket = socket;
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
}
