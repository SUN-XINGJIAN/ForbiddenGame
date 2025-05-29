// The main logic control class
package logic;

import board.Treasure;
import board.WaterMeter;
import cards.Card;
import cards.TreasureCard;
import controller.ScreenController;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import board.Tile;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import module.Diver;
import module.Engineer;
import module.Explorer;
import module.Messenger;
import module.Navigator;
import module.Pilot;
import module.Player;
import module.PlayerBag;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static cards.TreasureCard.Type.*;

public class ForbiddenGameStarted {
    public ScreenController screenController;
    public Pane mainBoard;
    public boolean isMoveMode = false;
    public boolean isSpecialMode = false;
    private int[] random1= new int[24]; // A randomly generated island tile position indices array
    private List<Tile> tiles = new ArrayList<>();
    private List<TreasureCard> treasureCards = new ArrayList<>();
    private List<WaterMeter> waterMeters = new ArrayList<>();
    private boolean isSaveMode = false; // Whether in 'save island' mode
    private Label messageLabel;
    private List<Canvas> floodedTileCanvases = new ArrayList<>();
    public List<TreasureCard> currentBag = new ArrayList<>(); // The card inventory for current player
    private List<List> currentBags = new ArrayList<>(); // The card inventory for all players
    private List<Treasure> treasures = new ArrayList<>();
    private List<Player> players = new ArrayList<>(); // Arraylist of all the player roles that are in the game
    private List<Player> players1 = new ArrayList<>(); // Arraylist of all the player roles that are in the UI
    public List<Player> currentPlayers = new ArrayList<>(); // Arraylist of all the players that are in the game
    public List<PlayerBag> playerBags = new ArrayList<>(); // The UI for the player inventory
    private int currentWaterMeterIndex = 1;
    private Button useSpecialCardButton;
    private Diver diver, diver1;
    private Engineer engineer, engineer1;
    private Explorer explorer, explorer1;
    private Messenger messenger, messenger1;
    private Navigator navigator, navigator1;
    private Pilot pilot, pilot1;
    public Player currentPlayer;
    private Player currentPlayer1;
    public TurnManage turnManage;
    public int step;
    private int playerCount;
    private boolean b = false;
    private int x;
    public int pilotCount,diverCount,engineerCount,explorerCount,messengerCount,navigatorCount; // The counter for each role's special ability
    public boolean isGetSOIL ,isGetFIRE ,isGetCLOUD ,isGetWATER ; // The status for collecting treasures

    // Constructor for initializing the game
    public ForbiddenGameStarted(ScreenController screenController, int playerCount, int initialWaterMeterIndex) {
        // Initialize the random1 array (the tiles of the island)
        random1 = new int[24];
        List<Integer> numbers = new ArrayList<>();
        this.playerCount = playerCount;

        // Add 1-24 numbers
        for (int i = 1; i <= 24; i++) {
            numbers.add(i);
        }

        // Shuffle the order of the numbers
        Collections.shuffle(numbers);

        // Add the shuffled numbers in the random1 array
        for (int i = 0; i < random1.length; i++) {
            random1[i] = numbers.get(i);
        }

        this.screenController = screenController;
        mainBoard = screenController.getMainBoard();
        initializeGame();

        setStartFloodCards();

        // Add a click event handler to the "move" button
        screenController.getMove().setOnAction(event -> {
            isMoveMode = !isMoveMode;  // Switch move mode
            if (isMoveMode) {
                screenController.getMove().setText("Cancel Move");
                enableTileMovement(currentPlayer);  // Enable the mode where the pawns can be moved each time a click occurs
            } else {
                screenController.getMove().setText("Move");
            }
        });

        screenController.getSaveTheIsland().setOnAction(event -> {
            isSaveMode = !isSaveMode; // Switch save island mode
            if (isSaveMode) {
                screenController.getSaveTheIsland().setText("Cancel Save");
                enableSaveMode(); // Start save island mode
            } else {
                screenController.getSaveTheIsland().setText("Save the Island");
                disableSaveMode(); // Cancel save island mode
            }
        });

        // The event for ending each turn
        screenController.getTurnOver().setOnAction(event -> {
            handleTurnOver();
            screenController.getUseSpecialSkill().setDisable(false);
        });

        screenController.getUseSpecialCardButton().setOnAction(event -> {
            useSpecialCards(); // Activate the usage of special cards
        });

        turnManage = new TurnManage(screenController);
        turnManage.showRemainSteps();

        screenController.getUseSpecialSkill().setOnAction(event ->{
            isSpecialMode = !isSpecialMode;
            if (currentPlayer instanceof Pilot) {
                if (pilotCount == 0) {
                    useSpecialSkill();
                } else {
                    showMessage("You can only use special ability once per turn");
                    screenController.getUseSpecialSkill().setDisable(true);
                }
            }
            if (currentPlayer instanceof Explorer) {
                useSpecialSkill();
            }
            if (currentPlayer instanceof Engineer) {
                if (engineerCount == 0) {
                    useSpecialSkill();
                } else {
                    showMessage("You can only use special ability once per turn");
                    screenController.getUseSpecialSkill().setDisable(true);
                }
            }
            if(currentPlayer instanceof Messenger){
                if (messengerCount == 0) {
                    useSpecialSkill();
                }else{
                    showMessage("You can only use special ability once per turn");
                    screenController.getUseSpecialSkill().setDisable(true);
                }
            }
            if(currentPlayer instanceof Navigator){
                if (navigatorCount == 0) {
                    useSpecialSkill();
                }else{
                    showMessage("You can only use special ability once per turn");
                    screenController.getUseSpecialSkill().setDisable(true);
                }
            }
            if(currentPlayer instanceof Diver){
                useSpecialSkill();
            }

        });

        WaterMeter waterMeter1 = new WaterMeter(0);
        WaterMeter waterMeter2 = new WaterMeter(1);
        WaterMeter waterMeter3 = new WaterMeter(2);
        WaterMeter waterMeter4 = new WaterMeter(3);
        WaterMeter waterMeter5 = new WaterMeter(4);
        WaterMeter waterMeter6 = new WaterMeter(5);
        WaterMeter waterMeter7 = new WaterMeter(6);
        WaterMeter waterMeter8 = new WaterMeter(7);
        WaterMeter waterMeter9 = new WaterMeter(8);
        WaterMeter waterMeter10 = new WaterMeter(9);
        Collections.addAll(waterMeters, waterMeter1, waterMeter2, waterMeter3, waterMeter4, waterMeter5, waterMeter6, waterMeter7, waterMeter8, waterMeter9, waterMeter10);

        currentWaterMeterIndex = initialWaterMeterIndex;
        mainBoard.getChildren().addAll(waterMeters.get(currentWaterMeterIndex));
        waterMeters.get(currentWaterMeterIndex).draw();

    }

    // Initializing the basic components of the game (tiles, cards, player role)
    private void initializeGame() {
        Tile tile1 = new Tile(random1[0],282,194);
        Tile tile2 = new Tile(random1[1],332,194);
        Tile tile3 = new Tile(random1[2],232,244);
        Tile tile4 = new Tile(random1[3],282,244);
        Tile tile5 = new Tile(random1[4],332,244);
        Tile tile24 = new Tile(random1[23],382,244);
        Tile tile6 = new Tile(random1[5],182,294);
        Tile tile7 = new Tile(random1[6],232,294);
        Tile tile8 = new Tile(random1[7],282,294);
        Tile tile9 = new Tile(random1[8],332,294);
        Tile tile10 = new Tile(random1[9],382,294);
        Tile tile11 = new Tile(random1[10],432,294);
        Tile tile12 = new Tile(random1[11],182,344);
        Tile tile13 = new Tile(random1[12],232,344);
        Tile tile14 = new Tile(random1[13],282,344);
        Tile tile15 = new Tile(random1[14],332,344);
        Tile tile16 = new Tile(random1[15],382,344);
        Tile tile17 = new Tile(random1[16],432,344);
        Tile tile18 = new Tile(random1[17],232,394);
        Tile tile19 = new Tile(random1[18],282,394);
        Tile tile20 = new Tile(random1[19],332,394);
        Tile tile21 = new Tile(random1[20],382,394);
        Tile tile22 = new Tile(random1[21],282,444);
        Tile tile23 = new Tile(random1[22],332,444);

        // Add all Tile objects to the "tiles" list
        Collections.addAll(tiles, tile1, tile2, tile3, tile4, tile5, tile24, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tile14, tile15, tile16, tile17, tile18, tile19, tile20, tile21, tile22, tile23);

        TreasureCard soil = new TreasureCard(0);
        TreasureCard cloud = new TreasureCard(5);
        TreasureCard water = new TreasureCard(10);
        TreasureCard fire = new TreasureCard(15);
        TreasureCard helicopter = new TreasureCard(20);
        TreasureCard sandbags = new TreasureCard(23);
        TreasureCard waterRise = new TreasureCard(26);

        // Add all TreasureCard objects to the "treasureCards" list
        Collections.addAll(treasureCards, soil, cloud, water, fire, helicopter, sandbags, waterRise);

        mainBoard.getChildren().addAll(tiles);
        for (Tile tile : tiles) {
            tile.draw();
        }

        Treasure treasure1 = new Treasure(1,196,177);
        Treasure treasure2 = new Treasure(2,445,177);
        Treasure treasure3 = new Treasure(3,196,446);
        Treasure treasure4 = new Treasure(4,445,446);

        // Add all Treasure objects to the "treasures" list
        Collections.addAll(treasures, treasure1, treasure2, treasure3, treasure4);

        mainBoard.getChildren().addAll(treasures);
        for(Treasure treasure : treasures){
            treasure.draw();
        }

        diver = new Diver("Diver");
        engineer = new Engineer("Engineer");
        explorer = new Explorer("Explorer");
        messenger = new Messenger("Messenger");
        navigator = new Navigator("Navigator");
        pilot = new Pilot("Pilot");

        diver1 = new Diver("Diver");
        engineer1 = new Engineer("Engineer");
        explorer1 = new Explorer("Explorer");
        messenger1 = new Messenger("Messenger");
        navigator1 = new Navigator("Navigator");
        pilot1 = new Pilot("Pilot");

        // Add all Player objects to the "players" list
        Collections.addAll(players, diver, engineer, explorer, messenger, navigator, pilot);
        // Add all Player1 objects to the "players1" list
        Collections.addAll(players1, diver1, engineer1, explorer1, messenger1, navigator1, pilot1);

        mainBoard.getChildren().addAll(players);
        currentPlayers = getRandomPlayers(players, playerCount);
        for(Player player : currentPlayers){
            if(player instanceof Diver || player instanceof Engineer || player instanceof Explorer || player instanceof Messenger || player instanceof Navigator || player instanceof Pilot){
                player.draw();
                player.setLayoutX(player.getPositionX(this));
                player.setX(player.getPositionX(this)-30);
                player.setLayoutY(player.getPositionY(this));
                player.setY(player.getPositionY(this));
            }
            currentBags.add(player.getBag());
        }

        for(List<TreasureCard> bag : currentBags){
            sentSpecialCardsWithoutWaterRise(bag);
        }

        currentPlayer = currentPlayers.getFirst();
        for(Player p : players1) {
            if(p.getType().equals(currentPlayer.getType())){
                currentPlayer1 = p;
                mainBoard.getChildren().add(currentPlayer1);
                currentPlayer1.draw();
                currentPlayer1.setLayoutX(155);
                currentPlayer1.setLayoutY(30);
            }
        }
        currentBag = currentBags.getFirst();

        PlayerBag diverBag = new PlayerBag(PlayerBag.playerType.Diver);
        PlayerBag engineerBag = new PlayerBag(PlayerBag.playerType.Engineer);
        PlayerBag explorerBag = new PlayerBag(PlayerBag.playerType.Explorer);
        PlayerBag messengerBag = new PlayerBag(PlayerBag.playerType.Messenger);
        PlayerBag navigatorBag = new PlayerBag(PlayerBag.playerType.Navigator);
        PlayerBag pilotBag = new PlayerBag(PlayerBag.playerType.Pilot);

        // Add all PlayerBag objects to the "playerBags" list
        Collections.addAll(playerBags, diverBag, engineerBag, explorerBag, messengerBag, navigatorBag, pilotBag);

        mainBoard.getChildren().addAll(playerBags);

        for(int i=0;i<currentPlayers.size();i++){
            if(currentPlayers.get(i) instanceof Diver){
                playerBags.getFirst().draw();
                playerBags.getFirst().setLayoutX(124);
                playerBags.getFirst().setLayoutY(517+i*69);
            }
            if(currentPlayers.get(i) instanceof Engineer){
                playerBags.get(1).draw();
                playerBags.get(1).setLayoutX(124);
                playerBags.get(1).setLayoutY(517+i*69);
            }
            if(currentPlayers.get(i) instanceof Explorer){
                playerBags.get(2).draw();
                playerBags.get(2).setLayoutX(124);
                playerBags.get(2).setLayoutY(517+i*69);
            }
            if(currentPlayers.get(i) instanceof Messenger){
                playerBags.get(3).draw();
                playerBags.get(3).setLayoutX(124);
                playerBags.get(3).setLayoutY(517+i*69);
            }
            if(currentPlayers.get(i) instanceof Navigator){
                playerBags.get(4).draw();
                playerBags.get(4).setLayoutX(124);
                playerBags.get(4).setLayoutY(517+i*69);
            }
            if(currentPlayers.get(i) instanceof Pilot){
                playerBags.get(5).draw();
                playerBags.get(5).setLayoutX(124);
                playerBags.get(5).setLayoutY(517+i*69);
            }
        }
        drawAllTreasureCards();

        screenController.getExchangeCards().setDisable(true);
    }

    // Disable or enable the control
    public void setAllControlsDisabled(boolean disable) {
        for (Node node : mainBoard.getChildren()) {
            node.setDisable(disable);
        }
    }

    private void selectRandomTile() {
        if (tiles.isEmpty()) return; // If no tile, return

        Random random = new Random();

        // Based on the current stage value of WaterMeter, determine the number of submerged islands
        int floodCount = waterMeters.get(currentWaterMeterIndex).getStage();

        // Create a temporary list to store the tiles that have been selected
        List<Tile> selectedTiles = new ArrayList<>();

        for (int i = 0; i < floodCount; i++) {
            // Randomly choose a tile
            int randomIndex = random.nextInt(tiles.size());
            Tile selectedTile = tiles.get(randomIndex);

            // Increase state
            selectedTile.incrementState();

            if (selectedTile.getState() >= 2) {
                // If state >= 2, remove
                tiles.remove(selectedTile); // Remove from list
                mainBoard.getChildren().remove(selectedTile); // Remove from UI
            } else {
                selectedTile.draw(); // Redraw it
            }

            // Draw the image of the selected tile behind FloodDeck
            drawFloodedTileOnDeck(selectedTile);

            // Add the selected tiles to the temporary list and remove them from the original list to prevent duplicate selections
            selectedTiles.add(selectedTile);
            tiles.remove(selectedTile);
        }

        // Re-add the tiles in the temporary list back to the original list to restore the complete tile state
        tiles.addAll(selectedTiles);
        checkDefeat();
    }

    public void setStartFloodCards(){
        if (tiles.isEmpty()) return; // If no tile, return

        Random random = new Random();

        // Create a temporary list to store the tiles that have been selected
        List<Tile> selectedTiles = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            // Randomly choose a tile
            int randomIndex = random.nextInt(tiles.size());
            Tile selectedTile = tiles.get(randomIndex);

            // Increase state
            selectedTile.incrementState();

            selectedTile.draw(); // Redraw it

            // Draw the image of the selected tile behind FloodDeck
            drawFloodedTileOnDeck(selectedTile);

            // Add the selected tiles to the temporary list and remove them from the original list to prevent duplicate selections
            selectedTiles.add(selectedTile);
            tiles.remove(selectedTile);
        }

        // Re-add the tiles in the temporary list back to the original list to restore the complete tile state
        tiles.addAll(selectedTiles);
    }

    public static List<Player> getRandomPlayers(List<Player> players, int count) {
        Random random = new Random();
        List<Player> copyOfPlayers = new ArrayList<>(players); // Create a copy so as not to modify the original list
        List<Player> selectedPlayers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(copyOfPlayers.size()); // Randomly select an index from the list of copies
            selectedPlayers.add(copyOfPlayers.get(randomIndex)); // Add to the result list
            copyOfPlayers.remove(randomIndex); // Remove the extracted elements from the copy list to prevent duplication
        }

        return selectedPlayers;
    }

    // Enable the player move mode
    public void enableTileMovement(Player p) {
        // Traverse all the child nodes of the mainBoard
        for (Node node : mainBoard.getChildren()) {
            if (node instanceof Tile  tile) {
                tile.setOnMouseClicked(e -> {
                    if (!isMoveMode) return;

                    int targetX = (int) tile.getLayoutX();
                    int targetY = (int) tile.getLayoutY();

                    int currX = (int) p.getLayoutX()-30;
                    int currY = (int) p.getLayoutY();

                    int tileSize = 50;

                    boolean isAdjacent =
                            (Math.abs(targetX - currX) == tileSize && targetY == currY) ||
                                    (Math.abs(targetY - currY) == tileSize && targetX == currX);

                    if (isAdjacent && tile.getState() == 0) {
                        p.setLayoutX(targetX+30);
                        p.setX(targetX);
                        p.setLayoutY(targetY);
                        p.setY(targetY);
                        p.draw();
                        checkTreasureSubmit();

                        turnManage.useStep();
                        turnManage.showRemainSteps();
                        changeCurrentPlayer();

                        exchangeCards();
                        isVictory();

                        isMoveMode = false;
                        screenController.getMove().setText("Move");
                    }else if(tile.getState() == 1){
                        showMessage("Target tile is flooded!");
                    } else {
                        showMessage("Target tile is not adjacent!");
                    }
                });
            }
        }
    }

    private void drawFloodedTileOnDeck(Tile tile) {
        // Obtain the position of the flood deck
        double floodDeckX = screenController.getFloodDeck().getLayoutX();
        double floodDeckY = screenController.getFloodDeck().getLayoutY();
        double floodDeckWidth = 30;
        double floodDeckHeight = 69;

        // Calculate the offset position of the new image (shift it a certain distance to the right each time)
        int offset = 30; // The horizontal offset of each image
        int currentImageCount = floodedTileCanvases.size(); // Using floodedTileCanvases' size

        double newImageX = floodDeckX + offset * currentImageCount + 50;
        double newImageY = floodDeckY;

        // Create a new Canvas for drawing the image of the Tile
        Canvas floodedTileCanvas = new Canvas(floodDeckWidth, floodDeckHeight);
        floodedTileCanvas.setLayoutX(newImageX);
        floodedTileCanvas.setLayoutY(newImageY);

        GraphicsContext gc = floodedTileCanvas.getGraphicsContext2D();

        floodedTileCanvas.setUserData(tile.getTileName1());

        // Draw the correct image based on the status of Tile
        String tileImagePath = tile.getTileName1();
        gc.drawImage(new Image(getClass().getResourceAsStream(tileImagePath)), 0, 0, floodDeckWidth, floodDeckHeight);

        // Add the new Canvas to the main Pane (mainBoard)
        mainBoard.getChildren().add(floodedTileCanvas);

        // Add Canvas to the list
        floodedTileCanvases.add(floodedTileCanvas);
    }

    public void removeFloodedTileFromDeckByTile(Tile tile) {
        String tileImagePath = tile.getTileName1(); // Obtain the image path of the current Tile

        // Traverse the "floodedTileCanvases" list and find the Canvas corresponding to the Tile
        for (int i = 0; i < floodedTileCanvases.size(); i++) {
            Canvas canvas = floodedTileCanvases.get(i);

            // Use UserData to store the path of the image
            String canvasImagePath = (String) canvas.getUserData();

            if (canvasImagePath != null && canvasImagePath.equals(tileImagePath)) {
                // Remove the Canvas from the UI and the list
                mainBoard.getChildren().remove(canvas);
                floodedTileCanvases.remove(i);

                // Update the positions of the remaining pictures
                updateFloodDeckImagePositions();
                break; // Exit the loop immediately after finding
            }
        }
    }

    private void updateFloodDeckImagePositions() {
        double floodDeckX = screenController.getFloodDeck().getLayoutX();
        double floodDeckY = screenController.getFloodDeck().getLayoutY();
        int offset = 30;

        for (int i = 0; i < floodedTileCanvases.size(); i++) {
            Canvas canvas = floodedTileCanvases.get(i);
            double newImageX = floodDeckX + offset * i + 50;
            canvas.setLayoutX(newImageX);
            canvas.setLayoutY(floodDeckY);
        }
    }

    public void enableSaveMode() {
        // Add click events to all Tiles
        for (Tile tile : tiles) {
            tile.setOnMouseClicked(event -> handleTileSave(tile)); // Add mouse clicking event
        }
    }

    public void disableSaveMode() {
        // Ban click events to all Tiles
        for (Tile tile : tiles) {
            tile.setOnMouseClicked(null); // Ban mouse clicking event
        }
    }

    // The logic for tile clicking
    public void handleTileSave(Tile tile) {
        if (!isSaveMode) return; // If not in save mode, return

        int targetX = (int) tile.getLayoutX();
        int targetY = (int) tile.getLayoutY();

        int currX = (int) currentPlayer.getLayoutX()-30;
        int currY = (int) currentPlayer.getLayoutY();

        int tileSize = 50;

        boolean isAdjacentOrSame =
                (Math.abs(targetX - currX) == tileSize && targetY == currY) ||
                        (Math.abs(targetY - currY) == tileSize && targetX == currX) ||
                        (targetX == currX && targetY == currY);

        if (!isAdjacentOrSame) {
            showMessage("The island is too far to save!");
            isSaveMode = false;
            screenController.getSaveTheIsland().setText("Save the Island");
            disableSaveMode();
            return;
        }

        if (tile.getState() == 1) {
            tile.setState(0);
            tile.draw();

            // Delete the corresponding Tile picture
            removeFloodedTileFromDeckByTile(tile);

            showMessage("Island saved successfully!");
            turnManage.useStep();
            turnManage.showRemainSteps();
            changeCurrentPlayer();

            isSaveMode = false;
            screenController.getSaveTheIsland().setText("Save the Island");
            disableSaveMode();
        } else {
            showMessage("The island has either submerged or has not been flooded!");
            isSaveMode = false;
            screenController.getSaveTheIsland().setText("Save the Island");
            disableSaveMode();
        }
    }

    public void showMessage(String message) {
        if (messageLabel == null) {
            messageLabel = new Label();
            messageLabel.setLayoutX(409.0);
            messageLabel.setLayoutY(70.0);
            messageLabel.setWrapText(true);

            mainBoard.getChildren().add(messageLabel);
        }

        messageLabel.setText(message);

        // Adjust the width and height dynamically
        messageLabel.setPrefWidth(200.0); // Set maximum width
        messageLabel.setPrefHeight(Region.USE_COMPUTED_SIZE); // Automatically calculate the height based on the content

        messageLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> messageLabel.setVisible(false));
        pause.play();
    }

    // The logic for handling the turn for each player
    private void handleTurnOver() {
        turnManage.setStep(0);
        turnManage.showRemainSteps();
        changeCurrentPlayer();
        drawAllTreasureCards();
    }

    public void sentSpecialCards(List<TreasureCard> playerBag){
        // Distribute the treasure cards based on the probability
        List<TreasureCard> availableCards = new ArrayList<>();

        // Each treasure card is added to the "availableCards" list according to its probability
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(0)); // soil
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(5)); // cloud
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(10)); // water
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(15)); // fire
        for (int i = 0; i < 3; i++) availableCards.add(new TreasureCard(20)); // helicopter
        for (int i = 0; i < 3; i++) availableCards.add(new TreasureCard(26)); // waterRise
        for (int i = 0; i < 2; i++) availableCards.add(new TreasureCard(23)); // sandbags

        // Randomly draw two treasure cards
        Random random = new Random();
        TreasureCard card1 = availableCards.get(random.nextInt(availableCards.size()));
        TreasureCard card2 = availableCards.get(random.nextInt(availableCards.size()));

        // Check if waterRise card is drawn
        if (card1.getCardType() == 26 ) {
            updateWaterMeter();
        }else{
            playerBag.add(card1);
        }

        if(card2.getCardType() == 26){
            updateWaterMeter();
        }else{
            playerBag.add(card2);
        }
        drawAllTreasureCards();

        if (playerBag.size() > 5) {
            x=0;
            promptDiscardCards(currentBag);  // Show discard panel
        }

    }

    public void sentSpecialCardsWithoutWaterRise(List<TreasureCard> playerBag){
        // Distribute the treasure cards based on the probability
        List<TreasureCard> availableCards = new ArrayList<>();

        // Each treasure card is added to the "availableCards" list according to its probability
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(0)); // soil
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(5)); // cloud
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(10)); // water
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(15)); // fire
        for (int i = 0; i < 3; i++) availableCards.add(new TreasureCard(20)); // helicopter
        for (int i = 0; i < 2; i++) availableCards.add(new TreasureCard(23)); // sandbags

        // Randomly draw two treasure cards
        Random random = new Random();
        TreasureCard card1 = availableCards.get(random.nextInt(availableCards.size()));
        TreasureCard card2 = availableCards.get(random.nextInt(availableCards.size()));

        playerBag.add(card1);

        playerBag.add(card2);
    }


    public void promptDiscardCards(List<TreasureCard> bag) {
        showMessage("You have too many cards! Please discard until only 5 remain.");

        // Ban all the other controls
        setAllControlsDisabled(true);

        double centerX = mainBoard.getWidth() / 2;
        double discardAreaY = screenController.getDiverBag().getLayoutY() - 150;

        double cardWidth = 80;
        double cardHeight = 120;
        int offset = 90;

        if(x<1){
            for (int i = 0; i < bag.size(); i++) {
                TreasureCard card = bag.get(i);
                double x = centerX - (bag.size() * offset) / 2 + offset * i;
                double y = discardAreaY;

                Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
                cardCanvas.setLayoutX(x);
                cardCanvas.setLayoutY(y);
                cardCanvas.setUserData("discard");

                GraphicsContext gc = cardCanvas.getGraphicsContext2D();
                gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

                // Every card can be clicked to discard
                int index = i;
                cardCanvas.setOnMouseClicked(e -> {
                    confirmDiscard(index);
                });
                mainBoard.getChildren().add(cardCanvas);
            }
        }else{
            int t = 0;
            for (int i = 0; i < currentPlayers.size(); i++) {
                if (currentPlayers.get(i).equals(currentPlayer)) {
                    t = turnManage.getIndex1(i, currentPlayers);
                }
            }
            List<TreasureCard> tr = currentBags.get(t);
            for (int i = 0; i < tr.size(); i++) {
                TreasureCard card = tr.get(i);
                double x = centerX - (tr.size() * offset) / 2 + offset * i;
                double y = discardAreaY;

                Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
                cardCanvas.setLayoutX(x);
                cardCanvas.setLayoutY(y);
                cardCanvas.setUserData("discard");

                GraphicsContext gc = cardCanvas.getGraphicsContext2D();
                gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

                // Every card can be clicked to discard
                int index = i;
                cardCanvas.setOnMouseClicked(e -> {
                    confirmDiscard(index);
                });
                mainBoard.getChildren().add(cardCanvas);
            }
            x = 0;
        }

    }

    private void confirmDiscard(int index) {
        x++;
        int t = 0;
        for (int i = 0; i < currentPlayers.size(); i++) {
            if (currentPlayers.get(i).equals(currentPlayer)) {
                t = turnManage.getIndex1(i, currentPlayers);
            }
        }

        currentBags.get(t).remove(index);

        // Clear the discard card UI
        mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));

        drawAllTreasureCards();

        if (currentBags.get(t).size() > 5) {
            promptDiscardCards(currentBag);  // Keep discarding
        } else {
            showMessage("You now have 5 cards or fewer.");

            // After discard is done, enable all other controls
            setAllControlsDisabled(false);
        }
    }


    public void drawAllTreasureCards() {
        // Firstly remove the old card Canvas
        mainBoard.getChildren().removeIf(node -> node instanceof Canvas && "treasure".equals(node.getUserData()));

        double cardWidth = 50;
        double cardHeight = 69;
        int offset = 50;

        for(int i=0;i<currentPlayers.size();i++){
            int index = connectPlayerToBag(currentPlayers.get(i));
            double currentBagX = playerBags.get(index).getLayoutX();
            double currentBagY = playerBags.get(index).getLayoutY();
            for(int j=0;j<currentBags.get(i).size();j++){
                TreasureCard card = (TreasureCard) currentBags.get(i).get(j);
                double x = currentBagX + offset * j + 50;
                double y = currentBagY;

                Card cardCanvas = new Card();
                cardCanvas.setLayoutX(x);
                cardCanvas.setLayoutY(y);
                cardCanvas.setUserData("treasure");  // Marking that can be easily removed
                GraphicsContext gc = cardCanvas.getGraphicsContext2D();
                gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);
                mainBoard.getChildren().add(cardCanvas);
            }
        }
    }

    private void updateWaterMeter() {
        if (currentWaterMeterIndex < waterMeters.size() - 1) {
            // Remove the current WaterMeter from the main pane
            mainBoard.getChildren().remove(waterMeters.get(currentWaterMeterIndex));

            // Update the index to the next WaterMeter
            currentWaterMeterIndex++;

            // Add a new WaterMeter to the main Pane
            WaterMeter nextWaterMeter = waterMeters.get(currentWaterMeterIndex);
            nextWaterMeter.draw();
            mainBoard.getChildren().add(nextWaterMeter);

            showMessage("Water level has risen!");
        } else {
            checkDefeat();
        }
    }

    private List<TreasureCard> getSpecialCards() {
        List<TreasureCard> specialCards = new ArrayList<>();
        for(TreasureCard card : currentBag){
            if(card.getType() == SANDBAGS){
                specialCards.add(card);
            }
            if(card.getType() == HELICOPTER){
                specialCards.add(card);
            }
        }
        return specialCards;
    }

    private void useSpecialCards() {
        if (getSpecialCards().size() == 0) {
            showMessage("You don't have any special cards!");
        } else {
            showMessage("Choose the special card you want to use:");

            double centerX = mainBoard.getWidth() / 2;
            double discardAreaY = screenController.getDiverBag().getLayoutY() - 150;

            double cardWidth = 80;
            double cardHeight = 120;
            int offset = 90;

            // Create a list to store the currently added UI elements, so that they can be cleared later
            List<Node> tempUIElements = new ArrayList<>();

            for (int i = 0; i < getSpecialCards().size(); i++) {
                TreasureCard card = getSpecialCards().get(i);
                double x = 700;
                double y = discardAreaY - (currentBag.size() * offset) / 2 + offset * i - 30;

                Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
                cardCanvas.setLayoutX(x);
                cardCanvas.setLayoutY(y);
                cardCanvas.setUserData("usecard");

                GraphicsContext gc = cardCanvas.getGraphicsContext2D();
                gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

                int index = i;
                cardCanvas.setOnMouseClicked(e -> {
                    useThisCard(index);
                    // Clean the temporary UI elements
                    clearTempUIElements(tempUIElements);
                });

                mainBoard.getChildren().add(cardCanvas);
                tempUIElements.add(cardCanvas); // Save to the temporary UI list
            }

            // Create the "Cancel" button
            Button cancelButton = new Button("Cancel");
            cancelButton.setLayoutX(centerX - 50); // The centre of the button
            cancelButton.setLayoutY(discardAreaY + 200); // Adjusting the button's position
            cancelButton.setOnAction(e -> {
                // Clean the temporary UI elements
                clearTempUIElements(tempUIElements);
            });

            mainBoard.getChildren().add(cancelButton);
            tempUIElements.add(cancelButton); // Save to the temporary UI list
        }
    }

    private void clearTempUIElements(List<Node> tempUIElements) {
        for (Node node : tempUIElements) {
            mainBoard.getChildren().remove(node);
        }
        tempUIElements.clear(); // Clear the list to avoid repeated cleaning
    }

    private void useThisCard(int index){
            List<TreasureCard> specialCards = getSpecialCards();
            if (specialCards.get(index).getType() == SANDBAGS) {
                for (Tile tile : tiles) {
                    tile.setOnMouseClicked(event -> {
                        saveBySandbags(tile);
                    });
                }
            }
            if (specialCards.get(index).getType() == HELICOPTER) {
                for (Tile tile : tiles) {
                    tile.setOnMouseClicked(event -> {
                        moveByHelicopter(tile);
                    });
                }
            }
    }

    private void saveBySandbags(Tile tile) {

        if (tile.getState() == 1) {
            tile.setState(0);
            tile.draw();

            // Delete the corresponding Tile picture
            removeFloodedTileFromDeckByTile(tile);

            showMessage("Island saved successfully!");
            isSaveMode = false;
            disableSaveMode();

            for(TreasureCard card : currentBag){
                if(card.getType() == SANDBAGS){
                    currentBag.remove(card);
                    break;
                }
            }
            drawAllTreasureCards();
            mainBoard.getChildren().removeIf(node -> "usecard".equals(node.getUserData()));
        } else {
            showMessage("The island has either submerged or has not been flooded!");
            isSaveMode = false;
            disableSaveMode();
        }
    }

    private void moveByHelicopter(Tile tile) {
        if(tile.getState() == 0) {
            int targetX = (int) tile.getLayoutX();
            int targetY = (int) tile.getLayoutY();

            for (TreasureCard card : currentBag) {
                if (card.getType() == HELICOPTER) {
                    currentBag.remove(card);
                    break;
                }
            }
            mainBoard.getChildren().removeIf(node -> "usecard".equals(node.getUserData()));
            drawAllTreasureCards();

            currentPlayer.setLayoutX(targetX + 30);
            currentPlayer.setX(targetX);
            currentPlayer.setLayoutY(targetY);
            currentPlayer.setY(targetY);
            currentPlayer.draw();

            exchangeCards();
            checkTreasureSubmit();
            isVictory();
        }else{
            showMessage("This tile is already flood!");
        }
    }

    public Tile getTileByPlayer(Player p) {
        int currentX = p.getX();
        int currentY = p.getY();
        Tile t = new Tile(0,0,0);
        for (Tile tile : tiles) {
            if (tile.getPositionX() == currentX && tile.getPositionY() == currentY) {
                t = tile;
            }
        }
        return t;
    }


    public void checkTreasureSubmit() {
        step = turnManage.getStep();
        if (step == 1) {
            return;
        }

        int index = 0;
        Player p = currentPlayer;
            Tile currentTile = getTileByPlayer(p);
            if (currentTile.getName().equals("1") || currentTile.getName().equals("2")) {
                for (TreasureCard card : p.getBag()) {
                    if (card.getType().equals(SOIL)) {
                        index++;
                    }
                }
                if (index >= 4) {
                    showMessage("You have submitted the treasure!");
                    int j = 0;
                    List<TreasureCard> cardsToRemove = new ArrayList<>();
                    for (TreasureCard card : p.getBag()) {
                        if (card.getType() == SOIL && j < 4) {
                            cardsToRemove.add(card);
                            j++;
                        }
                    }
                    p.getBag().removeAll(cardsToRemove);
                    drawAllTreasureCards();
                    treasures.get(1).draw();
                    treasures.get(1).setLayoutX(551);
                    treasures.get(1).setLayoutY(540);
                    isGetSOIL = true;

                    turnManage.useStep();
                    turnManage.showRemainSteps();
                    changeCurrentPlayer();
                }
            }
            if (currentTile.getName().equals("3") || currentTile.getName().equals("4")) {
                for (TreasureCard card : p.getBag()) {
                    if (card.getType().equals(CLOUD)) {
                        index++;
                    }
                }
                if (index >= 4) {
                    showMessage("You have submitted the treasure!");
                    int j = 0;
                    List<TreasureCard> cardsToRemove = new ArrayList<>();
                    for (TreasureCard card : p.getBag()) {
                        if (card.getType() == CLOUD && j < 4) {
                            cardsToRemove.add(card);
                            j++;
                        }
                    }
                    p.getBag().removeAll(cardsToRemove);
                    drawAllTreasureCards();
                    treasures.get(0).draw();
                    treasures.get(0).setLayoutX(551);
                    treasures.get(0).setLayoutY(590);
                    isGetCLOUD = true;

                    turnManage.useStep();
                    turnManage.showRemainSteps();
                    changeCurrentPlayer();
                }
            }
            if (currentTile.getName().equals("7") || currentTile.getName().equals("8")) {
                for (TreasureCard card : p.getBag()) {
                    if (card.getType().equals(WATER)) {
                        index++;
                    }
                }
                if (index >= 4) {
                    showMessage("You have submitted the treasure!");
                    int j = 0;
                    List<TreasureCard> cardsToRemove = new ArrayList<>();
                    for (TreasureCard card : p.getBag()) {
                        if (card.getType() == WATER && j < 4) {
                            cardsToRemove.add(card);
                            j++;
                        }
                    }
                    p.getBag().removeAll(cardsToRemove);
                    drawAllTreasureCards();
                    treasures.get(3).draw();
                    treasures.get(3).setLayoutX(551);
                    treasures.get(3).setLayoutY(640);
                    isGetWATER = true;

                    turnManage.useStep();
                    turnManage.showRemainSteps();
                    changeCurrentPlayer();
                }
            }
            if (currentTile.getName().equals("5") || currentTile.getName().equals("6")) {
                for (TreasureCard card : p.getBag()) {
                    if (card.getType().equals(FIRE)) {
                        index++;
                    }
                }
                if (index >= 4) {
                    showMessage("You have submitted the treasure!");
                    int j = 0;
                    List<TreasureCard> cardsToRemove = new ArrayList<>();
                    for (TreasureCard card : p.getBag()) {
                        if (card.getType() == FIRE && j < 4) {
                            cardsToRemove.add(card);
                            j++;
                        }
                    }
                    p.getBag().removeAll(cardsToRemove);
                    drawAllTreasureCards();
                    treasures.get(2).draw();
                    treasures.get(2).setLayoutX(551);
                    treasures.get(2).setLayoutY(690);
                    isGetFIRE = true;

                    turnManage.useStep();
                    turnManage.showRemainSteps();
                    changeCurrentPlayer();
                }
            }
    }


    public void promptDiscardCardsByExchange(List<TreasureCard> bag) {
        showMessage("You have too many cards! Please discard until only 5 remain.");

        // Ban all the other controls
        setAllControlsDisabled(true);

        double centerX = mainBoard.getWidth() / 2;
        double discardAreaY = screenController.getDiverBag().getLayoutY() - 150;

        double cardWidth = 80;
        double cardHeight = 120;
        int offset = 90;

        for (int i = 0; i < bag.size(); i++) {
            TreasureCard card = bag.get(i);
            double x = centerX - (bag.size() * offset) / 2 + offset * i;
            double y = discardAreaY;

            Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
            cardCanvas.setLayoutX(x);
            cardCanvas.setLayoutY(y);
            cardCanvas.setUserData("discard");

            GraphicsContext gc = cardCanvas.getGraphicsContext2D();
            gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

            // Every card can be clicked to discard
            int index = i;
            cardCanvas.setOnMouseClicked(e -> {
                confirmDiscard(index);
                turnManage.useStep();
                turnManage.showRemainSteps();
                changeCurrentPlayer();
            });
            mainBoard.getChildren().add(cardCanvas);
        }
    }
    public List<Tile> getTiles(){
        return tiles;
    }

    public int connectPlayerToBag(Player player){
        int index=0;
        PlayerBag.playerType type = player.getType();
        if(type.equals(PlayerBag.playerType.Diver)){
            index = 0;
        }
        if(type.equals(PlayerBag.playerType.Engineer)){
            index = 1;
        }
        if(type.equals(PlayerBag.playerType.Explorer)){
            index = 2;
        }
        if(type.equals(PlayerBag.playerType.Messenger)){
            index = 3;
        }
        if(type.equals(PlayerBag.playerType.Navigator)){
            index = 4;
        }
        if(type.equals(PlayerBag.playerType.Pilot)){
            index = 5;
        }
        return index;
    }

    public boolean checkIfHaveSamePosition(Player player){
        boolean b = false;
        for(Player p :currentPlayers){
            if(!p.equals(currentPlayer) && getTileByPlayer(p).equals(getTileByPlayer(player))){
                b = true;
            }
        }
        return b;
    }

    private List<Player> findSamePositionPlayers(Player player){
        List<Player> sameTilePlayers = new ArrayList<>();

        for(Player p :currentPlayers){
            if(!p.equals(currentPlayer) && getTileByPlayer(p).equals(getTileByPlayer(player))){
                sameTilePlayers.add(p);
            }
        }
        return sameTilePlayers;
    }

    public List<TreasureCard> selectTreasureCards(List<TreasureCard> bag){
        List<TreasureCard> selectedCards = new ArrayList<>();
        for(TreasureCard card : bag){
            if(!card.getType().equals(HELICOPTER) && !card.getType().equals(SANDBAGS)){
                selectedCards.add(card);
            }
        }
        return selectedCards;
    }


    private void promptExchangeCards() {
        showMessage("Choose the card you want to send!");

        List<TreasureCard> bag = selectTreasureCards(currentBag);
        setAllControlsDisabled(true); // Ban all the other controls

        double centerX = mainBoard.getWidth() / 2;
        double discardAreaY = screenController.getDiverBag().getLayoutY() - 150;

        double cardWidth = 80;
        double cardHeight = 120;
        int offset = 90;

        // Save all the nodes added to the UI for subsequent cleanup
        List<Node> tempUIElements = new ArrayList<>();

        for (int i = 0; i < bag.size(); i++) {
            TreasureCard card = bag.get(i);
            double x = centerX - (bag.size() * offset) / 2 + offset * i;
            double y = discardAreaY;

            Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
            cardCanvas.setLayoutX(x);
            cardCanvas.setLayoutY(y);
            cardCanvas.setUserData("discard");

            GraphicsContext gc = cardCanvas.getGraphicsContext2D();
            gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

            int index = i;
            cardCanvas.setOnMouseClicked(e -> {
                giveCards(index);

                // Clean up the temporary UI elements
                clearTempUIElements(tempUIElements);
            });

            mainBoard.getChildren().add(cardCanvas);
            tempUIElements.add(cardCanvas);
        }

        // Add the "Cancel" button
        Button cancelButton = new Button("Cancel");
        cancelButton.setLayoutX(centerX - 40);
        cancelButton.setLayoutY(discardAreaY + 150); // Put under the card

        cancelButton.setOnAction(e -> {
            clearTempUIElements(tempUIElements);
            setAllControlsDisabled(false); // Restore control
            showMessage("Action canceled.");
        });

        mainBoard.getChildren().add(cancelButton);
        tempUIElements.add(cancelButton);
    }



    private void giveCards(int index) {
        // Clear discard card UI
        mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));

        List<Player> sameTilePlayers = findSamePositionPlayers(currentPlayer);
        List<PlayerBag> samePlayerBags = new ArrayList<>();
        for (PlayerBag pb : playerBags) {
            for (Player p : sameTilePlayers) {
                if (p.getType().equals(pb.getPlayerType())) {
                    pb.setDisable(false);
                    samePlayerBags.add(pb);
                }
            }
        }

        for (PlayerBag pb : samePlayerBags) {
            pb.setOnMouseClicked(e -> {
                for (Player p : sameTilePlayers) {
                    if (p.getType().equals(pb.getPlayerType())) {
                        p.getBag().add(selectTreasureCards(currentBag).get(index));
                    }
                    if (p.getBag().size() > 5) {
                        promptDiscardCardsByExchange(p.getBag());
                    }else{
                        turnManage.useStep();
                        turnManage.showRemainSteps();
                        changeCurrentPlayer();
                    }
                }
                currentBag.remove(selectTreasureCards(currentBag).get(index));

                setAllControlsDisabled(false);
                pb.setDisable(true);
                drawAllTreasureCards();
            });
        }
    }

    public void changeCurrentPlayer(){
        step = turnManage.getStep();
        if(step==0) {
            sentSpecialCards(currentBag);
            selectRandomTile();
            mainBoard.getChildren().remove(currentPlayer1);
            for (int i = 0; i < currentPlayers.size(); i++) {
                if (currentPlayers.get(i).equals(currentPlayer)) {
                    currentPlayer = currentPlayers.get(turnManage.getIndex(i, currentPlayers));
                    for (Player p : players1) {
                        if (p.getType().equals(currentPlayer.getType())) {
                            currentPlayer1 = p;
                            mainBoard.getChildren().add(currentPlayer1);
                            currentPlayer1.draw();
                            currentPlayer1.setLayoutX(155);
                            currentPlayer1.setLayoutY(30);
                        }
                    }
                    currentBag = currentBags.get(turnManage.getIndex(i, currentPlayers));
                    break;
                }
            }
            setCurrentIndex();
            checkTreasureSubmit();
            mainBoard.getChildren().removeIf(node -> "usecard".equals(node.getUserData()));
        }
        screenController.getUseSpecialSkill().setDisable(false);
    }

    public void exchangeCards(){
        if(checkIfHaveSamePosition(currentPlayer)){
            screenController.getExchangeCards().setDisable(false);
            screenController.getExchangeCards().setOnAction(e1 -> {
                if(selectTreasureCards(currentBag).size()==0){
                    showMessage("You have no treasure cards to exchange");
                }else {
                    promptExchangeCards();
                }
            });
        }else{
            screenController.getExchangeCards().setDisable(true);
        }
    }

    public void useSpecialSkill(){
        currentPlayer.useSpecialAbility(this,currentPlayer);
    }

    public void setCurrentIndex(){
        diverCount = 0;
        explorerCount = 0;
        engineerCount = 0;
        messengerCount = 0;
        navigatorCount = 0;
        pilotCount = 0;
    }

    public boolean isGetSoil(){
        return isGetSOIL;
    }
    public boolean isGetFire(){
        return isGetFIRE;
    }
    public boolean isGetWater(){
        return isGetWATER;
    }
    public boolean isGetCloud(){
        return isGetCLOUD;
    }

    public boolean allAtFoolLanding(){
        int index = 0;
        Tile foolLanding = null;
        for(Tile t: tiles){
            if(t.getName().equals("14")){
                foolLanding = t;
            }
        }
        if (foolLanding == null) {
            return false;
        }
        for(Player p : currentPlayers){
            if(p.getX() == foolLanding.getPositionX() && p.getY() == foolLanding.getPositionY()){
                index++;
            }
        }
        return(index == currentPlayers.size());
    }

    public boolean isHaveHelicopter(){
        boolean b = false;
        List<TreasureCard> treasureCards = null;
        for(Player p: currentPlayers){
            for(TreasureCard t :p.getBag()){
                if(t.getType().equals(HELICOPTER)){
                    b = true;
                }
            }
        }
        return b;
    }


    public void isVictory(){
        if(isGetSoil()&&isGetFire()&&isGetWater()&&isGetCloud() && allAtFoolLanding() && isHaveHelicopter()){
            showMessage("You win!");
            screenController.setWin();
        }
    }

    public boolean isDefeat(){
        boolean isDefeat = false;
        for(Tile t : tiles){
            if(t.getState() > 1){
                for(Player p : currentPlayers){
                    if(p.getX() == t.getPositionX() && p.getY() == t.getPositionY()){
                        isDefeat = true;
                    }
                }
            }

            if(t.getState() > 1 && t.getName().equals("10")){
                isDefeat = true;
            }
        }

        if(currentWaterMeterIndex > 9){
            isDefeat = true;
        }

        int indexSoil=0;
        int indexCloud=0;
        int indexFire=0;
        int indexWater=0;

        for(Tile t : tiles){

            if (t.getName().equals("1") && t.getState() > 1 ){
                indexSoil++;
            }
            if (t.getName().equals("2") && t.getState() > 1 ){
                indexSoil++;
            }
            if (t.getName().equals("3") && t.getState() > 1 ){
                indexCloud++;
            }
            if (t.getName().equals("4") && t.getState() > 1 ){
                indexCloud++;
            }
            if (t.getName().equals("5") && t.getState() > 1 ){
                indexFire++;
            }
            if (t.getName().equals("6") && t.getState() > 1 ){
                indexFire++;
            }
            if (t.getName().equals("7") && t.getState() > 1 ){
                indexWater++;
            }
            if (t.getName().equals("8") && t.getState() > 1 ){
                indexWater++;
            }
        }

        if(indexSoil ==2 && !isGetSoil()){
            isDefeat = true;
        }
        if(indexCloud ==2 && !isGetCloud()){
            isDefeat = true;
        }
        if(indexFire ==2 && !isGetFire()){
            isDefeat = true;
        }
        if(indexWater ==2 && !isGetWater()) {
            isDefeat = true;
        }

        return isDefeat;
    }

    public void checkDefeat(){
        if(isDefeat()){
            String reason = getDefeatReason();
            showMessage("You lose! " + reason);
            screenController.setGameOver(reason);
        }
    }

    private String getDefeatReason() {
        if (currentWaterMeterIndex > 9) {
            return "The water level has risen too high!";
        }

        for (Tile t : tiles) {
            if (t.getState() > 1) {
                for (Player p : currentPlayers) {
                    if (p.getX() == t.getPositionX() && p.getY() == t.getPositionY()) {
                        return "A player was on a tile that sank with no adjacent tiles!";
                    }
                }
            }

            if (t.getState() > 1 && t.getName().equals("10")) {
                return "Fool's Landing has sunk!";
            }
        }

        int indexSoil = 0, indexCloud = 0, indexFire = 0, indexWater = 0;
        for (Tile t : tiles) {
            if (t.getName().equals("1") && t.getState() > 1) indexSoil++;
            if (t.getName().equals("2") && t.getState() > 1) indexSoil++;
            if (t.getName().equals("3") && t.getState() > 1) indexCloud++;
            if (t.getName().equals("4") && t.getState() > 1) indexCloud++;
            if (t.getName().equals("5") && t.getState() > 1) indexFire++;
            if (t.getName().equals("6") && t.getState() > 1) indexFire++;
            if (t.getName().equals("7") && t.getState() > 1) indexWater++;
            if (t.getName().equals("8") && t.getState() > 1) indexWater++;
        }

        if (indexSoil == 2 && !isGetSoil()) {
            return "Both Earth treasure tiles sank before the treasure was collected!";
        }
        if (indexCloud == 2 && !isGetCloud()) {
            return "Both Wind treasure tiles sank before the treasure was collected!";
        }
        if (indexFire == 2 && !isGetFire()) {
            return "Both Fire treasure tiles sank before the treasure was collected!";
        }
        if (indexWater == 2 && !isGetWater()) {
            return "Both Water treasure tiles sank before the treasure was collected!";
        }
        return "Game Over";
    }
}