package client;

import javafx.scene.canvas.Canvas;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    protected Socket clientSocket = null;
    protected PrintWriter networkOut = null;
    protected BufferedReader networkIn = null;

    String SERVER_ADDRESS = "localhost";
    int SERVER_PORT = 9000;

    private boolean connected = false;
    private ConnectionThread thread = null;

    private Canvas canvas = null;

    public Client(String SERVER_ADDRESS, int SERVER_PORT) {
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.SERVER_PORT = SERVER_PORT;

        connectToServer(SERVER_ADDRESS, SERVER_PORT);
        this.thread = new ConnectionThread(this);
        this.thread.start();
        if(clientSocket.isConnected()){
            connected = true;
        }
    }

    public boolean isConnected(){
        return connected;
    }

    public Socket connectToServer(String SERVER_ADDRESS, int SERVER_PORT){
        try {
            this.clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        }catch(UnknownHostException e){
            System.err.println("Error: Unknown Host");
        } catch (IOException e) {
            System.err.println("IOException while connecting to server");
        }
        try{
            this.networkIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.networkOut = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("IOException while opening network streams");
        }

        this.networkOut.println("UID " + Player.getName());

        return clientSocket;
    }

    public void sendMessageToServer() {
            this.networkOut.println(Player.getName());
            this.networkOut.flush();
    }

    public void sendCoordinateToServer() {
            this.networkOut.println(Player.getPlayerX());
            this.networkOut.println(Player.getPlayerY());
            this.networkOut.flush();
    }

    /**
     * Sends coordinates of current stroke to server as CSV
     */
    public void sendCoords() {
        this.networkOut.printf("DRAW " + Player.getPlayerX() + "," + Player.getPlayerY() + "\n");
    }

    public void cleanup() {
        try {
            networkOut.close();
            networkIn.close();
            clientSocket.close();
        }catch(IOException e){
            System.err.println("Could not safely close socket");
        }
    }

    public void setCanvas(Canvas c){
        this.canvas = c;
    }

    public Canvas getCanvas(){
        return this.canvas;
    }
}
