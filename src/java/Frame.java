import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Frame {

    public static void load(Stage stage){
        System.out.println("🔧 正在加载 Frame..."); // ✅ 这一步必须看到

        Parent root = null;
        try {
            System.out.println("🎯 加载 initialBoard.fxml"); // ✅ 确认是否进入这个判断
            root = FXMLLoader.load(Frame.class.getResource("/fxml/Screen.fxml"));

        } catch (IOException e) {
            System.out.println("❌ 加载 FXML 失败");
            e.printStackTrace();
            return; // 加载失败就别往下执行
        }

        if (root == null) {
            System.out.println("❌ root 是 null，Scene 无法创建！");
            return;
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Forbidden Island");
        stage.setWidth(Director.FRAME_WIDTH);
        stage.setHeight(Director.FRAME_HEIGHT);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        System.out.println("✅ 初始界面加载成功");
    }



}