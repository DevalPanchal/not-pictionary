package client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CheckBox;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;
import java.net.Socket;

public class Controller {
    @FXML private TextField chatInput;
    @FXML private ListView<String> chatMenu;
    @FXML private Button addBtn;

    @FXML private Canvas mainCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private CheckBox eraser;
    @FXML private TextField brushSize;
    @FXML private Button clearCanvas;

    @FXML private GraphicsContext gc;

    public void initialize() {
        gc = mainCanvas.getGraphicsContext2D();

        addBtn.setOnAction(actionEvent -> addChat());

        mainCanvas.setOnMouseDragged(e -> {
            String brush = brushSize.getText();
            double size = Double.parseDouble(brush);
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


}

