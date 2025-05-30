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

                // Initialize pilot
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
        // Create a CountDownLatch to wait for the test to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Executing the test in the JavaFX thread
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // Set testing environment
                game.isSpecialMode = true;
                game.turnManage.setStep(3);
                System.out.println("Initial steps: " + game.turnManage.getStep());

                // Firstly set the initial position of the explorer
                Tile startTile = game.getTiles().get(0); // Get the first tile as initial position
                pilot.setX(startTile.getPositionX());
                pilot.setY(startTile.getPositionY());
                pilot.setLayoutX(startTile.getPositionX() + 30);
                pilot.setLayoutY(startTile.getPositionY());

                System.out.println("Initial pilot position: X=" + pilot.getX() + ", Y=" + pilot.getY());

                // Get all tiles
                List<Tile> allTiles = game.getTiles();
                List<Tile> availableTiles = new ArrayList<>();

                // Find usable target tile
                for (Tile tile : allTiles) {
                    if (tile.getState() == 0 && 
                        (tile.getPositionX() != pilot.getX() || tile.getPositionY() != pilot.getY())) {
                        availableTiles.add(tile);
                    }
                }

                assertTrue("Should find available tiles", !availableTiles.isEmpty());

                // Choose target tile
                Tile targetTile = availableTiles.get(0);
                System.out.println("Selected target tile: X=" + targetTile.getPositionX() + 
                                 ", Y=" + targetTile.getPositionY());

                // Use special ability
                pilot.useSpecialAbility(game, pilot);

                // Record initial position
                int initialX = pilot.getX();
                int initialY = pilot.getY();

                // Record initial action points
                int initialSteps = game.turnManage.getStep();

                // Simulate click event
                targetTile.getOnMouseClicked().handle(null);

                // Wait for the process to finish
                try {
                    System.out.println("Waiting for flight processing...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Test if position is updated
                int newX = pilot.getX();
                int newY = pilot.getY();

                System.out.println("After flight - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After flight - Initial Y: " + initialY + ", New Y: " + newY);

                // Test the position change
                assertTrue("Pilot should move to new position", newX != initialX || newY != initialY);
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // Test if special mode closes after click event
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