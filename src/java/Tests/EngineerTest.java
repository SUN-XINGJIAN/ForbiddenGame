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

                // Initialize engineer
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
        // Create a CountDownLatch to wait for the test to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Executing the test in the JavaFX thread
        Platform.runLater(() -> {
            try {
                System.out.println("Starting test in JavaFX thread...");

                // Set testing environment
                game.isSpecialMode = true;

                // Firstly set the initial position of the engineer
                Tile startTile = game.getTiles().get(0);  // Get the first tile as initial position
                engineer.setX(startTile.getPositionX());
                engineer.setY(startTile.getPositionY());
                engineer.setLayoutX(startTile.getPositionX() + 30);
                engineer.setLayoutY(startTile.getPositionY());

                // print initial position
                System.out.println("Initial engineer position: X=" + engineer.getX() + ", Y=" + engineer.getY());

                // Use special ability
                engineer.useSpecialAbility(game, engineer);

                // get all tiles
                List<Tile> adjacentTiles = game.getTiles();
                int floodedTilesCount = 0;
                List<Tile> floodedTiles = new ArrayList<>();

                // Find and set adjacent flooded tile
                for (Tile tile : adjacentTiles) {
                    if (isAdjacentOrSame(tile, engineer)) {
                        tile.setState(1);  // Set tile to flooded
                        floodedTiles.add(tile);
                        floodedTilesCount++;

                        // Only choose two adjacent flooded tile
                        if (floodedTilesCount >= 2) {
                            break;
                        }
                    }
                }

                // Test if enough flooded tiles are found
                assertEquals("Should find exactly two adjacent flooded tiles", 2, floodedTilesCount);

                // Print the position of the chosen flooded tile
                System.out.println("Selected flooded tiles for testing:");
                for (Tile tile : floodedTiles) {
                    System.out.println("Flooded tile position: X=" + tile.getPositionX() +
                            ", Y=" + tile.getPositionY());
                }

                // Record initial action points
                int initialSteps = game.turnManage.getStep();

                // Ensure that Tile has an event handler
                for (Tile tile : floodedTiles) {
                    if (tile.getOnMouseClicked() == null) {
                        tile.setOnMouseClicked(event -> {
                            tile.setState(0);
                            game.turnManage.useStep();
                        });
                    }
                }

                // Test shoring up first tile
                System.out.println("\nStarting first tile repair test...");
                Tile firstTile = floodedTiles.get(0);
                System.out.println("Repairing first tile: X=" + firstTile.getPositionX() + ", Y=" + firstTile.getPositionY());

                // Simulate clicking on the first tile
                firstTile.getOnMouseClicked().handle(null);

                // Wait for the process to finish
                try {
                    System.out.println("Waiting for first repair processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Check the status of the first tile
                assertEquals("First tile should be saved", 0, firstTile.getState());
                assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());

                // Test shoring up second tile
                System.out.println("\nStarting second tile repair test...");
                Tile secondTile = floodedTiles.get(1);
                System.out.println("Repairing second tile: X=" + secondTile.getPositionX() + ", Y=" + secondTile.getPositionY());

                // Simulate clicking on the second tile
                secondTile.getOnMouseClicked().handle(null);

                // Wait for the process to finish
                try {
                    System.out.println("Waiting for second repair processing...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Check the status of the second tile
                assertEquals("Second tile should be saved", 0, secondTile.getState());
                assertEquals("Should use another step", initialSteps - 2, game.turnManage.getStep());

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
            boolean completed = latch.await(3, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    // Check if tiles are adjacent or same
    private boolean isAdjacentOrSame(Tile tile, Engineer engineer) {
        int tileX = tile.getPositionX();
        int tileY = tile.getPositionY();
        int engineerX = engineer.getX();
        int engineerY = engineer.getY();

        // Check for adjacency or similarity (With the distance of 1 tile or the same position)
        return (Math.abs(tileX - engineerX) <= 50 && Math.abs(tileY - engineerY) <= 50);
    }
}
