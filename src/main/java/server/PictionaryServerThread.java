package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class PictionaryServerThread extends Thread {
    //Networking
    protected PrintWriter out = null;
    protected BufferedReader in = null;

    //Function
    Player player = null;

    //Constructor
    public PictionaryServerThread(PrintWriter out, BufferedReader in, Player player){
        super();
        this.in = in;
        this.out = out;
        this.player = player;
    }

    //insertion point
    @Override
    public void run(){
        //send confirmation of connection
        out.println("CONNECTED 200 OK");

        //Process commands until the client terminates the connection
        boolean exit = false;
        while(!exit){
            exit = processCommand();
        }
    }

    /**
     * Method to break the request from the client into command and arguments
     * @return status of thread - true for exit, false for keep alive
     */
    private boolean processCommand() {
        String message = null;
        try{
            message = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        // break the message into command and arguments
        StringTokenizer st = new StringTokenizer(message);
        String command = st.nextToken();
        String args = null;
        if(st.hasMoreTokens()){
            args = message.substring(command.length()+1, message.length());
        }
        return processCommand(command, args);
    }

    /**
     * Method to process the commands sent from the client
     * Current full list of commands:
     *      EXIT - closes the connection with the client and ends the thread
     *
     * TODO: Implement commands for core functionality
     *
     * @param command Command issued from the client
     * @param args Additional arguments
     * @return Thread status - true for exit, false for keep alive
     */
    private boolean processCommand(String command, String args) {
        // Tentative command required for functionality

        // Get the username from the player
        if(command.equalsIgnoreCase(("UID"))){
            synchronized (player.getUsername()) {
                player.setUsername(args);
            }
            return false;
        }
        //Ensure the user has a username first
        else if(player.getUsername() != null){
            out.println("401 PREREQUISITE UID REQUEST NOT RECEIVED. DISCONNECTING");
            return true;
        }
        // Receive drawing points from the user
        else if(command.equalsIgnoreCase("DRAW")){
            //add the new coordinate to the coordinate queue
            //TODO: Add some verification that the coordinate is valid
            try {
                synchronized(player.coordinates) {
                    player.coordinates.put(args);
                }
            }catch(InterruptedException e){
                System.err.println("Could not add coordinate to player's coordinate queue");
            }
            return false;

        }
        // Receive a message from the chat from the player
        else if(command.equalsIgnoreCase("MSG")){
            //add the new message to the guess queue I guess?
            try{
                synchronized (player.guesses){
                    player.guesses.put(args);
                }
            }catch(InterruptedException e){
                System.err.println("Could not add message to guess queue");
            }
            return false;
        }
        // Get the command to terminate from the player
        else if(command.equalsIgnoreCase("EXIT")) {
            return true;
        }else{
            out.println("400 REQUEST NOT UNDERSTOOD");
            return false;
        }
    }
}