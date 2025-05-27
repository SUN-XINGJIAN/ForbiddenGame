package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerCountController {
    @FXML private RadioButton rb2;
    @FXML private RadioButton rb3;
    @FXML private RadioButton rb4;
    @FXML private ToggleGroup playerGroup;

    @FXML
    private void startGame() throws IOException {
        int playerCount = 2;
        if (rb3.isSelected()) playerCount = 3;
        else if (rb4.isSelected()) playerCount = 4;

        Stage stage = (Stage) rb2.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DifficultySelection.fxml"));
        Parent root = loader.load();

        DifficultyController difficultyController = loader.getController();
        difficultyController.setPlayerCount(playerCount);

        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }
}