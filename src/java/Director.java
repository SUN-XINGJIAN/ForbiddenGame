import controller.PlayerCountController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Director {

    private static Director instance = new Director();
    private Stage stage;
    public static final int FRAME_WIDTH = 900,FRAME_HEIGHT = 900;


    public Director(){}

    public void init(Stage stage){
        this.stage = stage;
//        InitialScene(stage);
        showPlayerCountDialog();

    }

//    public void InitialScene(Stage stage){
//        Frame.load(stage);
//    }


    public static Director getInstance(){
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    // 显示输入玩家人数
    private void showPlayerCountDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerCountDialog.fxml"));
            Parent root = loader.load();
            PlayerCountController controller = loader.getController();

            // 创建新的Stage作为对话框
            Stage dialogStage = new Stage();
            dialogStage.setTitle("选择玩家人数: ");
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.showAndWait(); // 显示并等待关闭

            int playerCount = controller.getPlayerCount();
            Frame.load(stage, playerCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}