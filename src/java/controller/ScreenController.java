package controller;

import canvas.PawnCanvas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.ForbiddenGameStarted;

public class ScreenController {

    // 声明一个Pane对象，用于显示棋盘
    @FXML
    private Pane mainBoard;

    // 声明一个Button对象，用于触发事件
    @FXML
    private Button button1;

    @FXML
    private Button move;



    // 声明一个PawnCanvas对象，用于绘制棋盘
    private ForbiddenGameStarted forbiddenGameStarted;


    private boolean isMoveMode = false;
    private Button currentPawnButton;

    @FXML
    private void initialize() {
        forbiddenGameStarted = new ForbiddenGameStarted(this);



    }


    public Pane getMainBoard() {
        return mainBoard;
    }

    public Button getButton1() {
        return button1;
    }



    public Button getMove() {
        return move;
    }
}