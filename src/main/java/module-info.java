module com.echecs.s201developpementapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    requires org.mockito;
    requires org.junit.jupiter.api;

    opens com.echecs.main to javafx.fxml;
    exports com.echecs.main;
}