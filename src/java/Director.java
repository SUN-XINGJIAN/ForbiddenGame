import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        InitialScene(stage);

    }

    public void InitialScene(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerCountSelection.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, FRAME_WIDTH, FRAME_HEIGHT); // 设置窗口尺寸
            stage.setScene(scene);
            stage.setTitle("Choose the number of players");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameOverScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game_over.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, FRAME_WIDTH, FRAME_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("Game Over");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static Director getInstance(){
        return instance;
    }

    public Stage getStage() {
        return stage;
    }
}