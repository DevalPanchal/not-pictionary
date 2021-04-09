package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Lobby {

    @FXML private TextField playerName;
    @FXML private Button playButton;

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
}
