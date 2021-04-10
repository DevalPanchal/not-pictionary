package client;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Controller {
    @FXML private TextField chatInput;
    @FXML private ListView<String> chatMenu;
    @FXML private ListView<String> playerMenu;
    @FXML private Button addBtn;

    @FXML private Canvas mainCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private CheckBox eraser;
    @FXML private TextField brushSize;
    @FXML private Button clearCanvas;
    @FXML private VBox playerView;
    @FXML private VBox chatView;

    @FXML private Spinner<Integer> BrushSize;

    @FXML private GraphicsContext gc;

    //Current settings
    Client client = null;
    String background = "#f7f7f7";

    // Arraylist of players
    ArrayList<String> players;

    //Insertion point
    public void initialize() {
        gc = mainCanvas.getGraphicsContext2D();

        addPlayer(Player.getName());

        addBtn.setOnAction(actionEvent -> autoScrollChat(chatMenu));

        mainCanvas.setOnMousePressed((e) -> {
            //Ensure the player is actually allowed to draw
            if(Player.isDrawer()) {
                int brushWeight = BrushSize.getValue();
//            String brush = brushSize.getText();
                double size = Double.parseDouble(String.valueOf(brushWeight));
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;

//            System.out.println(x + ", " + y);
                Player.setPlayerX(x);
                Player.setPlayerY(y);


                if (eraser.isSelected()) {
                    client.setDrawSettings(size, background);
                    gc.setFill(Color.valueOf(background));
                } else {
                    client.setDrawSettings(size, colorPicker.getValue().toString());
                    gc.setFill(colorPicker.getValue());
                    //gc.fillRoundRect(x, y, size, size, 10, 10);
                }
                gc.fillOval(x, y, size, size);
                client.sendCoords();
            }
        });

        mainCanvas.setOnMouseDragged((e) -> {
            //Ensure the player is actually allowed to draw
            if(Player.isDrawer()) {
                int brushWeight = BrushSize.getValue();
//            String brush = brushSize.getText();
                double size = Double.parseDouble(String.valueOf(brushWeight));
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;

                Player.setPlayerX(x);
                Player.setPlayerY(y);
                //System.out.printf("PlayerX: %f | PlayerY: %f\n",Player.getPlayerX(), Player.getPlayerY());

                if (eraser.isSelected()) {
                    gc.setFill(Color.valueOf("#F7F7F7"));
                } else {
                    gc.setFill(colorPicker.getValue());
                    //gc.fillRoundRect(x, y, size, size, 10, 10);
                }
                gc.fillOval(x, y, size, size);
                client.sendCoords();
            }
        });

        mainCanvas.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
            //Ensure the player is actually allowed to draw
            if(Player.isDrawer()) {
                int brushWeight = BrushSize.getValue();

                Circle circle = new Circle(brushWeight, null);
                circle.setStroke(colorPicker.getValue());
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);
                WritableImage image = circle.snapshot(sp, null);

                mainCanvas.setCursor(new ImageCursor(image, brushWeight, brushWeight));
            }
        });

        clearCanvas.setOnAction(actionEvent -> resetCanvas(gc));
    }

    /**
     * resets the textfield to empty and refocuses on it in the chat menu
     * @param textField
     */
    public void resetTextField(TextField textField) {
        textField.setText("");
        textField.requestFocus();
    }

    /**
     * resets the canvas to a blank slate
     * @param g
     */
    public void resetCanvas(GraphicsContext g) {
        if(Player.isDrawer()) {
            g.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
            client.sendClear();
        }
    }

    /**
     * auto scroll chat to bottom when overflow occurs in chat container
     * @param listView
     */
    public void autoScrollChat(ListView<String> listView) {
        String message = chatInput.getText();
        if (!message.equals("")) {
            ObservableList<String> items = listView.getItems();
            items.add(message);
            listView.scrollTo(items.size());
            resetTextField(chatInput);
        }
    }

    /**
     * add player name to the room [top-right pane]
     * @param
     */
    public void addPlayer(String name) {
        Label label = new Label(name);
        HBox PlayerNode = new HBox(label);
        PlayerNode.getStyleClass().add("testCell");
        playerView.getChildren().add(PlayerNode);
    }

    /**
     * Reads player names from the players textfile
     */
    public void readPlayerNames()  {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("players.txt"));
            players = new ArrayList<>();
            String line = reader.readLine();
            while(line != null) {
                players.add(line);
                line = reader.readLine();
            }
            reader.close();
            System.out.println(players);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
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

    public void setClient(Client client){
        this.client = client;
        this.client.setCanvas(mainCanvas);
    }

}

