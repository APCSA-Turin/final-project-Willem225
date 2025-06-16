module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http; 
    requires java.base;
    requires java.desktop;
    requires java.logging;
    requires java.sql;
    requires java.xml;
    opens com.example to javafx.fxml;
    exports com.example;
}
