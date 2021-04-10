package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ConnectClientThread implements Runnable{
    private static final int SERVER_PORT = 9000;
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;

    private final int MAX_PLAYERS;
    private final List<Player> players;

    public ConnectClientThread(int MAX_PLAYERS, List<Player> players){
        this.MAX_PLAYERS = MAX_PLAYERS;
        this.players = players;
    }

    @Override
    public void run() {
        try {
            //Start the server
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("SERVER IS RUNNING...");
            System.out.println("LISTENING ON PORT " + SERVER_PORT);

            //Listen for incoming connections until the max number of players is reached
            while (players.size() <= MAX_PLAYERS) {
                clientSocket = serverSocket.accept();

                synchronized (players) {
                    // Create a new Player object
                    players.add(new Player(clientSocket));
                    players.get(players.size() - 1).pictionaryThread.start();
                    players.notifyAll();

                    //Check for disconnected players and remove them from the list
                    for (int i = 0; i < players.size(); i++) {
                        if (!players.get(i).pictionaryThread.isAlive()) {
                            players.get(i).pictionaryThread.join();
                            i = 0;
                        }
                    }
                    System.out.println("CONNECTED PLAYERS " + players.size());
                }
            }
        }catch(IOException | InterruptedException e){
            System.err.println("Could not create socket");
        }
    }
}
