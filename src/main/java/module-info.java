module com.lab6.map_socialnetwork_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires junit;
    requires org.apache.pdfbox;

    opens com.lab6.map_socialnetwork_gui to javafx.fxml, javafx.base;
    opens com.lab6.map_socialnetwork_gui.domain to javafx.base;
    exports com.lab6.map_socialnetwork_gui;
    exports com.lab6.map_socialnetwork_gui.controllers;
    opens com.lab6.map_socialnetwork_gui.controllers to javafx.fxml;
}