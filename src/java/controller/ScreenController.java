package controller;

import canvas.PawnCanvas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button SelectFloodCards;

    @FXML
    private Button saveTheIsland;

    // 声明一个PawnCanvas对象，用于绘制棋盘
    private ForbiddenGameStarted forbiddenGameStarted;


    private boolean isMoveMode = false;
    private Button currentPawnButton;

    @FXML
    private void initialize() {
        forbiddenGameStarted = new ForbiddenGameStarted(this);


    }

    @FXML
    public Button getSaveTheIsland() {
        return saveTheIsland;
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

}