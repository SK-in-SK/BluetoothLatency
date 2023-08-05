package com.musicbox.bluetoothlatency;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to hold DataEntry objects for use in the table
 */
public class DataSet {


    protected final List<DataEntry> dataSet;
    private final int setSize = 100;

    public DataSet(){
        dataSet = new ArrayList<>();
    }

    protected int getDataSetSize(){
        return setSize;
    }

    protected void recordData(Long outputTime, Long receivedTime, Long currentTime, Long difference){
        DataEntry newEntry = new DataEntry(outputTime, receivedTime, currentTime, difference);
        this.dataSet.add(newEntry);
    }

    protected List<DataEntry> getDataSet(){
        return dataSet;
    }

    /**
     * Calculates and returns the mean and standard deviation
     * @return an array of mean and std deviation
     */
    public double[] getMeanDeviation(){
        long mean = 0;
        long stdDeviation= 0;

        for (DataEntry entry : this.getDataSet()){
            mean += entry.getDifference();
        }
        mean /= this.getDataSetSize();
        for (DataEntry entry: this.getDataSet()){
            stdDeviation += Math.pow(entry.getDifference() - mean, 2);
        }
        stdDeviation /= this.getDataSetSize();
        return new double[] {mean, Math.sqrt(stdDeviation)};

    }

}
