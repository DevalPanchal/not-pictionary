package client;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {
    @FXML private VBox playerView;
    @FXML private TextField chatInput;
    @FXML private ListView<String> chatMenu;
    @FXML private ListView<String> playerMenu;
    @FXML private javafx.scene.control.Button addBtn;
    @FXML private Canvas mainCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private CheckBox eraser;
    @FXML private TextField brushSize;
    @FXML private Button clearCanvas;
    @FXML private VBox chatView;
    @FXML private Spinner<Integer> BrushSize;
    @FXML private GraphicsContext gc;
    @FXML private Label wordLabel;

    //Current settings
    Client client = null;
    String background = "#f7f7f7";

    //Insertion point
    public void initialize() {
        gc = mainCanvas.getGraphicsContext2D();

        addBtn.setOnAction(actionEvent2 -> autoScrollChat(chatMenu));

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
        wordLabel.setText(Player.getWord());
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
     * + (added by Justin) sends messages to server
     * @param listView
     */
    public void autoScrollChat(ListView<String> listView) {
        String message = chatInput.getText();
        if (!message.equals("")) {
            ObservableList<String> items = listView.getItems();
            listView.scrollTo(items.size());
            resetTextField(chatInput);
            client.sendMessageToServer(message);
        }
    }

    public void setClient(Client client){
        synchronized (client) {
            this.client = client;
            this.client.setCanvas(mainCanvas);
            this.client.setWordLabel(wordLabel);
            this.client.setItems(chatMenu.getItems());
            this.client.setPlayerView(this.playerView);
            this.client.notify();
        }
    }
}

