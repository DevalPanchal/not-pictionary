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

    String SERVER_ADDRESS = "localhost";
    int SERVER_PORT = 9000;

    PrintWriter networkOut = null;
    BufferedReader networkIn = null;

    public Lobby() {}

    public void initialize() {
        playButton.setOnAction((e) -> {
            String name = playerName.getText();
            if (!name.equals("")) {
                Player.setName(name);
                Client client = new Client(SERVER_ADDRESS, SERVER_PORT);

                System.out.println("Created client object");
                playGame(client);
            } else {
                System.out.println("Please enter a name.");
            }
        });
    }

    public void playGame(Client client) {
        Stage currentStage = Main.getPrimaryStage();
        currentStage.hide();
        try {
            boolean connected;

            if((connected = client.isConnected())) {
                Stage mainGameStage = new Stage();
                Main.setPrimaryStage(mainGameStage);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
                Parent root = loader.load();
                Controller mainController = loader.getController();
                mainController.setClient(client);

                mainGameStage.setScene(new Scene(root, 1200, 800));
                mainGameStage.getIcons().add(new Image("client/public/icon.jpg"));
                mainGameStage.setTitle("Not Pictionary");
                mainGameStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
