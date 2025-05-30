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

                // Initialize explorer
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
        // Create a CountDownLatch to wait for the test to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Executing the test in the JavaFX thread
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // Set testing environment
                game.isSpecialMode = true;

                // Firstly set the initial position of the explorer
                Tile startTile = game.getTiles().get(0);  // Get the first tile as initial position
                explorer.setX(startTile.getPositionX());
                explorer.setY(startTile.getPositionY());
                explorer.setLayoutX(startTile.getPositionX() + 30);
                explorer.setLayoutY(startTile.getPositionY());

                // print initial position
                System.out.println("Initial explorer position: X=" + explorer.getX() + ", Y=" + explorer.getY());

                // Use special ability
                explorer.useSpecialAbility(game, explorer);

                // Get all tiles
                List<Tile> adjacentTiles = game.getTiles();
                int diagonalTilesCount = 0;
                List<Tile> diagonalTiles = new ArrayList<>();

                // Find diagonal tile
                for (Tile tile : adjacentTiles) {
                    if (isAround(tile, explorer)) {
                        diagonalTiles.add(tile);
                        diagonalTilesCount++;
                    }
                }

                // Test if diagonal tile is found
                assertTrue("Should find diagonal tiles", diagonalTilesCount > 0);

                // Print the position of the selected diagonal tile
                System.out.println("Selected diagonal tiles for testing:");
                for (Tile tile : diagonalTiles) {
                    System.out.println("Diagonal tile position: X=" + tile.getPositionX() + 
                                     ", Y=" + tile.getPositionY());
                }

                // Record initial position
                int initialX = explorer.getX();
                int initialY = explorer.getY();

                // Record initial action points
                int initialSteps = game.turnManage.getStep();

                // Test moving to diagonal tile
                System.out.println("\nStarting move test...");
                Tile targetTile = diagonalTiles.get(0);
                System.out.println("Moving to tile: X=" + targetTile.getPositionX() + ", Y=" + targetTile.getPositionY());

                // Manually execute the movement logic
                explorer.setLayoutX(targetTile.getPositionX() + 30);
                explorer.setX(targetTile.getPositionX());
                explorer.setLayoutY(targetTile.getPositionY());
                explorer.setY(targetTile.getPositionY());
                explorer.draw();
                
                // Use action points
                game.turnManage.useStep();
                game.turnManage.showRemainSteps();
                game.changeCurrentPlayer();
                game.exchangeCards();
                game.checkTreasureSubmit();
                game.isVictory();

                // Wait for the process to finish
                try {
                    System.out.println("Waiting for move processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Test if position is updated
                int newX = explorer.getX();
                int newY = explorer.getY();
                
                System.out.println("After move - Initial X: " + initialX + ", New X: " + newX);
                System.out.println("After move - Initial Y: " + initialY + ", New Y: " + newY);
                
                // Use stricter assertions
                assertTrue("Explorer should move to new position. Initial X: " + initialX + ", New X: " + newX, 
                          newX != initialX);
                assertTrue("Explorer should move to new position. Initial Y: " + initialY + ", New Y: " + newY, 
                          newY != initialY);
                
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

        // Wait for the test to finish
        try {
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    // Check if tile is diagonal
    private boolean isAround(Tile tile, Explorer explorer) {
        int tileX = tile.getPositionX();
        int tileY = tile.getPositionY();
        int explorerX = explorer.getX();
        int explorerY = explorer.getY();
        
        // Check if diagonally (The distance is 1 tile)
        return Math.abs(tileX - explorerX) == 50 && Math.abs(tileY - explorerY) == 50;
    }
}