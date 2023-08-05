#include "driver/rtc_io.h"
#include "BluetoothSerial.h" 


#define Light 13 // LED 
BluetoothSerial BT; //Object for Bluetooth



void setup() {
  Serial.begin(115200);  
  BT.begin("ESP32"); // Bluetooth Name    
  pinMode(Light, OUTPUT);

}


void loop() {
if (BT.available())
  {
    BT.write(BT.read());
  }

}
