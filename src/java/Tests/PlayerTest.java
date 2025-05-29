package Tests;

import javafx.application.Platform;
import logic.*;
import org.junit.*;

import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.*;
import cards.TreasureCard;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class PlayerTest {
    
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Diver diver;
    private Engineer engineer;
    private Explorer explorer;
    private Messenger messenger;
    private Navigator navigator;
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
                    // 打印调试信息
                    System.out.println("Current directory: " + System.getProperty("user.dir"));
                    System.out.println("Class path: " + System.getProperty("java.class.path"));
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

                // 初始化所有角色
                diver = new Diver("Diver");
                engineer = new Engineer("Engineer");
                explorer = new Explorer("Explorer");
                messenger = new Messenger("Messenger");
                navigator = new Navigator("Navigator");
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
    public void testPlayerMovement() {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                System.out.println("Starting player movement test...");
                
                // 测试所有角色的基本移动功能
                Player[] players = {diver, engineer, explorer, messenger, navigator, pilot};
                
                for (Player player : players) {
                    System.out.println("\nTesting movement for " + player.getClass().getSimpleName());
                    
                    // 重置步数
                    game.turnManage.setStep(3);  // 设置初始步数为3
                    System.out.println("Reset steps to: " + game.turnManage.getStep());
                    
                    // 设置初始位置
                    Tile startTile = game.getTiles().get(0);
                    player.setX(startTile.getPositionX());
                    player.setY(startTile.getPositionY());
                    player.setLayoutX(startTile.getPositionX() + 30);
                    player.setLayoutY(startTile.getPositionY());
                    
                    // 验证初始位置
                    assertEquals("Initial X position should match", startTile.getPositionX(), player.getX());
                    assertEquals("Initial Y position should match", startTile.getPositionY(), player.getY());
                    
                    // 测试移动到相邻位置
                    List<Tile> adjacentTiles = game.getTiles();
                    for (Tile tile : adjacentTiles) {
                        if (isAdjacentOrSame(tile, player)) {
                            // 记录移动前的行动点
                            int initialSteps = game.turnManage.getStep();
                            System.out.println("Initial steps before move: " + initialSteps);
                            
                            // 执行移动
                            player.setX(tile.getPositionX());
                            player.setY(tile.getPositionY());
                            player.setLayoutX(tile.getPositionX() + 30);
                            player.setLayoutY(tile.getPositionY());
                            player.draw();
                            
                            // 使用行动点
                            game.turnManage.useStep();
                            System.out.println("Steps after move: " + game.turnManage.getStep());
                            
                            // 验证新位置
                            assertEquals("New X position should match", tile.getPositionX(), player.getX());
                            assertEquals("New Y position should match", tile.getPositionY(), player.getY());
                            assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());
                            
                            // 只测试一次移动
                            break;
                        }
                    }
                    
                    // 验证剩余步数
                    assertTrue("Should have remaining steps", game.turnManage.getStep() > 0);
                    System.out.println("Remaining steps: " + game.turnManage.getStep());
                }
                
                System.out.println("Player movement test completed successfully!");
                latch.countDown();
                
            } catch (Exception e) {
                e.printStackTrace();
                fail("Test failed with exception: " + e.getMessage());
                latch.countDown();
            }
        });
        
        try {
            boolean completed = latch.await(3, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }


    @Test
    public void testPlayerPosition() {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                System.out.println("Starting player position test...");
                
                // 测试所有角色的位置获取方法
                Player[] players = {diver, engineer, explorer, messenger, navigator, pilot};
                
                for (Player player : players) {
                    System.out.println("\nTesting position for " + player.getClass().getSimpleName());
                    
                    // 获取位置
                    int x = player.getPositionX(game);
                    int y = player.getPositionY(game);
                    
                    // 验证位置是否有效
                    assertTrue("X position should be valid", x >= 0);
                    assertTrue("Y position should be valid", y >= 0);
                }
                
                System.out.println("Player position test completed successfully!");
                latch.countDown();
                
            } catch (Exception e) {
                e.printStackTrace();
                fail("Test failed with exception: " + e.getMessage());
                latch.countDown();
            }
        });
        
        try {
            boolean completed = latch.await(3, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testPlayerCardExchange() {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                System.out.println("Starting player card exchange test...");
                
                // 测试所有角色的卡牌交换功能
                Player[] players = {diver, engineer, explorer, messenger, navigator, pilot};
                
                // 第一步：初始化所有玩家的卡包
                System.out.println("Initializing player card bags...");
                for (Player player : players) {
                    System.out.println("\nInitializing card bag for " + player.getClass().getSimpleName());
                    
                    // 获取玩家的卡牌包并清空
                    List<TreasureCard> playerBag = player.getBag();
                    assertNotNull("Player bag should not be null", playerBag);
                    playerBag.clear();
                    
                    // 添加初始卡牌
                    TreasureCard card1 = new TreasureCard(0);
                    TreasureCard card2 = new TreasureCard(5);
                    TreasureCard card3 = new TreasureCard(10);
                    
                    playerBag.add(card1);
                    playerBag.add(card2);
                    playerBag.add(card3);
                    
                    // 验证初始卡牌数量
                    assertEquals("Should have 3 cards in bag", 3, player.getBag().size());
                    System.out.println("Initial card count for " + player.getClass().getSimpleName() + 
                                     ": " + player.getBag().size());
                }
                
                // 第二步：测试卡牌交换
                System.out.println("\nTesting card exchange between players...");
                for (int i = 0; i < players.length - 1; i++) {
                    Player player1 = players[i];
                    Player player2 = players[i + 1];
                    
                    System.out.println("\nTesting exchange between " + 
                                     player1.getClass().getSimpleName() + " and " + 
                                     player2.getClass().getSimpleName());
                    
                    List<TreasureCard> bag1 = player1.getBag();
                    List<TreasureCard> bag2 = player2.getBag();
                    
                    // 记录交换前的卡牌数量
                    int initialCount1 = bag1.size();
                    int initialCount2 = bag2.size();
                    
                    // 模拟交换卡牌
                    TreasureCard cardToExchange = bag1.get(0); // 获取第一张卡牌用于交换
                    bag1.remove(cardToExchange);
                    bag2.add(cardToExchange);
                    
                    // 验证交换后的卡牌数量
                    assertEquals("First player should have one less card", 
                               initialCount1 - 1, bag1.size());
                    assertEquals("Second player should have one more card", 
                               initialCount2 + 1, bag2.size());
                    
                    System.out.println("Card counts after exchange - " + 
                                     player1.getClass().getSimpleName() + ": " + bag1.size() + 
                                     ", " + player2.getClass().getSimpleName() + ": " + bag2.size());
                    
                    // 验证交换后的卡牌内容
                    assertTrue("Second player should have the exchanged card", 
                              bag2.contains(cardToExchange));
                    assertFalse("First player should not have the exchanged card", 
                               bag1.contains(cardToExchange));
                }
                
                System.out.println("Player card exchange test completed successfully!");
                latch.countDown();
                
            } catch (Exception e) {
                e.printStackTrace();
                fail("Test failed with exception: " + e.getMessage());
                latch.countDown();
            }
        });
        
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
    private boolean isAdjacentOrSame(Tile tile, Player player) {
        int tileX = tile.getPositionX();
        int tileY = tile.getPositionY();
        int playerX = player.getX();
        int playerY = player.getY();
        
        // 检查是否相邻或相同（距离为1个tile或相同位置）
        return (Math.abs(tileX - playerX) <= 50 && Math.abs(tileY - playerY) <= 50);
    }
}
