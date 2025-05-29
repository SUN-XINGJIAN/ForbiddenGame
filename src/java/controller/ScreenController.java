package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ForbiddenGameStarted;

public class ScreenController {

    // Declare a Pane object to be used for displaying the chessboard
    @FXML
    private Pane mainBoard;

    @FXML
    private Button move;

    @FXML
    private Button useSpecialSkill;

    @FXML
    private ImageView FloodDeck;

    @FXML
    private ImageView DiverBag;

    @FXML
    private Label RemainSteps;

    @FXML
    private Button ExchangeCards;

    @FXML
    private Button TurnOver;

    @FXML
    private Button saveTheIsland;

    @FXML
    private Button useSpecialCardButton;

    // Declare a PawnCanvas object to be used for drawing the chessboard
    private ForbiddenGameStarted forbiddenGameStarted;

    private boolean isMoveMode = false;
    private Button currentPawnButton;
    private int playerCount;

    @FXML
    private void initialize() {
//        forbiddenGameStarted = new ForbiddenGameStarted(this);
    }

    @FXML
    public Button getSaveTheIsland() {
        return saveTheIsland;
    }

    public Button getUseSpecialCardButton() {
        return useSpecialCardButton;
    }

    public ImageView getFloodDeck() {
        return FloodDeck;
    }

    public ImageView getDiverBag() {
        return DiverBag;
    }

    public Pane getMainBoard() {
        return mainBoard;
    }

    public Label getRemainSteps() {
        return RemainSteps;
    }

    public Button getMove() {
        return move;
    }

    public Button getUseSpecialSkill(){
        return useSpecialSkill;
    }

    public Button getExchangeCards() {
        return ExchangeCards;
    }

    public Button getTurnOver() {return TurnOver;}

    public void initData(int playerCount, int waterMeterIndex) {
        this.playerCount = playerCount;
        forbiddenGameStarted = new ForbiddenGameStarted(this, playerCount, waterMeterIndex);
    }

    public void setGameOver(String reason) {
        try {
            if (forbiddenGameStarted.isDefeat()) { // See if the game is over
                // Get the Stage for current window
                Stage stage = (Stage) mainBoard.getScene().getWindow();

                // Loading the fxml for the game over panel
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game_over.fxml"));
                Parent root = loader.load();

                // Obtain the GameOverController and set the failure reason
                GameOverController controller = loader.getController();
                if (controller != null) {
                    controller.setFailureReason(reason);
                }

                // Set a new scene and display it
                Scene scene = new Scene(root, 1000, 1000);
                stage.setScene(scene);
                stage.setTitle("Game Over");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWin() {
        try {
                // Get the Stage for current window
                Stage stage = (Stage) mainBoard.getScene().getWindow();

                // Loading the fxml for the game success panel
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game_success.fxml"));
                Parent root = loader.load();

                // Set a new scene and display it
                Scene scene = new Scene(root, 1000, 1000);
                stage.setScene(scene);
                stage.setTitle("Game Over");
                stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}