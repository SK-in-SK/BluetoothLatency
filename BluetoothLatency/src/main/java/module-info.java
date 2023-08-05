module com.musicbox.bluetoothlatency {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires bluecove;
    requires java.desktop;
    requires java.logging;

    opens com.musicbox.bluetoothlatency to javafx.fxml;
    exports com.musicbox.bluetoothlatency;
}