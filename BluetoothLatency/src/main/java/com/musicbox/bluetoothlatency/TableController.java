package com.musicbox.bluetoothlatency;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.awt.Desktop;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class TableController implements Initializable  {
    public Button openButton;
    public Label textBox;
    public VBox mainPane;
    @FXML private TableView<DataEntry> tableView;
    @FXML private TableColumn<DataEntry,Long> timeAtOutCol;
    @FXML private TableColumn<DataEntry,Long> returnedValueCol;
    @FXML private TableColumn<DataEntry,Long> timeAtReturnCol;
    @FXML private TableColumn<DataEntry,Long> differenceCol;
    private final Desktop desktop = Desktop.getDesktop();
    private FileChooser fileChooser;
    private double mean;
    private double stD;


    /**
     * Create table
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Create columns
        timeAtOutCol.setCellValueFactory(new PropertyValueFactory<>("timeAtOut"));
        returnedValueCol.setCellValueFactory(new PropertyValueFactory<>("returnedValue"));
        timeAtReturnCol.setCellValueFactory(new PropertyValueFactory<>("timeAtReturn"));
        differenceCol.setCellValueFactory(new PropertyValueFactory<>("difference"));

        //add items to table
        tableView.setItems(getTableList());

        textBox.setText("Mean: "+mean+"\n"+"Standard Deviation: "+stD);

        fileChooser = new FileChooser();
        fileChooser.setInitialFileName("Data");
        fileChooser.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("All Files", "*.*"));


    }

    /**
     * Opens dialog for file save location
     * @param e event
     */
    @FXML
    protected void openButtonClick(ActionEvent e){
        final Stage[] stage = new Stage[1];
        Node node = (Node) e.getSource();
        stage[0] = (Stage) node.getScene().getWindow();
        configureFileChooser(fileChooser);
        File destinationFile = fileChooser.showSaveDialog(stage[0]);
        System.out.println(destinationFile.toString());
        if (destinationFile != null) {
            outputDataToFile(destinationFile);
        }
    }


    /**
     * Writes data to file
     * @param file output file
     */
    private void outputDataToFile(File file){
        PrintWriter writer;
        try{
            writer = new PrintWriter(file);
            for (DataEntry entry: BTLatencyApp.dataSet.getDataSet() ) {
                writer.println(entry.getTimeAtOut() + " " +
                        entry.getReturnedValue() + " " +
                        entry.getTimeAtReturn() + " " +
                entry.getDifference());
            };
            writer.close();
        } catch (FileNotFoundException f) {
            System.out.println("File not found");
        }

    }

    /**
     * @return The list based on the results in the table
     */
    private ObservableList<DataEntry> getTableList(){

        ObservableList<DataEntry> tableList = FXCollections.observableArrayList();
        tableList.addAll(BTLatencyApp.dataSet.getDataSet());
        getResults();

        return tableList;
    }

    /**
     * FileChooser configuration method
     * @param fileChooser javafx FileChooser
     */
    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Choose File Name");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }


    /**
     * Get the statistics from the data set for display
     */
    protected void getResults(){
        double [] result = BTLatencyApp.dataSet.getMeanDeviation();
        mean = result[0];
        stD = result[1];
    }

}
