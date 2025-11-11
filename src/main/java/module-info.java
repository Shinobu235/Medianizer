module medianizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;
    requires java.sql;

    opens com.moni.medianizer.app to javafx.graphics, javafx.fxml;
    opens com.moni.medianizer.app.view to javafx.graphics, javafx.fxml;
    opens com.moni.medianizer.app.controller to javafx.graphics, javafx.fxml;
    opens com.moni.medianizer.app.model to javafx.base;
}
