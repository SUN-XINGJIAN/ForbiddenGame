package Tests;

import javafx.application.Platform;
import logic.ForbiddenGameStarted;
import org.junit.*;
import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.Pilot;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class PilotTest {
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Pilot pilot;

    @BeforeClass
    public static void setupJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (Exception e) {
            // 如果已经启动，忽略异常
        }
    }

    @Before
    public void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // 尝试多个可能的路径
                String[] possiblePaths = {
                        "/fxml/Screen.fxml",           // 标准路径
                        "fxml/Screen.fxml",            // 不带前导斜杠
                        "/Screen.fxml",                // 根目录
                        "Screen.fxml"                  // 当前目录
                };

                URL fxmlUrl = null;
                for (String path : possiblePaths) {
                    fxmlUrl = getClass().getResource(path);
                    if (fxmlUrl != null) {
                        System.out.println("Found FXML file at: " + path);
                        break;
                    }
                }

                if (fxmlUrl == null) {
                    throw new RuntimeException("Cannot find FXML file. Tried paths: " + Arrays.toString(possiblePaths));
                }

                // 初始化FXML加载器
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                // 获取ScreenController实例
                screenController = loader.getController();
                if (screenController == null) {
                    throw new RuntimeException("ScreenController is null");
                }

                // 初始化游戏
                screenController.initData(2, 1);
                game = new ForbiddenGameStarted(screenController, 2, 1);
                if (game == null) {
                    throw new RuntimeException("Game object is null after initialization");
                }

                // 初始化飞行员
                pilot = new Pilot("Pilot");

                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
                throw new RuntimeException("Failed to initialize: " + e.getMessage(), e);
            }
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        if (!completed) {
            throw new RuntimeException("Failed to initialize JavaFX components");
        }

        if (game == null) {
            throw new RuntimeException("Game object is null after initialization");
        }
    }

    @Test
    public void testPilotSpecialAbility() {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // 设置测试环境
                game.isSpecialMode = true;
                game.turnManage.setStep(3);
                System.out.println("Initial steps: " + game.turnManage.getStep());

                // 设置pilot的初始位置
                Tile startTile = game.getTiles().get(0);
                pilot.setX(startTile.getPositionX());
                pilot.setY(startTile.getPositionY());
                pilot.setLayoutX(startTile.getPositionX() + 30);
                pilot.setLayoutY(startTile.getPositionY());

                System.out.println("Initial pilot position: X=" + pilot.getX() + ", Y=" + pilot.getY());

                // 获取所有版块
                List<Tile> allTiles = game.getTiles();
                List<Tile> availableTiles = new ArrayList<>();

                // 找到可用的目标岛屿
                for (Tile tile : allTiles) {
                    if (tile.getState() == 0 && 
                        (tile.getPositionX() != pilot.getX() || tile.getPositionY() != pilot.getY())) {
                        availableTiles.add(tile);
                    }
                }

                assertTrue("Should find available tiles", !availableTiles.isEmpty());

                // 选择目标岛屿
                Tile targetTile = availableTiles.get(0);
                System.out.println("Selected target tile: X=" + targetTile.getPositionX() + 
                                 ", Y=" + targetTile.getPositionY());

                // 使用特殊能力
                pilot.useSpecialAbility(game, pilot);

                // 记录初始位置和行动点
                int initialX = pilot.getX();
                int initialY = pilot.getY();
                int initialSteps = game.turnManage.getStep();

                // 模拟点击事件
                targetTile.getOnMouseClicked().handle(null);

                // 等待处理完成
                try {
                    System.out.println("Waiting for flight processing...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 验证位置已更新
                int newX = pilot.getX();
                int newY = pilot.getY();

                System.out.println("After flight - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After flight - Initial Y: " + initialY + ", New Y: " + newY);

                // 验证位置变化
                assertTrue("Pilot should move to new position", newX != initialX || newY != initialY);
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // 验证特殊模式是否在点击事件后关闭
                assertFalse("Special mode should be disabled after using ability", game.isSpecialMode);

                System.out.println("Test completed successfully!");
                latch.countDown();

            } catch (Exception e) {
                e.printStackTrace();
                fail("Test failed with exception: " + e.getMessage());
                latch.countDown();
            }
        });

        try {
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }
}