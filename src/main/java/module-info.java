module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires org.junit.jupiter.api;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}