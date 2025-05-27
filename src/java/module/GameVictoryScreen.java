package module;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameVictoryScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个文本对象，显示“游戏胜利！”
        Text victoryText = new Text("You win!!！");
        victoryText.setFont(Font.font("Arial", 50));
        victoryText.setFill(Color.GOLD);
        victoryText.setEffect(new DropShadow(10, Color.BLACK));


        Button exitButton = new Button("退出游戏");
        exitButton.setFont(Font.font("Arial", 20));
        exitButton.setOnAction(e -> {
            // 退出程序
            System.exit(0);
        });

        // 创建一个布局
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: black; -fx-alignment: center;");
        layout.getChildren().addAll(victoryText, exitButton);

        // 创建一个场景
        Scene scene = new Scene(layout, 1000, 1000);

        // 设置舞台
        primaryStage.setTitle("GAME WIN!!!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
