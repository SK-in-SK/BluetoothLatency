package com.musicbox.bluetoothlatency;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.bluetooth.*;
import java.io.IOException;
import java.util.*;


public class BTLatencyApp extends Application {
    public static final Vector<RemoteDevice> devicesDiscovered = new Vector<>();
    public static Stage currentStage;
    static List<String> devicesDiscoveredNames;
    static String selectedDeviceURL;
    static RemoteDevice selectedDevice;
    public static DataSet dataSet;
    public static SceneController sceneController;




    @Override
    public void start(Stage stage) throws IOException {

        sceneController = new SceneController("loading.fxml");
        currentStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "loading.fxml" ));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * This method changes the scene based on the provided fxml scene name
     * @param fxmlName next scene
     * @throws IOException if scene not found it will through an error
     */
    public static void changeScene(String fxmlName) throws IOException {
        sceneController = new SceneController(fxmlName);
        FXMLLoader loader = new FXMLLoader(BTLatencyApp.class.getResource(fxmlName));
        Parent root = loader.load();
        currentStage.setScene(new Scene(root));
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();
        devicesDiscoveredNames = new ArrayList<String>();
        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {
            /**
             * Add device to the deviceDiscovered List
             * @param btDevice found device
             * @param deviceClass not used, required for DiscoveryListener
             */
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass deviceClass) {
                devicesDiscovered.addElement(btDevice);

                try {
                  devicesDiscoveredNames.add(btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                    System.out.println("Can't find device");
                }
            }

            public void inquiryCompleted(int discType) {

                    Platform.runLater(() -> {
                        try {
                            changeScene("selection.fxml");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            }
        };

        synchronized(inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                launch();
                inquiryCompletedEvent.wait();

            }

        }

    }


}