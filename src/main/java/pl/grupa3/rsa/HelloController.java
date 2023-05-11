package pl.grupa3.rsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    RSA rsa = new RSA();

    @FXML
    TextArea nField;
    @FXML
    TextArea eField;
    @FXML
    TextArea dField;
    @FXML
    TextArea encryptedMsgField;
    @FXML
    TextArea decryptedMsgField;

    byte[] msgTab;
    byte[] encryptedMsgTab;
    byte[] decryptedMsgTab;
    List<Byte> msg = new ArrayList<>();
    List<Byte> encryptedMsg = new ArrayList<>();
    List<Byte> decryptedMsg = new ArrayList<>();

    private void saveFile(int type) {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showSaveDialog(new Stage());
        if (f != null) {
            String fileName = f.getAbsolutePath();
            try (OutputStream outputStream = new FileOutputStream(fileName)) {
                try {
                    if (type == 0) {
                        outputStream.write(decryptedMsgTab);
                    } else if (type == 1) {
                        outputStream.write(encryptedMsgTab);
                    }
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private byte[] listToArray(List<Byte> list) {
        byte[] arr = new byte[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
    private void loadFile(int type, TextArea fileField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ALL FILES", "*.*"));
        File f = fileChooser.showOpenDialog(null);
        if (f != null) {
            String fileName = f.getAbsolutePath();
            try (InputStream inputStream = new FileInputStream(fileName)) {
                byte[] file = inputStream.readAllBytes();
                if (type == 1) {
                    msgTab = file;
                    for (byte b : msgTab) {
                        msg.add(b);
                    }
                    fileField.setText(new String(file));
                }
                if (type == 2) {
                    encryptedMsgTab = file;
                    for (byte b : encryptedMsgTab) {
                        encryptedMsg.add(b);
                    }
                    fileField.setText(new String(file));
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @FXML
    public void generateKey() {
        rsa.generateKey();
        nField.setText(rsa.getN().toString());
        eField.setText(rsa.getE().toString());
        dField.setText(rsa.getD().toString());
    }
    @FXML
    public void encrypt() {
        try {
            encryptedMsg = rsa.encrypt(msg);
            encryptedMsgTab = listToArray(encryptedMsg);
            encryptedMsgField.setText(new String(encryptedMsgTab));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void decrypt() {
        try {
            decryptedMsg = rsa.decrypt(encryptedMsg);
            decryptedMsgTab = listToArray(decryptedMsg);
            decryptedMsgField.setText(new String(decryptedMsgTab));
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void loadDecryptedFile() {
        loadFile(1, decryptedMsgField);
    }
    @FXML
    public void loadEncryptedFile() {
        loadFile(2, encryptedMsgField);
    }
    @FXML
    public void saveDecryptedFile(ActionEvent event) {
        saveFile(0);
    }
    @FXML
    public void saveEncryptedFile(ActionEvent event) {
        saveFile(1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        encryptedMsgField.setWrapText(true);
        decryptedMsgField.setWrapText(true);
    }
}