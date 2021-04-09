package client;

import javafx.scene.canvas.GraphicsContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ConnectionThread extends Thread{
    private BufferedReader in = null;
    Client client = null;

    ConnectionThread(Client client){
        super();
        this.in = client.networkIn;
        this.client = client;
    }

    //insertion point
    @Override
    public void run(){
        boolean exit = false;
        while(!exit){
            exit = processCommand();
        }
        System.out.println("terminated thread");
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

        StringTokenizer st = new StringTokenizer(message);
        String command = st.nextToken();
        String args = null;
        if(st.hasMoreTokens()){
            args = message.substring(command.length()+1, message.length());
        }
        return processCommand(command,args);
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
    private boolean processCommand(String command, String args){
        if(command.equalsIgnoreCase("COORD")){
            System.out.println("Received coords " + args);
            GraphicsContext gc = client.getCanvas().getGraphicsContext2D();
            double x = Double.parseDouble(args.split(",")[0]);
            double y = Double.parseDouble(args.split(",")[1]);
            gc.fillOval(x,y,15,15);
           return false;
        }else if(command.equalsIgnoreCase("EXIT")){
            return true;
        }else if(command.equalsIgnoreCase("ROLE")) {
            if(args.equalsIgnoreCase("DRAWER")){
                Player.setDrawer(true);
            }else{
                Player.setDrawer(false);
            }

            return false;
        }
        else {
                return false;
        }
    }
}
