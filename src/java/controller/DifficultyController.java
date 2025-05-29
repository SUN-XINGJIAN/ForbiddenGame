package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class DifficultyController {
    @FXML private RadioButton rbNovice;
    @FXML private RadioButton rbNormal;
    @FXML private RadioButton rbElite;
    @FXML private RadioButton rbLegendary;
    @FXML private ToggleGroup difficultyGroup;

    private int playerCount;

    // Set player count received from previous screen
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    // Starts game with selecting difficulty levels
    @FXML
    private void startGame() throws IOException {
        int waterMeterIndex = 1;
        
        if (rbNormal.isSelected()) waterMeterIndex = 2;
        else if (rbElite.isSelected()) waterMeterIndex = 3;
        else if (rbLegendary.isSelected()) waterMeterIndex = 4;

        // Get current window
        Stage stage = (Stage) rbNovice.getScene().getWindow();

        // Load main game
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Screen.fxml"));
        Parent root = loader.load();

        // Initialize the game with player count and difficulty selection
        ScreenController screenController = loader.getController();
        screenController.initData(playerCount, waterMeterIndex);

        // Switch to game panel
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }
} 