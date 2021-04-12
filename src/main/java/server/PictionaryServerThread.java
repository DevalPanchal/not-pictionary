package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Thread meant to receive messages from the client over the network.
 * Is constantly listening for new commands.
 * Takes action when a valid command is sent
 */
public class PictionaryServerThread extends Thread {
    //Networking
    protected PrintWriter out;
    protected BufferedReader in;

    //Function
    final Player player;

    //Constructor
    public PictionaryServerThread(Player player){
        super();
        this.in = player.getNetworkReader();
        this.out = player.getNetworkWriter();
        this.player = player;
    }

    //insertion point
    @Override
    public void run(){
        //Process commands until connection is terminated
        boolean exit = false;
        while(!exit && player.getSocket().isConnected()){
            exit = processCommand();
        }
        System.out.println(player.getUsername() + " DISCONNECTED");
    }

    /**
     * Method to break the request from the client into command and arguments
     * @return status of thread - true for exit, false for keep alive
     */
    private boolean processCommand() {
        String message;
        try{
            //hangs here until new message is received
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
            args = message.substring(command.length()+1);
        }
        return processCommand(command, args);
    }

    /**
     * Method to process the commands sent from the client
     * Current full list of commands:
     *      EXIT - closes the connection with the client and ends the thread
     *
     * @param command Command issued from the client
     * @param args Additional arguments
     * @return Thread status - true for exit, false for keep alive
     */
    private boolean processCommand(String command, String args) {
        // Get the username from the player. Sets the username in the associated player object
        if(command.equalsIgnoreCase(("UID"))){
            player.setUsername(args);
            synchronized (player) {
                player.notifyAll();
            }
            return false;
        }
        //Ensure the user has a username. If they don't, requests are invalid and the server disconnects
        else if(player.getUsername() == null){
            out.println("401 PREREQUISITE UID REQUEST NOT RECEIVED. DISCONNECTING");
            System.out.println("NO UID");
            return true;
        }
        // Receive drawing points from the user, add them to the player's drawing queue
        else if(command.equalsIgnoreCase("DRAW")){
            //add the new coordinate to the coordinate queue
            try {
                player.coordinates.put(args);
            }catch(InterruptedException e) {
                System.err.println("Could not add coordinate to player's coordinate queue");
            }
            return false;
        }
        //Receive guesses and add them to the player's guess queue (Justin)
        else if (command.equalsIgnoreCase("MSG")){
            try{
                player.guesses.put(args);
            }catch(InterruptedException e){
                System.err.println("Could not add message to player's guess queue");
            }
            return false;
        }
        // Set the clear flag of the player to true.
        else if(command.equalsIgnoreCase("CLEAR")){
            synchronized (player) {
                player.setClear(true);
            }
            return false;
        }
        // Terminate the connection with the user
        else if(command.equalsIgnoreCase("EXIT")) {
            out.println("EXIT");
            return true;
        }
        // Request does not contain any of the previous commands. Have no idea what to do with it.
        else{
            out.println("400 REQUEST NOT UNDERSTOOD");
            return false;
        }
    }
}