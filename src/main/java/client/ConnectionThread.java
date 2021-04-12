package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ConnectionThread extends Thread {
    private BufferedReader in = null;
    private Client client = null;


    //Constructor
    ConnectionThread(Client client){
        super();
        this.in = client.networkIn;
        this.client = client;
    }

    //insertion point
    @Override
    public void run(){
        boolean exit = false;
        while(!exit && !Thread.currentThread().isInterrupted()){
            exit = processCommand();
        }

        //disconnect the input and output streams
        //
        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
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
     * @param command Command issued from the client
     * @param args Additional arguments
     * @return Thread status - true for exit, false for keep alive
     */
    private boolean processCommand(String command, String args){
        //Receive coordinates to draw from the server
        if(command.equalsIgnoreCase("COORD")){
            GraphicsContext gc = client.getCanvas().getGraphicsContext2D();

            //parse the attributes of the point
            String[] items = args.split(" ");
            double x = Double.parseDouble(items[2].split(",")[0]);
            double y = Double.parseDouble(items[2].split(",")[1]);
            double size = Double.parseDouble(items[0]);
            Color color = Color.valueOf(items[1]);

            //draw the point
            gc.setFill(color);
            gc.fillOval(x,y,size,size);
           return false;
        }

        //Save the role assigned by the server
        else if(command.equalsIgnoreCase("ROLE")) {
            if(args.equalsIgnoreCase("DRAWER")){
                Player.setDrawer(true);
            }else{
                Player.setDrawer(false);
            }
            return false;
        }

        //Receive playernames and update the Playername display
        else if(command.equalsIgnoreCase("PLAYERNAMES")){
            String[] names = args.split(" ");

            System.out.println(names);
            synchronized (client) {
                if (client.getPlayerView() == null) {
                    try {
                        client.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(()->{
                    client.getPlayerView().getChildren().clear();
                    Label title = new Label("PLAYERS");
                    HBox TitleNode = new HBox(title);
                    TitleNode.getStyleClass().add("testCellTitle");
                    TitleNode.setAlignment(Pos.CENTER);
                    client.getPlayerView().getChildren().add(TitleNode);

                    for (String name : names) {
                        Label label = new Label(name);
                        HBox PlayerNode = new HBox(label);
                        PlayerNode.getStyleClass().add("testCell");
                        client.getPlayerView().getChildren().add(PlayerNode);
                    }
                });
            }
            return false;
        }

        //Receive message assigned by the server
        else if(command.equalsIgnoreCase("MSG")){
            Platform.runLater(()->{
                if(!args.equals("")) {
                    System.out.println(args);
                    client.getItems().add(args);
                }
            });
            return false;
        }

        //Clear the current canvas
        else if(command.equalsIgnoreCase("CLEAR")){
            GraphicsContext gc = client.getCanvas().getGraphicsContext2D();
            gc.clearRect(0,0,client.getCanvas().getWidth(), client.getCanvas().getHeight());
            System.out.println("Clearing...");
            return false;
        }

        // Receive the current word and update the client
        else if(command.equalsIgnoreCase("WORD")) {
            Platform.runLater(() -> client.getWordLabel().setText(args));
            return false;
        }

        else if (command.equalsIgnoreCase("CENSORED")){
            Platform.runLater(() -> {
                client.getWordLabel().setText(args);
            });
            return false;
        }

        //Exit the game
        else if(command.equalsIgnoreCase("EXIT")) {
            return true;
        }

        //None of the above
        else {
            System.out.println("Could not understand request " + command + " " + args);
            return false;
        }
    }
}
