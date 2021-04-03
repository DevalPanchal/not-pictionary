package client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    protected Socket clientSocket = null;
    protected PrintWriter output = null;

    public Client() { /* */ }

    public void sendMessageToServer() {
        try {
            clientSocket = new Socket("localhost", 8080);
            output = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));

            output.println(Player.getName());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
