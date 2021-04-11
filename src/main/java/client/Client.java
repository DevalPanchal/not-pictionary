package client;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    //Connection Information
    String SERVER_ADDRESS = "localhost";
    int SERVER_PORT = 9000;
    protected Socket clientSocket = null;
    protected PrintWriter networkOut = null;
    protected BufferedReader networkIn = null;
    private boolean connected = false;
    private ConnectionThread thread = null;

    //Game/client attributes
    private Canvas canvas = null;
    private String brushColor;
    private double brushWidth;
    private Label wordLabel;

    //Constructor
    public Client(String SERVER_ADDRESS, int SERVER_PORT) {
        //Set the server port and address
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.SERVER_PORT = SERVER_PORT;

        //Connect to server and start the connection thread
        connectToServer(SERVER_ADDRESS, SERVER_PORT);
        this.thread = new ConnectionThread(this);
        this.thread.start();
        if(clientSocket.isConnected()){
            connected = true;
        }
    }

    /**
     * Method to establish a connection with the server
     * @param SERVER_ADDRESS IP address of the server
     * @param SERVER_PORT Port the server is listening on
     * @return Socket that has just been established
     */
    public Socket connectToServer(String SERVER_ADDRESS, int SERVER_PORT){
        //Create the socket
        try {
            this.clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        }catch(UnknownHostException e){
            System.err.println("Error: Unknown Host");
        } catch (IOException e) {
            System.err.println("IOException while connecting to server");
        }

        //Begin the network Reader and Writer
        try{
            this.networkIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.networkOut = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("IOException while opening network streams");
        }

        //Send the user's name to the server
        this.networkOut.println("UID " + Player.getName());

        return clientSocket;
    }

    //Communication

    /**
     * Method to send new messages from chat to the server
     */
    public void sendMessageToServer() {
            this.networkOut.println(Player.getName());
            this.networkOut.flush();
    }

    /**
     * Deprecated? Have been using sendCoords for the same purpose
     */
    public void sendCoordinateToServer() {
            this.networkOut.println(Player.getPlayerX());
            this.networkOut.println(Player.getPlayerY());
            this.networkOut.flush();
    }

    /**
     * Sends coordinates of current stroke to server in the following format
     * DRAW [WIDTH] [COLOR] [X],[Y]
     *
     *  [WIDTH] - Weight of the brush
     *  [COLOR] - Color of the brush
     *  [X]     - x-coordinate
     *  [Y]     - y-coordinate
     */
    public void sendCoords() {
        //ensure the brush color is defined
        if(this.brushColor == null){
            this.brushColor = "0x000000ff";
        }

        this.networkOut.printf("DRAW " +
                this.brushWidth + " " +
                this.brushColor + " " +
                Player.getPlayerX() + "," +
                Player.getPlayerY() + "\n");
    }

    /**
     * Cleanly disconnects from the server, closes all the streams and sockets
     */
    public void disconnect() throws IOException {
        this.thread.interrupt();
        this.networkOut.println("EXIT");
        this.networkOut.close();
        this.networkIn.close();
        this.clientSocket.close();
    }

    /**
     * Sends a clear signal to the server
     */
    public void sendClear() {
        networkOut.println("CLEAR");
    }

    //Getters

    public synchronized boolean isConnected(){
        return connected;
    }

    public Canvas getCanvas(){
        return this.canvas;
    }

    public Label getWordLabel(){ return this.wordLabel; }

    //Setters

    public void setDrawSettings(double width, String color){
        this.brushWidth = width;
        this.brushColor = color;
    }

    public void setCanvas(Canvas c){
        this.canvas = c;
    }

    public synchronized void setConnected(boolean status){
        this.connected = status;
    }

    public void setWordLabel(Label label){
        this.wordLabel = label;
        System.out.println(label.getText());
    }
}
