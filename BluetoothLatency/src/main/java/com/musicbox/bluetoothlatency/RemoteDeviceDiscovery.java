package com.musicbox.bluetoothlatency;
import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.*;

/**
 * Minimal Device Discovery example.
 * from http://www.bluecove.org/bluecove/apidocs/overview-summary.html
 */

public class RemoteDeviceDiscovery {

    public static final Vector devicesDiscovered = new Vector();


    public static void main(String[] args) throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();

        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {

                devicesDiscovered.addElement(btDevice);

            }

            public void inquiryCompleted(int discType) {

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

                inquiryCompletedEvent.wait();

            }
        }
    }

}
