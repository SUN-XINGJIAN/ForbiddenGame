package controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GameOverController {
    @FXML
    private Text failureReasonText;

    // Sets failure reasons
    public void setFailureReason(String reason) {
        if (failureReasonText != null) {
            failureReasonText.setText(reason);
        }
    }
} 