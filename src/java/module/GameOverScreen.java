package module;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOverScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个文本显示 "游戏失败"
        Text gameOverText = new Text("You Fail!!！");
        gameOverText.setFont(Font.font("Arial", 50));
        gameOverText.setFill(Color.RED);


        // 创建一个按钮 "退出游戏"
        Button exitButton = new Button("退出游戏");
        exitButton.setFont(Font.font(20));
        exitButton.setOnAction(event -> {
            System.exit(0); // 退出程序
        });

        // 将文本和按钮添加到一个垂直布局中
        VBox layout = new VBox(20); // 组件之间间隔为 20
        layout.setAlignment(Pos.CENTER); // 居中对齐
        layout.getChildren().addAll(gameOverText, exitButton);

        // 创建场景并设置到舞台
        Scene scene = new Scene(layout, 1000, 1000);
        primaryStage.setTitle("GAME FAIL");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
