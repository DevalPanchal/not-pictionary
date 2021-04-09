package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Lobby {

    @FXML private TextField playerName;
    @FXML private Button playButton;

    //Networking
    Socket socket = null;

    PrintWriter networkOut = null;
    BufferedReader networkIn = null;

    public Lobby() {}

    public void initialize() {
        playButton.setOnAction((e) -> {
            String name = playerName.getText();
            if (!name.equals("")) {
                Player.setName(name);
                //Client client = new Client();
                //client.sendMessageToServer();
                playGame();
            } else {
                System.out.println("Please enter a name.");
            }
        });
    }

    public void playGame() {
        Stage currentStage = Main.getPrimaryStage();
        currentStage.hide();
        try {
            Stage mainGameStage = new Stage();
            Main.setPrimaryStage(mainGameStage);
            Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));
            mainGameStage.setScene(new Scene(root, 1200, 800));
            mainGameStage.getIcons().add(new Image("client/public/icon.jpg"));
            mainGameStage.setTitle("Not Pictionary");
            mainGameStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean connectToServer(String SERVER_ADDRESS, int SERVER_PORT){
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        }catch(UnknownHostException e){
            System.err.println("Error: Unknown Host");
            return false;
        } catch (IOException e) {
            System.err.println("IOException while connecting to server");
            return false;
        }
        try{
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            networkOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("IOException while opening network streams");
            return false;
        }

        return true;
    }
}
