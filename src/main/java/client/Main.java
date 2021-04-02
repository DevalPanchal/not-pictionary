package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getPrimaryStage () {
        return Main.primaryStage;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("lobby.fxml"));
        primaryStage.getIcons().add(new Image("client/public/icon.jpg"));
        primaryStage.setTitle("Not Pictionary");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
