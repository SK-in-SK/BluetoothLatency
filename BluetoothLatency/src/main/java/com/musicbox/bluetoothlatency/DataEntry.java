package com.musicbox.bluetoothlatency;

/**
 * Class used in the output table
 */
public class DataEntry {
    private final Long returnedValue;
    private final Long timeAtOut;
    private final Long timeAtReturn;
    private final Long difference;


    public DataEntry(Long timeAtOut, Long returnedValue, Long timeAtReturn, Long difference){
        this.timeAtOut = timeAtOut;
        this.returnedValue = returnedValue;
        this.timeAtReturn = timeAtReturn;
        this.difference = difference;
    }
    public Long getTimeAtOut(){ return this.timeAtOut; }
    public Long getReturnedValue() { return this.returnedValue; }
    public Long getTimeAtReturn() { return  this.timeAtReturn; }
    public Long getDifference() { return this.difference; }
}
