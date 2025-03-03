module peete.de {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens peete.de to javafx.fxml;
    exports peete.de;
}