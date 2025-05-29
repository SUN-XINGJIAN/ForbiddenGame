package Tests;

import javafx.application.Platform;
import logic.ForbiddenGameStarted;
import org.junit.*;
import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.Messenger;
import module.Player;
import cards.TreasureCard;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class MessengerTest {
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Messenger messenger;
    private Player otherPlayer; // 添加另一个玩家用于测试卡牌交换

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

                // 初始化信使和另一个玩家
                messenger = new Messenger("Messenger");
                otherPlayer = new Messenger("OtherPlayer"); // 使用另一个信使作为测试对象
                game.currentPlayers.add(messenger);
                game.currentPlayers.add(otherPlayer);

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
    public void testMessengerSpecialAbility() {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // 设置测试环境
                game.isSpecialMode = true;
                game.turnManage.setStep(3); // 设置初始行动点为3
                System.out.println("Initial steps: " + game.turnManage.getStep());

                // 设置messenger的初始位置
                Tile startTile = game.getTiles().get(0);
                messenger.setX(startTile.getPositionX());
                messenger.setY(startTile.getPositionY());
                messenger.setLayoutX(startTile.getPositionX() + 30);
                messenger.setLayoutY(startTile.getPositionY());

                System.out.println("Initial messenger position: X=" + messenger.getX() + ", Y=" + messenger.getY());

                // 初始化messenger的卡牌包
                List<TreasureCard> messengerBag = new ArrayList<>();
                TreasureCard card1 = new TreasureCard(0);
                TreasureCard card2 = new TreasureCard(5);
                TreasureCard card3 = new TreasureCard(10);
                messengerBag.add(card1);
                messengerBag.add(card2);
                messengerBag.add(card3);
                messenger.getBag().addAll(messengerBag);

                // 初始化otherPlayer的卡牌包
                List<TreasureCard> otherPlayerBag = new ArrayList<>();
                TreasureCard card4 = new TreasureCard(15);
                otherPlayerBag.add(card4);
                otherPlayer.getBag().addAll(otherPlayerBag);

                // 记录初始卡牌数量
                int initialMessengerCardCount = messenger.getBag().size();
                int initialOtherPlayerCardCount = otherPlayer.getBag().size();
                System.out.println("Initial messenger card count: " + initialMessengerCardCount);
                System.out.println("Initial other player card count: " + initialOtherPlayerCardCount);

                // 使用特殊能力
                messenger.useSpecialAbility(game, messenger);

                // 等待UI更新
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 模拟卡牌交换
                if (!messenger.getBag().isEmpty()) {
                    TreasureCard cardToExchange = messenger.getBag().get(0);
                    messenger.getBag().remove(cardToExchange);
                    otherPlayer.getBag().add(cardToExchange);
                    
                    // 模拟使用行动点
                    game.turnManage.useStep();
                    game.turnManage.showRemainSteps();
                    game.changeCurrentPlayer();
                    
                    System.out.println("Steps after exchange: " + game.turnManage.getStep());
                }

                // 验证卡牌交换
                assertEquals("Messenger should have one less card", 
                           initialMessengerCardCount - 1, messenger.getBag().size());
                assertEquals("Other player should have one more card", 
                           initialOtherPlayerCardCount + 1, otherPlayer.getBag().size());
                System.out.println("Final messenger card count: " + messenger.getBag().size());
                System.out.println("Final other player card count: " + otherPlayer.getBag().size());

                // 验证行动点使用
                int remainingSteps = game.turnManage.getStep();
                assertEquals("Should use one step", 2, remainingSteps);
                System.out.println("Remaining steps: " + remainingSteps);

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

}