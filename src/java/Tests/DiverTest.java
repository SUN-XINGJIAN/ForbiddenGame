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

                // Initialize diver
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
        // Create a CountDownLatch to wait for the test to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Executing the test in the JavaFX thread
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");
                
                // Set testing environment
                game.isSpecialMode = true;
                
                // Firstly set the initial position of the diver
                Tile startTile = game.getTiles().get(0);  // Get the first tile as initial position
                diver.setX(startTile.getPositionX());
                diver.setY(startTile.getPositionY());
                diver.setLayoutX(startTile.getPositionX() + 30);
                diver.setLayoutY(startTile.getPositionY());
                
                // print initial position
                System.out.println("Initial diver position: X=" + diver.getX() + ", Y=" + diver.getY());
                
                // get all tiles
                List<Tile> adjacentTiles = game.getTiles();
                final Tile floodedTile = findFloodedTile(adjacentTiles, diver);

                // Test whether flooded tile is found
                assertNotNull("Should find an adjacent flooded tile", floodedTile);
                System.out.println("Selected flooded tile position: X=" + floodedTile.getPositionX() + 
                                 ", Y=" + floodedTile.getPositionY());

                // Record initial position and action points
                final int initialX = diver.getX();
                final int initialY = diver.getY();
                final int initialSteps = game.turnManage.getStep();

                System.out.println("\nStarting move test...");
                System.out.println("Moving to tile: X=" + floodedTile.getPositionX() + 
                                 ", Y=" + floodedTile.getPositionY());

                // Execute move logic
                diver.setX(floodedTile.getPositionX());
                diver.setY(floodedTile.getPositionY());
                diver.setLayoutX(floodedTile.getPositionX() + 30);
                diver.setLayoutY(floodedTile.getPositionY());
                diver.draw();
                
                // Update game status
                game.turnManage.useStep();
                game.turnManage.showRemainSteps();
                game.changeCurrentPlayer();
                game.exchangeCards();
                game.checkTreasureSubmit();
                game.isVictory();
                
                // End special mode
                game.isSpecialMode = false;

                // Waiting for the processing to be completed
                try {
                    System.out.println("Waiting for move processing...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Test if position is updated
                int newX = diver.getX();
                int newY = diver.getY();
                
                System.out.println("After move - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After move - Initial Y: " + initialY + ", New Y: " + newY);
                
                // Test if position is indeed updated
                assertTrue("Diver should move to new position. Initial X: " + initialX + ", New X: " + newX + 
                          ", Target X: " + floodedTile.getPositionX(), 
                          newX == floodedTile.getPositionX());
                assertTrue("Diver should move to new position. Initial Y: " + initialY + ", New Y: " + newY + 
                          ", Target Y: " + floodedTile.getPositionY(), 
                          newY == floodedTile.getPositionY());
                
                // Test step consumption
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // Test whether special mode is disabled
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

    // Search for flooded tiles
    private Tile findFloodedTile(List<Tile> adjacentTiles, Diver diver) {
        for (Tile tile : adjacentTiles) {
            if (Math.abs(tile.getPositionX() - diver.getX()) <= 50 &&
                Math.abs(tile.getPositionY() - diver.getY()) <= 50 &&
                tile.getPositionY() != diver.getY()) {
                tile.setState(1);  // Set tile to flooded
                return tile;
            }
        }
        return null;
    }
}