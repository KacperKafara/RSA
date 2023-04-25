module pl.grupa3.rsa {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.grupa3.rsa to javafx.fxml;
    exports pl.grupa3.rsa;
}