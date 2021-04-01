package client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    @FXML private Spinner<Integer> BrushSize;

    @FXML private GraphicsContext gc;

    public void initialize() {
        gc = mainCanvas.getGraphicsContext2D();
        //playerMenu.getItems().add("Players");
        for (int i = 1; i <= 4; i++) {
            addNewPlayer(i);
        }
        addBtn.setOnAction(actionEvent -> addChat());

        mainCanvas.setOnMouseDragged(e -> {
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
                gc.fillRect(x, y, size, size);
            }
        });
        clearCanvas.setOnAction(actionEvent -> resetCanvas(gc));
    }

    public void addChat() {
        String message = chatInput.getText();
        if (!message.equals("")) {
            chatMenu.getItems().add(message);
        }
        resetTextField(chatInput);
    }

    public void resetTextField(TextField textField) {
        textField.setText("");
    }

    public void resetCanvas(GraphicsContext g) {
        g.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    public void addNewPlayer(int i) {
        Label label = new Label("PLAYER " + i);
        HBox playerNode = new HBox(label);
        playerNode.getStyleClass().add("testCell");
        playerView.getChildren().add(playerNode);
    }

}

