package com.musicbox.bluetoothlatency;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;



public class BTLatencyController implements Initializable {


    @FXML
    public ListView discoveredListView;
    public VBox mainPane;
    public Label textBox;
    private ObservableList<String> discoveredNames = FXCollections.observableArrayList();


    /**
     * Get the services of the selected device
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    protected void onProcessDeviceClick() throws IOException, InterruptedException {


        ObservableList selectedIndices = discoveredListView.getSelectionModel().getSelectedIndices();
        if (selectedIndices.size() > 0){
            BTLatencyApp.selectedDevice = BTLatencyApp.devicesDiscovered.get((Integer) selectedIndices.get(0));
            ServicesSearch.main(null);
            StreamConnection streamConnection;
            try {
                recordDataStream((StreamConnection) Connector.open(BTLatencyApp.selectedDeviceURL));

            } catch (NullPointerException e){
                textBox.setText("No services found for selected device. Please check power.");
            }


        } else {
            textBox.setText("No device selected. Please select device and press select.");
        }




    }

    /**
     * Record the interaction with the selected device and output it to a table
     * @param sc the device connection stream
     * @throws IOException
     */
    private void recordDataStream(StreamConnection sc) throws IOException {

        OutputStream os = sc.openOutputStream();
        InputStream is = sc.openInputStream();

        byte[] outputData;
        byte[] buffer = ByteBuffer.allocate(Long.SIZE).array();
        long currentTime;
        long difference;


        BTLatencyApp.dataSet = new DataSet();



        for (int i = 0; i < BTLatencyApp.dataSet.getDataSetSize(); i++){


            outputData = ByteBuffer.allocate(Long.SIZE).putLong(System.currentTimeMillis()).array();

            os.write(outputData);

            is.read(buffer);
            while (ByteBuffer.wrap(buffer).getLong() == 0) {
                is.read(buffer);
            }
            currentTime = System.currentTimeMillis();
            difference =  currentTime - ByteBuffer.wrap(buffer).getLong();
            BTLatencyApp.dataSet.recordData(ByteBuffer.wrap(outputData).getLong(), ByteBuffer.wrap(buffer).getLong(), System.currentTimeMillis(), difference);


        }

        os.close();
        is.close();
        sc.close();

        BTLatencyApp.changeScene("resultTable.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

        discoveredListView.setItems(discoveredNames);
        discoveredNames.addAll(BTLatencyApp.devicesDiscoveredNames);
    }





}