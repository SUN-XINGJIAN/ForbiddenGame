package Tests;

import javafx.application.Platform;
import logic.ForbiddenGameStarted;
import org.junit.*;
import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.Engineer;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class EngineerTest {
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Engineer engineer;

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

                // 初始化工程师
                engineer = new Engineer("Engineer");

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
    public void testEngineerSpecialAbility() {
        // 创建一个 CountDownLatch 来等待测试完成
        CountDownLatch latch = new CountDownLatch(1);

        // 在JavaFX线程中执行测试
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // 设置测试环境
                game.isSpecialMode = true;

                // 首先设置engineer的初始位置
                Tile startTile = game.getTiles().get(0);  // 获取第一个tile作为起始位置
                engineer.setX(startTile.getPositionX());
                engineer.setY(startTile.getPositionY());
                engineer.setLayoutX(startTile.getPositionX() + 30);
                engineer.setLayoutY(startTile.getPositionY());

                // 打印初始位置
                System.out.println("Initial engineer position: X=" + engineer.getX() + ", Y=" + engineer.getY());

                // 使用特殊能力
                engineer.useSpecialAbility(game, engineer);

                // 获取所有版块
                List<Tile> adjacentTiles = game.getTiles();
                int floodedTilesCount = 0;
                List<Tile> floodedTiles = new ArrayList<>();

                // 找到并设置相邻的淹没岛屿
                for (Tile tile : adjacentTiles) {
                    if (isAdjacentOrSame(tile, engineer)) {
                        tile.setState(1);  // 设置岛屿为淹没状态
                        floodedTiles.add(tile);
                        floodedTilesCount++;

                        // 只选择两个相邻的淹没岛屿
                        if (floodedTilesCount >= 2) {
                            break;
                        }
                    }
                }

                // 验证是否找到足够的淹没岛屿
                assertEquals("Should find exactly two adjacent flooded tiles", 2, floodedTilesCount);

                // 打印选中的淹没岛屿的位置
                System.out.println("Selected flooded tiles for testing:");
                for (Tile tile : floodedTiles) {
                    System.out.println("Flooded tile position: X=" + tile.getPositionX() +
                            ", Y=" + tile.getPositionY());
                }

                // 记录初始行动点
                int initialSteps = game.turnManage.getStep();

                // 确保Tile有事件处理器
                for (Tile tile : floodedTiles) {
                    if (tile.getOnMouseClicked() == null) {
                        tile.setOnMouseClicked(event -> {
                            tile.setState(0);
                            game.turnManage.useStep();
                        });
                    }
                }

                // 测试修复第一个岛屿
                System.out.println("\nStarting first tile repair test...");
                Tile firstTile = floodedTiles.get(0);
                System.out.println("Repairing first tile: X=" + firstTile.getPositionX() + ", Y=" + firstTile.getPositionY());

                // 模拟点击第一个岛屿
                firstTile.getOnMouseClicked().handle(null);

                // 等待处理完成
                try {
                    System.out.println("Waiting for first repair processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 验证第一个岛屿状态
                assertEquals("First tile should be saved", 0, firstTile.getState());
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // 测试修复第二个岛屿
                System.out.println("\nStarting second tile repair test...");
                Tile secondTile = floodedTiles.get(1);
                System.out.println("Repairing second tile: X=" + secondTile.getPositionX() + ", Y=" + secondTile.getPositionY());

                // 模拟点击第二个岛屿
                secondTile.getOnMouseClicked().handle(null);

                // 等待处理完成
                try {
                    System.out.println("Waiting for second repair processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 验证第二个岛屿状态
                assertEquals("Second tile should be saved", 0, secondTile.getState());
                assertEquals("Should use another step", initialSteps - 2, game.turnManage.getStep());

                System.out.println("Test completed successfully!");

                // 测试完成后，减少 latch 计数
                latch.countDown();

            } catch (Exception e) {
                e.printStackTrace();
                fail("Test failed with exception: " + e.getMessage());
                latch.countDown();
            }
        });

        // 等待测试完成
        try {
            boolean completed = latch.await(3, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    // 辅助方法：检查tile是否相邻或相同
    private boolean isAdjacentOrSame(Tile tile, Engineer engineer) {
        int tileX = tile.getPositionX();
        int tileY = tile.getPositionY();
        int engineerX = engineer.getX();
        int engineerY = engineer.getY();

        // 检查是否相邻或相同（距离为1个tile或相同位置）
        return (Math.abs(tileX - engineerX) <= 50 && Math.abs(tileY - engineerY) <= 50);
    }
}
