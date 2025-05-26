package controller;

import canvas.PawnCanvas;
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

    // 声明一个PawnCanvas对象，用于绘制棋盘
    private ForbiddenGameStarted forbiddenGameStarted;


    private boolean isMoveMode = false;
    private Button currentPawnButton;
    private int playerCount; // 玩家数量

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

    public void initData(int playerCount) {
        this.playerCount = playerCount;
        forbiddenGameStarted = new ForbiddenGameStarted(this, playerCount);
    }

}