package controller;

import canvas.PawnCanvas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.ForbiddenGameStarted;

public class ScreenController {

    // 声明一个Pane对象，用于显示棋盘
    @FXML
    private Pane mainBoard;

    @FXML
    private Button move;

    @FXML
    private ImageView FloodDeck;

    @FXML
    private ImageView DiverBag;

    @FXML
    private Button SelectFloodCards;

    @FXML
    private Button TurnOver;

    @FXML
    private Button saveTheIsland;

    // 声明一个PawnCanvas对象，用于绘制棋盘
    private ForbiddenGameStarted forbiddenGameStarted;


    private boolean isMoveMode = false;
    private Button currentPawnButton;

//    @FXML
//    private void initialize() {
//        forbiddenGameStarted = new ForbiddenGameStarted(this);
//
//
//    }

    @FXML
    public Button getSaveTheIsland() {
        return saveTheIsland;
    }

    public ImageView getFloodDeck() {
        return FloodDeck;
    }

    public ImageView getDiverBag() {
        return DiverBag;
    }

    @FXML
    private Label messageLabel;


    public Pane getMainBoard() {
        return mainBoard;
    }



    public Button getMove() {
        return move;
    }

    public Button getSelectFloodCards() {
        return SelectFloodCards;
    }

    public Button getTurnOver() {return TurnOver;}

    public void setPlayerCount(int count) {
        Platform.runLater(() -> {
            // 清理之前的游戏状态
            if (this.forbiddenGameStarted != null) {
                this.forbiddenGameStarted.cleanUp();
            }
            // 创建新游戏实例
            this.forbiddenGameStarted = new ForbiddenGameStarted(this, count);
        });
    }

    private void initializeGameComponents() {
        // 这里可以初始化其他游戏组件
    }
}