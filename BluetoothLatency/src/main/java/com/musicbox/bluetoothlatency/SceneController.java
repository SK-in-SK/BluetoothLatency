package com.musicbox.bluetoothlatency;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;



public class SceneController {

    @FXML public Label textBox;
    FXMLLoader fxmlLoader;

    @FXML public VBox mainPane;

    public SceneController(){
        this.fxmlLoader = new FXMLLoader(BTLatencyApp.class.getResource("selection.fxml"));

    }
    public SceneController(String name){
        this.fxmlLoader = new FXMLLoader(BTLatencyApp.class.getResource(name));
    }


}

