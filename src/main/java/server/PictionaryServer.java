package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PictionaryServer {
    private static final int SERVER_PORT = 9000;
    private static final int MAX_PLAYERS = 4;

    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected Player[] players = null;
    protected int numPlayers = 0;

    public PictionaryServer(){
        try{
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("SERVER IS RUNNING...");
            System.out.println("LISTENING ON PORT " + SERVER_PORT);

            players = new Player[MAX_PLAYERS];

            //Listen for incoming connections
            while(true){
                clientSocket = serverSocket.accept();

                System.out.println("CONNECTED PLAYERS " + numPlayers);
                players[numPlayers] = new Player(clientSocket);
                players[numPlayers].pictionaryThread.start();
                numPlayers++;

                //Check for disconnected players and remove them from the list
                for(int i=0; i<players.length; i++){
                    if(players[i] != null && !players[i].pictionaryThread.isAlive()){
                        players[i].pictionaryThread.join();
                        for(int j=i; j<players.length-1; j++){
                            players[j] = players[j+1];
                        }
                        i=0;
                        numPlayers--;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
