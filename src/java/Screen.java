//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class Screen extends Application {
//    private static final int WINDOW_WIDTH = 1200;
//    private static final int WINDOW_HEIGHT = 900;
//
//    @Override
//    public void start(Stage primaryStage) {
//        Pane root = new Pane();
//
//        // 加载背景图片
//        try {
//            Image backgroundImage = new Image(
//                    getClass().getResource("/image/Map/Arena.jpg").toString()
//            );
//            ImageView background = new ImageView(backgroundImage);
//            background.setFitWidth(WINDOW_WIDTH);
//            background.setFitHeight(WINDOW_HEIGHT);
//            root.getChildren().add(background);
//        } catch (Exception e) {
//            System.err.println("加载背景图片失败: " + e.getMessage());
//            root.setStyle("-fx-background-color: #333333;");
//        }
//
//        // 创建菱形按钮网格
//        DiamondButtonGrid buttonGrid = new DiamondButtonGrid();
//        VBox diamondLayout = buttonGrid.getContainer();
//
//        // 居中布局
//        diamondLayout.setLayoutX((WINDOW_WIDTH - diamondLayout.getWidth()) / 2);
//        diamondLayout.setLayoutY((WINDOW_HEIGHT - diamondLayout.getHeight()) / 2);
//        root.getChildren().add(diamondLayout);
//
//        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
//        primaryStage.setTitle("菱形按钮布局游戏界面");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}