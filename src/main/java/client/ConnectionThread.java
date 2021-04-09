package client;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
            GraphicsContext gc = client.getCanvas().getGraphicsContext2D();

            //parse the location and settings of coordinate
            String[] items = args.split(" ");
            double x = Double.parseDouble(items[2].split(",")[0]);
            double y = Double.parseDouble(items[2].split(",")[1]);
            double size = Double.parseDouble(items[0]);
            Color color = Color.valueOf(items[1]);
            System.out.println(color);

            //draw the point
            gc.setFill(color);
            gc.fillOval(x,y,size,size);
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
