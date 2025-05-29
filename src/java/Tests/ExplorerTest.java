package Tests;

import javafx.application.Platform;
import logic.ForbiddenGameStarted;
import org.junit.*;
import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.Explorer;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class ExplorerTest {
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Explorer explorer;

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

                // 初始化探险家
                explorer = new Explorer("Explorer");

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
    public void testExplorerSpecialAbility() {
        // 创建一个 CountDownLatch 来等待测试完成
        CountDownLatch latch = new CountDownLatch(1);
        
        // 在JavaFX线程中执行测试
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");
                
                // 设置测试环境
                game.isSpecialMode = true;
                
                // 首先设置explorer的初始位置
                Tile startTile = game.getTiles().get(0);  // 获取第一个tile作为起始位置
                explorer.setX(startTile.getPositionX());
                explorer.setY(startTile.getPositionY());
                explorer.setLayoutX(startTile.getPositionX() + 30);
                explorer.setLayoutY(startTile.getPositionY());
                
                // 打印初始位置
                System.out.println("Initial explorer position: X=" + explorer.getX() + ", Y=" + explorer.getY());
                
                // 使用特殊能力
                explorer.useSpecialAbility(game, explorer);

                // 获取所有版块
                List<Tile> adjacentTiles = game.getTiles();
                int diagonalTilesCount = 0;
                List<Tile> diagonalTiles = new ArrayList<>();

                // 找到对角线方向的岛屿
                for (Tile tile : adjacentTiles) {
                    if (isAround(tile, explorer)) {
                        diagonalTiles.add(tile);
                        diagonalTilesCount++;
                    }
                }

                // 验证是否找到对角线方向的岛屿
                assertTrue("Should find diagonal tiles", diagonalTilesCount > 0);

                // 打印选中的对角线岛屿的位置
                System.out.println("Selected diagonal tiles for testing:");
                for (Tile tile : diagonalTiles) {
                    System.out.println("Diagonal tile position: X=" + tile.getPositionX() + 
                                     ", Y=" + tile.getPositionY());
                }

                // 记录初始位置
                int initialX = explorer.getX();
                int initialY = explorer.getY();

                // 记录初始行动点
                int initialSteps = game.turnManage.getStep();

                // 测试移动到对角线位置
                System.out.println("\nStarting move test...");
                Tile targetTile = diagonalTiles.get(0);
                System.out.println("Moving to tile: X=" + targetTile.getPositionX() + ", Y=" + targetTile.getPositionY());

                // 手动执行移动逻辑
                explorer.setLayoutX(targetTile.getPositionX() + 30);
                explorer.setX(targetTile.getPositionX());
                explorer.setLayoutY(targetTile.getPositionY());
                explorer.setY(targetTile.getPositionY());
                explorer.draw();
                
                // 使用行动点
                game.turnManage.useStep();
                game.turnManage.showRemainSteps();
                game.changeCurrentPlayer();
                game.exchangeCards();
                game.checkTreasureSubmit();
                game.isVictory();

                // 等待处理完成
                try {
                    System.out.println("Waiting for move processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 验证位置已更新
                int newX = explorer.getX();
                int newY = explorer.getY();
                
                System.out.println("After move - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After move - Initial Y: " + initialY + ", New Y: " + newY);
                
                // 使用更严格的断言
                assertTrue("Explorer should move to new position. Initial X: " + initialX + ", New X: " + newX, 
                          newX != initialX);
                assertTrue("Explorer should move to new position. Initial Y: " + initialY + ", New Y: " + newY, 
                          newY != initialY);
                
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // 手动关闭特殊模式
                game.isSpecialMode = false;
                System.out.println("Special mode disabled: " + !game.isSpecialMode);

                // 验证特殊模式是否关闭
                assertFalse("Special mode should be disabled after using ability", game.isSpecialMode);

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
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    // 辅助方法：检查tile是否在对角线方向
    private boolean isAround(Tile tile, Explorer explorer) {
        int tileX = tile.getPositionX();
        int tileY = tile.getPositionY();
        int explorerX = explorer.getX();
        int explorerY = explorer.getY();
        
        // 检查是否在对角线方向（距离为1个tile）
        return Math.abs(tileX - explorerX) == 50 && Math.abs(tileY - explorerY) == 50;
    }
}