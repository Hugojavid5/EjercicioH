module org.hugo.ejercicioh {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.hugo.ejercicioh to javafx.fxml;
    exports org.hugo.ejercicioh;
}