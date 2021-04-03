package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.*;
import java.net.Socket;

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


    public void initialize() {
        gc = mainCanvas.getGraphicsContext2D();

        System.out.println(Player.getName());
        addPlayer(Player.getName());

        addBtn.setOnAction(actionEvent -> autoScrollChat(chatMenu));

        mainCanvas.setOnMouseDragged((e) -> {
            int brushWeight = BrushSize.getValue();
//            String brush = brushSize.getText();
            double size = Double.parseDouble(String.valueOf(brushWeight));
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            System.out.println(x + ", " + y);

            if (eraser.isSelected()) {
                gc.clearRect(x, y, size, size);
            } else {
                gc.setFill(colorPicker.getValue());
                gc.fillOval(x, y, size, size);
                //gc.fillRoundRect(x, y, size, size, 10, 10);
            }
        });

        mainCanvas.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
            int brushWeight = BrushSize.getValue();

            Circle circle = new Circle(brushWeight, null);
            circle.setStroke(colorPicker.getValue());
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            WritableImage image = circle.snapshot(sp, null);

            mainCanvas.setCursor(new ImageCursor(image, brushWeight, brushWeight));
        });
        clearCanvas.setOnAction(actionEvent -> resetCanvas(gc));
    }

    public void resetTextField(TextField textField) {
        textField.setText("");
        textField.requestFocus();
    }

    public void resetCanvas(GraphicsContext g) {
        g.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    public void autoScrollChat(ListView<String> listView) {
        String message = chatInput.getText();
        if (!message.equals("")) {
            ObservableList<String> items = listView.getItems();
            items.add(message);
            listView.scrollTo(items.size());
            resetTextField(chatInput);
        }
    }

    public void addPlayer(String name) {
        Label label = new Label(name);
        HBox PlayerNode = new HBox(label);
        PlayerNode.getStyleClass().add("testCell");
        playerView.getChildren().add(PlayerNode);
    }

    public void addNewPlayer(int i) {
        Label label = new Label("PLAYER " + i);
        HBox playerNode = new HBox(label);
        playerNode.getStyleClass().add("testCell");
        playerView.getChildren().add(playerNode);
    }

}

