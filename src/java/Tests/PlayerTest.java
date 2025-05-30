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
                    // 打印调试信息
                    System.out.println("Current directory: " + System.getProperty("user.dir"));
                    System.out.println("Class path: " + System.getProperty("java.class.path"));
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

                // Initialize all roles
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
        // Create a CountDownLatch to wait for the test to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Executing the test in the JavaFX thread
        Platform.runLater(() -> {
            try {
                System.out.println("Starting player movement test...");
                
                // Testing the movement of all roles
                Player[] players = {diver, engineer, explorer, messenger, navigator, pilot};
                
                for (Player player : players) {
                    System.out.println("\nTesting movement for " + player.getClass().getSimpleName());
                    
                    // Reset action points
                    game.turnManage.setStep(3);  // Set initial action points to 3
                    System.out.println("Reset steps to: " + game.turnManage.getStep());
                    
                    // Set the initial position
                    Tile startTile = game.getTiles().get(0); // Get the first tile as initial position
                    player.setX(startTile.getPositionX());
                    player.setY(startTile.getPositionY());
                    player.setLayoutX(startTile.getPositionX() + 30);
                    player.setLayoutY(startTile.getPositionY());
                    
                    // Check initial position
                    assertEquals("Initial X position should match", startTile.getPositionX(), player.getX());
                    assertEquals("Initial Y position should match", startTile.getPositionY(), player.getY());
                    
                    // Test moving to adjacent tiles
                    List<Tile> adjacentTiles = game.getTiles();
                    for (Tile tile : adjacentTiles) {
                        if (isAdjacentOrSame(tile, player)) {
                            // Record action points before movement
                            int initialSteps = game.turnManage.getStep();
                            System.out.println("Initial steps before move: " + initialSteps);
                            
                            // Execute movement
                            player.setX(tile.getPositionX());
                            player.setY(tile.getPositionY());
                            player.setLayoutX(tile.getPositionX() + 30);
                            player.setLayoutY(tile.getPositionY());
                            player.draw();
                            
                            // Use action points
                            game.turnManage.useStep();
                            System.out.println("Steps after move: " + game.turnManage.getStep());
                            
                            // Check new position
                            assertEquals("New X position should match", tile.getPositionX(), player.getX());
                            assertEquals("New Y position should match", tile.getPositionY(), player.getY());
                            assertEquals("Should use one step", initialSteps - 1, game.turnManage.getStep());
                            
                            // Test movement only once
                            break;
                        }
                    }
                    
                    // Check leftover action points
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
                
                // Test the get position method for all roles
                Player[] players = {diver, engineer, explorer, messenger, navigator, pilot};
                
                for (Player player : players) {
                    System.out.println("\nTesting position for " + player.getClass().getSimpleName());
                    
                    // Get position
                    int x = player.getPositionX(game);
                    int y = player.getPositionY(game);
                    
                    // Check valid position
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
                
                // Check the card exchange function for all players
                Player[] players = {diver, engineer, explorer, messenger, navigator, pilot};
                
                // Firstly initialize all the cards for the players
                System.out.println("Initializing player card bags...");
                for (Player player : players) {
                    System.out.println("\nInitializing card bag for " + player.getClass().getSimpleName());
                    
                    // Get the card bag of players and clear it
                    List<TreasureCard> playerBag = player.getBag();
                    assertNotNull("Player bag should not be null", playerBag);
                    playerBag.clear();
                    
                    // Add initial cards
                    TreasureCard card1 = new TreasureCard(0);
                    TreasureCard card2 = new TreasureCard(5);
                    TreasureCard card3 = new TreasureCard(10);
                    
                    playerBag.add(card1);
                    playerBag.add(card2);
                    playerBag.add(card3);
                    
                    // Test initial card count
                    assertEquals("Should have 3 cards in bag", 3, player.getBag().size());
                    System.out.println("Initial card count for " + player.getClass().getSimpleName() + 
                                     ": " + player.getBag().size());
                }
                
                // Secondly test the exchange of cards
                System.out.println("\nTesting card exchange between players...");
                for (int i = 0; i < players.length - 1; i++) {
                    Player player1 = players[i];
                    Player player2 = players[i + 1];
                    
                    System.out.println("\nTesting exchange between " + 
                                     player1.getClass().getSimpleName() + " and " + 
                                     player2.getClass().getSimpleName());
                    
                    List<TreasureCard> bag1 = player1.getBag();
                    List<TreasureCard> bag2 = player2.getBag();
                    
                    // Record the card count before exchanging
                    int initialCount1 = bag1.size();
                    int initialCount2 = bag2.size();
                    
                    // Simulate exchanging cards
                    TreasureCard cardToExchange = bag1.get(0); // Get the first card for exchange
                    bag1.remove(cardToExchange);
                    bag2.add(cardToExchange);
                    
                    // Test the card count after exchanging
                    assertEquals("First player should have one less card",
                               initialCount1 - 1, bag1.size());
                    assertEquals("Second player should have one more card", 
                               initialCount2 + 1, bag2.size());
                    
                    System.out.println("Card counts after exchange - " + 
                                     player1.getClass().getSimpleName() + ": " + bag1.size() + 
                                     ", " + player2.getClass().getSimpleName() + ": " + bag2.size());
                    
                    // Test the cards after exchanging
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

    // Check if tiles are adjacent or same
    private boolean isAdjacentOrSame(Tile tile, Player player) {
        int tileX = tile.getPositionX();
        int tileY = tile.getPositionY();
        int playerX = player.getX();
        int playerY = player.getY();
        
        // Check for adjacency or similarity (With the distance of 1 tile or the same position)
        return (Math.abs(tileX - playerX) <= 50 && Math.abs(tileY - playerY) <= 50);
    }
}
