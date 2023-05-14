package pl.grupa3.rsa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    RSA rsa = new RSA();
    private final byte[] delimiter = "¡¿., abdsfjkgfsdfdasgdagsdbfdhgndfjkgbaruhsdt¡¿".getBytes();
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
    @FXML
    CheckBox userInput;

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
                    } else if (type == 2) {
                        List<Byte> publicKey = new ArrayList<>();
                        byte[] n = rsa.getN().toByteArray();
                        byte[] e = rsa.getE().toByteArray();
                        for (byte b : n) {
                            publicKey.add(b);
                        }
                        for (byte b : delimiter) {
                            publicKey.add(b);
                        }
                        for (byte b : e) {
                            publicKey.add(b);
                        }
                        outputStream.write(listToArray(publicKey));
                    } else if (type == 3) {
                        outputStream.write(rsa.getD().toByteArray());
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
                if (type == 0) {
                    msgTab = file;
                    for (byte b : msgTab) {
                        msg.add(b);
                    }
                    fileField.setText(new String(file));
                } else if (type == 1) {
                    encryptedMsgTab = file;
                    for (byte b : encryptedMsgTab) {
                        encryptedMsg.add(b);
                    }
                    fileField.setText(new String(file));
                } else if (type == 2) {
                    rsa.loadKey(0, file);
                    nField.setText(rsa.getN().toString());
                    eField.setText(rsa.getE().toString());
                } else if (type == 3) {
                    rsa.loadKey(1, file);
                    dField.setText(rsa.getD().toString());
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
            if (userInput.isSelected()) {
                byte[] tmp = decryptedMsgField.getText().getBytes();
                msg = new ArrayList<>();
                for (byte b : tmp) {
                    msg.add(b);
                }
            }
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
        loadFile(0, decryptedMsgField);
    }
    @FXML
    public void loadEncryptedFile() {
        loadFile(1, encryptedMsgField);
    }
    @FXML
    public void saveDecryptedFile() {
        saveFile(0);
    }
    @FXML
    public void saveEncryptedFile() {
        saveFile(1);
    }
    @FXML
    public void saveKey() {
        saveFile(2);
        saveFile(3);
    }
    @FXML
    public void loadKey() {
        loadFile(2, nField);
        loadFile(3, dField);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        encryptedMsgField.setWrapText(true);
        decryptedMsgField.setWrapText(true);
    }
}