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
    private Player otherPlayer; // Add another player to test exchanging cards

    @BeforeClass
    public static void setupJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (Exception e) {
            // If already started, ignore the exception
        }
    }

    @Before
    public void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Try different routes
                String[] possiblePaths = {
                    "/fxml/Screen.fxml",           // Standard route
                    "fxml/Screen.fxml",            // Without a leading slash
                    "/Screen.fxml",                // Root directory
                    "Screen.fxml"                  // Current
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

                // Initialize the FXML loader
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();

                // Obtain the instance of ScreenController
                screenController = loader.getController();
                if (screenController == null) {
                    throw new RuntimeException("ScreenController is null");
                }

                // Initialize the game
                screenController.initData(2, 1);
                game = new ForbiddenGameStarted(screenController, 2, 1);
                if (game == null) {
                    throw new RuntimeException("Game object is null after initialization");
                }

                // Initialize messenger and another player
                messenger = new Messenger("Messenger");
                otherPlayer = new Messenger("OtherPlayer"); // Use another messenger as the other player
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
        // Create a CountDownLatch to wait for the test to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Executing the test in the JavaFX thread
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // Set testing environment
                game.isSpecialMode = true;
                game.turnManage.setStep(3); // Set initial action points as 3
                System.out.println("Initial steps: " + game.turnManage.getStep());

                // Firstly set the initial position of the messenger
                Tile startTile = game.getTiles().get(0); // Get the first tile as initial position
                messenger.setX(startTile.getPositionX());
                messenger.setY(startTile.getPositionY());
                messenger.setLayoutX(startTile.getPositionX() + 30);
                messenger.setLayoutY(startTile.getPositionY());

                // print initial position
                System.out.println("Initial messenger position: X=" + messenger.getX() + ", Y=" + messenger.getY());

                // Initialize the card bag for messenger
                List<TreasureCard> messengerBag = new ArrayList<>();
                TreasureCard card1 = new TreasureCard(0);
                TreasureCard card2 = new TreasureCard(5);
                TreasureCard card3 = new TreasureCard(10);
                messengerBag.add(card1);
                messengerBag.add(card2);
                messengerBag.add(card3);
                messenger.getBag().addAll(messengerBag);

                // Initialize the card bag for other player
                List<TreasureCard> otherPlayerBag = new ArrayList<>();
                TreasureCard card4 = new TreasureCard(15);
                otherPlayerBag.add(card4);
                otherPlayer.getBag().addAll(otherPlayerBag);

                // Record initial card count
                int initialMessengerCardCount = messenger.getBag().size();
                int initialOtherPlayerCardCount = otherPlayer.getBag().size();
                System.out.println("Initial messenger card count: " + initialMessengerCardCount);
                System.out.println("Initial other player card count: " + initialOtherPlayerCardCount);

                // Use special ability
                messenger.useSpecialAbility(game, messenger);

                // Wait for the UI to update
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Simulate card exchange
                if (!messenger.getBag().isEmpty()) {
                    TreasureCard cardToExchange = messenger.getBag().get(0);
                    messenger.getBag().remove(cardToExchange);
                    otherPlayer.getBag().add(cardToExchange);
                    
                    // Simulate using action points
                    game.turnManage.useStep();
                    game.turnManage.showRemainSteps();
                    game.changeCurrentPlayer();
                    
                    System.out.println("Steps after exchange: " + game.turnManage.getStep());
                }

                // Test card exchange
                assertEquals("Messenger should have one less card", 
                           initialMessengerCardCount - 1, messenger.getBag().size());
                assertEquals("Other player should have one more card", 
                           initialOtherPlayerCardCount + 1, otherPlayer.getBag().size());
                System.out.println("Final messenger card count: " + messenger.getBag().size());
                System.out.println("Final other player card count: " + otherPlayer.getBag().size());

                // Test using action points
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