package pl.grupa3.rsa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class HelloController {
    RSA rsa = new RSA();

    @FXML
    TextArea nField;
    @FXML
    TextArea eField;
    @FXML
    TextArea dField;

    @FXML
    public void generateKey() {
        rsa.generateKey();
        nField.setText(rsa.getN().toString());
        eField.setText(rsa.getE().toString());
        dField.setText(rsa.getD().toString());
    }
}