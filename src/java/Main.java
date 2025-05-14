import javafx.application.Application;
import javafx.stage.Stage;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Director.getInstance().init(stage);
        System.out.println("✅ 初始界面加载成功");

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}