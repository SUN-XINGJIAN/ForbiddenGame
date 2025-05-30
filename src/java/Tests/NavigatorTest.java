package Tests;

import javafx.application.Platform;
import logic.ForbiddenGameStarted;
import org.junit.*;
import static org.junit.Assert.*;
import controller.ScreenController;
import board.Tile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import module.*;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class NavigatorTest {
    private ForbiddenGameStarted game;
    private ScreenController screenController;
    private Navigator navigator;
    private Diver diver;

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
                navigator = new Navigator("Navigator");
                diver = new Diver("testdiver");

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
    public void testNavigatorSpecialAbility() {
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
                navigator.setX(startTile.getPositionX());
                navigator.setY(startTile.getPositionY());
                navigator.setLayoutX(startTile.getPositionX() + 30);
                navigator.setLayoutY(startTile.getPositionY());

                diver.setX(startTile.getPositionX());
                diver.setY(startTile.getPositionY());
                diver.setLayoutX(startTile.getPositionX() + 30);
                diver.setLayoutY(startTile.getPositionY());

                System.out.println("Initial navigator position: X=" + navigator.getX() + ", Y=" + navigator.getY());
                System.out.println("Initial other player position: X=" + diver.getX() + ", Y=" + diver.getY());

                // Use special ability
                navigator.useSpecialAbility(game, navigator);

                // Get all the tiles
                List<Tile> allTiles = game.getTiles();
                List<Tile> availableTiles = new ArrayList<>();

                // Find usable target tile (Not flooded and different from current position)
                for (Tile tile : allTiles) {
                    if (tile.getState() == 0 && // 0 represents unflooded
                        (tile.getPositionX() != diver.getX() || tile.getPositionY() != diver.getY())) {
                        availableTiles.add(tile);
                    }
                }

                // Check if found usable target tile
                assertTrue("Should find available tiles", !availableTiles.isEmpty());

                // Choose target tile
                Tile targetTile = availableTiles.get(0);
                System.out.println("Selected target tile: X=" + targetTile.getPositionX() + 
                                 ", Y=" + targetTile.getPositionY());

                // Record initial position
                int initialX = diver.getX();
                int initialY = diver.getY();
                int initialSteps = game.turnManage.getStep();

                // Simulate moving other players
                diver.setLayoutX(targetTile.getPositionX() + 30);
                diver.setX(targetTile.getPositionX());
                diver.setLayoutY(targetTile.getPositionY());
                diver.setY(targetTile.getPositionY());
                diver.draw();

                // Testing using action points
                game.turnManage.useStep();
                game.turnManage.showRemainSteps();

                // Wait for the process to finish
                try {
                    System.out.println("Waiting for movement processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Test if position is updated
                int newX = diver.getX();
                int newY = diver.getY();

                System.out.println("After movement - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After movement - Initial Y: " + initialY + ", New Y: " + newY);

                // Check for position change
                assertTrue("Other player should move to new position", 
                          newX != initialX || newY != initialY);
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // Manually disable special mode
                game.isSpecialMode = false;
                System.out.println("Special mode disabled: " + !game.isSpecialMode);

                // Test if special mode is disabled
                assertFalse("Special mode should be disabled after using ability", game.isSpecialMode);

                System.out.println("Test completed successfully!");

                // After the test is completed, reduce the latch count
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
