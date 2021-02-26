import encryptors.ColumnEncrptr;
import encryptors.RailwayEncrpt;
import encryptors.VigenerEncrpt;
import constants.EncryptionMethod;
import constants.Errors;
import constants.Language;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.stage.Window;

import encryptors.*;

import java.io.*;

public class Controller extends Application {


    @FXML
    private RadioButton columnCheck;

    @FXML
    private RadioButton vigenerCheck;

    @FXML
    private RadioButton railwayCheck;

    @FXML
    private TextField keyArea;

    @FXML
    private TextArea cipherText;

    @FXML
    private TextArea plainText;


    private Encryptor encryptor;
    private Filter msgFilter;

    private Window window;

    @FXML
    void setupHint() {
        String hint = switch (getEncryptionMethod()) {
            case RailwayFence -> "Input a number";
            case Vigener -> "Введите слово";
            case Column -> "Input a word";
        };
        keyArea.setPromptText(hint);
    }


    @FXML
    void openToEncrypt() {
        loadFile(true);
    }

    @FXML
    void openToDecrypt() {
        loadFile(false);
    }


    @FXML
    void saveCipherText() {
        saveFile(false);
    }

    @FXML
    void savePlainText() {
        saveFile(true);
    }

    @FXML
    void stopProgram() {
        System.exit(0);
    }


    @FXML
    void encrypt() {
        cipherText.clear();
        if (plainText.getText().length() > 0) {
            String key;
            if ((key = setup()) != null) {
                String filteredMsg = msgFilter.filterMsg(plainText.getText(), false);
                if (filteredMsg.length() > 0) {
                    cipherText.setText(encryptor.encrypt(filteredMsg, key));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect input");
                    alert.setHeaderText(null);
                    alert.setContentText("Your plain text does not contain any matching characters.");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    void decrypt() {
        plainText.clear();
        if (cipherText.getText().length() > 0) {
            String key;
            if ((key = setup()) != null) {
                String filteredMsg = msgFilter.filterMsg(cipherText.getText(), false);
                if (filteredMsg.length() > 0) {
                    plainText.setText(encryptor.decrypt(filteredMsg, key));
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect input");
                    alert.setHeaderText(null);
                    alert.setContentText("Your cipher text does not contain any matching characters.");
                    alert.showAndWait();
                }
            }
        }
    }

    private String setup() {
        encryptor = null;
        msgFilter = null;
        Filter keyFilter = null;
        KeyChecker keyChecker = null;
        switch (getEncryptionMethod()) {
            case RailwayFence -> {
                encryptor = new RailwayEncrpt();
                msgFilter = new Filter(Language.ENG);
                keyFilter = new Filter(Language.NUM);
                keyChecker = new KeyChecker(Language.NUM);
            }
            case Vigener -> {
                encryptor = new VigenerEncrpt(Language.RUS);
                msgFilter = new Filter(Language.RUS);
                keyFilter = msgFilter;
                keyChecker = new KeyChecker(Language.RUS);
            }
            case Column -> {
                encryptor = new ColumnEncrptr();
                msgFilter = new Filter(Language.ENG);
                keyFilter = msgFilter;
                keyChecker = new KeyChecker(Language.ENG);
            }
        }
        Errors error;
        String key = keyFilter.filterMsg(keyArea.getText(), false);
        if ((error = keyChecker.checkKey(key)) != null) {
            key = null;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect key");
            alert.setHeaderText(null);
            alert.setContentText(getErrorMsg(error));
            alert.showAndWait();
        }
        return key;
    }

    private String getErrorMsg(Errors warning) {
        switch (warning) {
            case ZERO_LEN -> {
                return "The key does not contain any matching characters.";
            }
            case NUMBER_OVERFLOW -> {
                return "Your key is too large.";
            }
            case ZERO -> {
                return "The key must to be greater than 0.";
            }
            default -> {
                return null;
            }
        }
    }

    void saveFile(boolean savePlainText) {
        try {
            File file = chooseFile(true);
            if (file != null) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                if (savePlainText)
                    writer.write(plainText.getText());
                else
                    writer.write(cipherText.getText());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadFile(boolean toEncrypt) {
        clearTextAreas();
        try {
            File file = chooseFile(false);
            if (file != null) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder text = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null)
                    text.append(str);
                reader.close();
                if (toEncrypt) {
                    plainText.setText(text.toString());
                }
                else {
                    cipherText.setText(text.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearTextAreas() {
        cipherText.clear();
        plainText.clear();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
        primaryStage.setTitle("Encryptor");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        window = primaryStage.getOwner();
        primaryStage.show();

    }

    public File chooseFile(boolean toSave) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(".txt");
        fileChooser.setInitialDirectory(new File("res/"));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")));
        File file;
        if (toSave)
            file = fileChooser.showSaveDialog(window);
        else
            file = fileChooser.showOpenDialog(window);

        return file;
    }

    private EncryptionMethod getEncryptionMethod() {
        EncryptionMethod selected = null;
        if (railwayCheck.isSelected()) selected = EncryptionMethod.RailwayFence;
        else if (vigenerCheck.isSelected()) selected = EncryptionMethod.Vigener;
        else if (columnCheck.isSelected()) selected = EncryptionMethod.Column;
        return selected;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
