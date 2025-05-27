package controller;

import canvas.PawnCanvas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.ForbiddenGameStarted;

import java.io.IOException;

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

    public void initData(int playerCount, int waterMeterIndex) {
        this.playerCount = playerCount;
        forbiddenGameStarted = new ForbiddenGameStarted(this, playerCount, waterMeterIndex);
    }

    public void setGameOver() {
        try {
            if (forbiddenGameStarted.isDefeat()) { // 判断游戏是否失败
                // 获取当前窗口的 Stage
                Stage stage = (Stage) mainBoard.getScene().getWindow();

                // 加载失败界面的 FXML 文件
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game_over.fxml"));
                Parent root = loader.load();

                // 设置新的场景并显示
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
            if (forbiddenGameStarted.isDefeat()) { // 判断游戏是否失败
                // 获取当前窗口的 Stage
                Stage stage = (Stage) mainBoard.getScene().getWindow();

                // 加载失败界面的 FXML 文件
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game_success.fxml"));
                Parent root = loader.load();

                // 设置新的场景并显示
                Scene scene = new Scene(root, 1000, 1000);
                stage.setScene(scene);
                stage.setTitle("Game Over");
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}