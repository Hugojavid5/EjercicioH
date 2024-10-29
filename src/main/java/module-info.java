module org.hugo.ejercicioh {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.hugo.ejercicioh to javafx.fxml;
    exports org.hugo.ejercicioh;
}