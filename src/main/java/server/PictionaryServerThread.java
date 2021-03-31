package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class PictionaryServerThread extends Thread {
    //Networking
    protected Socket socket = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;

    //Function
    Player player = null;

    //Constructor
    public PictionaryServerThread(Socket socket, Player player){
        super();
        this.socket = socket;
        this.player = player;

        try{
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //close the connection
        try{
            socket.close();
        } catch (IOException e) {
            System.err.println("Could not disconnect from client");
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
            player.setUsername(args);
            return false;
        }
        // Receive drawing points from the user
        else if(command.equalsIgnoreCase("DRAW")){
            // Need to know how the data is being sent before this can be
            return false;
        }
        // Receive a message from the chat from the player
        else if(command.equalsIgnoreCase("MSG")){
            return false;
        }
        // Get the command to terminate from the player
        else if(command.equalsIgnoreCase("EXIT")) {
            return true;
        }else{
            return false;
        }
    }
}
