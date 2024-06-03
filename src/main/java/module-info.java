module com.echecs.s201developpementapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.echecs.s201developpementapplication to javafx.fxml;
    exports com.echecs.s201developpementapplication;
}