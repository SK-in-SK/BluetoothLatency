package com.musicbox.bluetoothlatency;

import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.*;

/**
 * This searches the selected device for the associated services
 */
public class ServicesSearch {
    static final UUID OBEX_FILE_TRANSFER = new UUID(0x1106);



    public static final Vector/*<String>*/ serviceFound = new Vector();

    public static void main(String[] args) throws IOException, InterruptedException {

        serviceFound.clear();


        UUID serviceUUID = new UUID(0x1101);
        if ((args != null) && (args.length > 0)) {
            serviceUUID = new UUID(args[0], false);
        }

        final Object serviceSearchCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            }

            public void inquiryCompleted(int discType) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                for (int i = 0; i < servRecord.length; i++) {
                    String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    if (url == null) {
                        continue;
                    }
                    serviceFound.add(url);
                    DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                    if (serviceName != null) {
                        System.out.println("service " + serviceName.getValue() + " found " + url);
                        BTLatencyApp.selectedDeviceURL = url; //Must correct this takes the last found address

                    } else {
                        System.out.println("service found " + url);
                    }


                }


            }

            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("service search completed!");
                synchronized(serviceSearchCompletedEvent){
                    serviceSearchCompletedEvent.notifyAll();
                }
            }

        };

        UUID[] searchUuidSet = new UUID[] { serviceUUID };
        int[] attrIDs =  new int[] {
                0x0100 // Service name
        };


        RemoteDevice btDevice = BTLatencyApp.selectedDevice;

        synchronized(serviceSearchCompletedEvent) {
            System.out.println("search services on " + btDevice.getBluetoothAddress() + " " + btDevice.getFriendlyName(false));
            LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice, listener);
            serviceSearchCompletedEvent.wait();
        }


    }

}
