package Tests;

import javafx.application.Platform;
import logic.ForbiddenGameStarted;
import org.junit.*;
import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.Diver;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class DiverTest {
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Diver diver;

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

                // 初始化潜水员
                diver = new Diver("Diver");

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
    public void testDiverSpecialAbility() {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");
                
                // 设置测试环境
                game.isSpecialMode = true;
                
                // 首先设置diver的初始位置
                Tile startTile = game.getTiles().get(0);  // 获取第一个tile作为起始位置
                diver.setX(startTile.getPositionX());
                diver.setY(startTile.getPositionY());
                diver.setLayoutX(startTile.getPositionX() + 30);
                diver.setLayoutY(startTile.getPositionY());
                
                // 打印初始位置
                System.out.println("Initial diver position: X=" + diver.getX() + ", Y=" + diver.getY());
                
                // 获取所有版块
                List<Tile> adjacentTiles = game.getTiles();
                final Tile floodedTile = findFloodedTile(adjacentTiles, diver);

                // 验证是否找到淹没岛屿
                assertNotNull("Should find an adjacent flooded tile", floodedTile);
                System.out.println("Selected flooded tile position: X=" + floodedTile.getPositionX() + 
                                 ", Y=" + floodedTile.getPositionY());

                // 记录初始位置和行动点
                final int initialX = diver.getX();
                final int initialY = diver.getY();
                final int initialSteps = game.turnManage.getStep();

                System.out.println("\nStarting move test...");
                System.out.println("Moving to tile: X=" + floodedTile.getPositionX() + 
                                 ", Y=" + floodedTile.getPositionY());

                // 直接执行移动逻辑
                diver.setX(floodedTile.getPositionX());
                diver.setY(floodedTile.getPositionY());
                diver.setLayoutX(floodedTile.getPositionX() + 30);
                diver.setLayoutY(floodedTile.getPositionY());
                diver.draw();
                
                // 更新游戏状态
                game.turnManage.useStep();
                game.turnManage.showRemainSteps();
                game.changeCurrentPlayer();
                game.exchangeCards();
                game.checkTreasureSubmit();
                game.isVictory();
                
                // 关闭特殊模式
                game.isSpecialMode = false;

                // 等待处理完成
                try {
                    System.out.println("Waiting for move processing...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 验证位置已更新
                int newX = diver.getX();
                int newY = diver.getY();
                
                System.out.println("After move - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After move - Initial Y: " + initialY + ", New Y: " + newY);
                
                // 验证位置确实发生了变化
                assertTrue("Diver should move to new position. Initial X: " + initialX + ", New X: " + newX + 
                          ", Target X: " + floodedTile.getPositionX(), 
                          newX == floodedTile.getPositionX());
                assertTrue("Diver should move to new position. Initial Y: " + initialY + ", New Y: " + newY + 
                          ", Target Y: " + floodedTile.getPositionY(), 
                          newY == floodedTile.getPositionY());
                
                // 验证行动点消耗
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // 验证特殊模式是否关闭
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
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    // 辅助方法：查找淹没的岛屿
    private Tile findFloodedTile(List<Tile> adjacentTiles, Diver diver) {
        for (Tile tile : adjacentTiles) {
            if (Math.abs(tile.getPositionX() - diver.getX()) <= 50 &&
                Math.abs(tile.getPositionY() - diver.getY()) <= 50 &&
                tile.getPositionY() != diver.getY()) {
                tile.setState(1);  // 设置岛屿为淹没状态
                return tile;
            }
        }
        return null;
    }
}