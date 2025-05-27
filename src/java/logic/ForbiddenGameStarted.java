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
    private int[] random1= new int[24];
    private List<Tile> tiles = new ArrayList<>();
    private List<TreasureCard> treasureCards = new ArrayList<>();
    private List<WaterMeter> waterMeters = new ArrayList<>();
    private boolean isSaveMode = false; // 是否进入 "save the island" 模式
    private Label messageLabel;
    private List<Canvas> floodedTileCanvases = new ArrayList<>(); // 用于保存 FloodDeck 后面的所有图片
    public List<TreasureCard> currentBag = new ArrayList<>();
    private List<List> currentBags = new ArrayList<>();
    private List<Treasure> treasures = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<Player> players1 = new ArrayList<>();
    public List<Player> currentPlayers = new ArrayList<>();
    public List<PlayerBag> playerBags = new ArrayList<>();
    private int currentWaterMeterIndex = 1;
    private Button useSpecialCardButton;  // 用于触发使用特殊牌的按钮
    private Diver diver,diver1;
    private Engineer engineer,engineer1;
    private Explorer explorer,explorer1;
    private Messenger messenger,messenger1;
    private Navigator navigator,navigator1;
    private Pilot pilot,pilot1;
    public Player currentPlayer;
    private Player currentPlayer1;
    public TurnManage turnManage;
    public int step;
    private int playerCount; // 玩家数量
    private boolean b = false;
    private int x;
    public int pilotCount,diverCount,engineerCount,explorerCount,messengerCount,navigatorCount;
    public boolean isGetSOIL,isGetFIRE,isGetCLOUD,isGetWATER;

    public ForbiddenGameStarted(ScreenController screenController, int playerCount, int initialWaterMeterIndex) {
        // 初始化random1数组
        random1 = new int[24];
        List<Integer> numbers = new ArrayList<>();
        this.playerCount = playerCount;

        // 填充1到24的数字
        for (int i = 1; i <= 24; i++) {
            numbers.add(i);
        }

        // 打乱数字的顺序
        Collections.shuffle(numbers);

        // 将打乱后的数字填充到random1数组
        for (int i = 0; i < random1.length; i++) {
            random1[i] = numbers.get(i);
        }

        this.screenController = screenController;
        mainBoard = screenController.getMainBoard();
        initializeGame();

        // 为move按钮添加点击事件处理程序
        screenController.getMove().setOnAction(event -> {
            isMoveMode = !isMoveMode;  // 切换移动模式
            if (isMoveMode) {
                screenController.getMove().setText("Cancel Move");
                enableTileMovement(currentPlayer);  // 启动每次点击时可以移动棋子的模式
            } else {
                screenController.getMove().setText("Move");
            }
        });


        screenController.getSaveTheIsland().setOnAction(event -> {
            isSaveMode = !isSaveMode; // 切换保存模式
            if (isSaveMode) {
                screenController.getSaveTheIsland().setText("Cancel Save");
                enableSaveMode(); // 启动保存模式
            } else {
                screenController.getSaveTheIsland().setText("Save the Island");
                disableSaveMode(); // 取消保存模式
            }
        });


        screenController.getTurnOver().setOnAction(event -> {
            handleTurnOver();
            screenController.getUseSpecialSkill().setDisable(false);
        });




        screenController.getUseSpecialCardButton().setOnAction(event -> {
            useSpecialCards(); // 触发特殊牌使用逻辑
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
        // 将所有Tile对象添加到tiles列表中
        Collections.addAll(tiles, tile1, tile2, tile3, tile4, tile5, tile24, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tile14, tile15, tile16, tile17, tile18, tile19, tile20, tile21, tile22, tile23);


        TreasureCard soil = new TreasureCard(0);
        TreasureCard cloud = new TreasureCard(5);
        TreasureCard water = new TreasureCard(10);
        TreasureCard fire = new TreasureCard(15);
        TreasureCard helicopter = new TreasureCard(20);
        TreasureCard sandbags = new TreasureCard(23);
        TreasureCard waterrise = new TreasureCard(26);
        Collections.addAll(treasureCards,soil,cloud, water, fire, helicopter, sandbags, waterrise);

        mainBoard.getChildren().addAll(tiles);
        for (Tile tile : tiles) {
            tile.draw();
        }

        Treasure treasure1 = new Treasure(1,45,127);
        Treasure treasure2 = new Treasure(2,45,217);
        Treasure treasure3 = new Treasure(3,45,307);
        Treasure treasure4 = new Treasure(4,45,397);

        Collections.addAll(treasures, treasure1, treasure2, treasure3, treasure4);
        mainBoard.getChildren().addAll(treasures);

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

        Collections.addAll(players,diver, engineer, explorer, messenger, navigator, pilot);
        Collections.addAll(players1,diver1, engineer1, explorer1, messenger1, navigator1, pilot1);
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

    public void setAllControlsDisabled(boolean disable) {
        for (Node node : mainBoard.getChildren()) {
            node.setDisable(disable); // 禁用或启用控件
        }
    }



    private void selectRandomTile() {
        if (tiles.isEmpty()) return; // 如果没有岛屿，直接返回

        Random random = new Random();

        // 根据当前 WaterMeter 的 stage 值，决定淹没的岛屿数量
        int floodCount = waterMeters.get(currentWaterMeterIndex).getStage();

        // 创建一个临时列表，用于存储已经选中的岛屿
        List<Tile> selectedTiles = new ArrayList<>();

        for (int i = 0; i < floodCount; i++) {
            // 随机选择一个岛屿
            int randomIndex = random.nextInt(tiles.size());
            Tile selectedTile = tiles.get(randomIndex);

            // 增加状态值
            selectedTile.incrementState();

            if (selectedTile.getState() >= 2) {
                // 如果 state >= 2，从列表和 UI 中移除
                tiles.remove(selectedTile); // 从列表中移除
                mainBoard.getChildren().remove(selectedTile); // 从 UI 中移除
            } else {
                selectedTile.draw(); // 重新绘制
            }

            // 在 FloodDeck 后面绘制选中的 Tile 的图片
            drawFloodedTileOnDeck(selectedTile);

            // 将选中的岛屿加入临时列表，并从原列表中移除，避免重复选择
            selectedTiles.add(selectedTile);
            tiles.remove(selectedTile);
        }

        // 将临时列表中的岛屿重新加入原列表，以恢复完整的岛屿状态
        tiles.addAll(selectedTiles);
        checkDefeat();
    }


    public static List<Player> getRandomPlayers(List<Player> players, int count) {

        Random random = new Random();
        List<Player> copyOfPlayers = new ArrayList<>(players); // 创建一个副本，以便不修改原始列表
        List<Player> selectedPlayers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(copyOfPlayers.size()); // 从副本列表中随机选择一个索引
            selectedPlayers.add(copyOfPlayers.get(randomIndex)); // 添加到结果列表
            copyOfPlayers.remove(randomIndex); // 从副本列表中移除已抽取的元素，防止重复
        }

        return selectedPlayers;
    }



    public void enableTileMovement(Player p) {
        // 遍历 mainBoard 的所有子节点
        for (Node node : mainBoard.getChildren()) {
            if (node instanceof Tile  tile) {
                tile.setOnMouseClicked(e -> {
                    if (!isMoveMode) return;

//                    boolean validMove = false;

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
        // 获取 FloodDeck 的位置
        double floodDeckX = screenController.getFloodDeck().getLayoutX();
        double floodDeckY = screenController.getFloodDeck().getLayoutY();
        double floodDeckWidth = 30;
        double floodDeckHeight = 69;

        // 计算新图片的偏移位置（每次向右偏移一定距离）
        int offset = 30; // 每张图片的水平偏移量
        int currentImageCount = floodedTileCanvases.size(); // 使用 floodedTileCanvases 的大小

        double newImageX = floodDeckX + offset * currentImageCount + 50;
        double newImageY = floodDeckY;

        // 创建一个新的 Canvas，用于绘制 Tile 的图片
        Canvas floodedTileCanvas = new Canvas(floodDeckWidth, floodDeckHeight);
        floodedTileCanvas.setLayoutX(newImageX);
        floodedTileCanvas.setLayoutY(newImageY);

        GraphicsContext gc = floodedTileCanvas.getGraphicsContext2D();

        floodedTileCanvas.setUserData(tile.getTileName1());

        // 根据 Tile 的状态绘制正确的图片
        String tileImagePath = tile.getTileName1();
        gc.drawImage(new Image(getClass().getResourceAsStream(tileImagePath)), 0, 0, floodDeckWidth, floodDeckHeight);

        // 将新的 Canvas 添加到主 Pane（mainBoard）中
        mainBoard.getChildren().add(floodedTileCanvas);

        // 将 Canvas 添加到列表中
        floodedTileCanvases.add(floodedTileCanvas);
    }

    public void removeFloodedTileFromDeckByTile(Tile tile) {
        String tileImagePath = tile.getTileName1(); // 获取当前 Tile 的图片路径

        // 遍历 floodedTileCanvases 列表，找到与 Tile 对应的 Canvas
        for (int i = 0; i < floodedTileCanvases.size(); i++) {
            Canvas canvas = floodedTileCanvases.get(i);

            // 使用 UserData 存储图片路径
            String canvasImagePath = (String) canvas.getUserData();

            if (canvasImagePath != null && canvasImagePath.equals(tileImagePath)) {
                // 从 UI 和列表中移除 Canvas
                mainBoard.getChildren().remove(canvas);
                floodedTileCanvases.remove(i);

                // 更新剩余图片的位置
                updateFloodDeckImagePositions();
                break; // 找到后立即退出循环
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
        // 为所有 Tile 添加点击事件
        for (Tile tile : tiles) {
            tile.setOnMouseClicked(event -> handleTileSave(tile)); // 添加鼠标点击事件
        }
    }

    public void disableSaveMode() {
        // 禁用所有 Tile 的点击事件
        for (Tile tile : tiles) {
            tile.setOnMouseClicked(null); // 移除鼠标点击事件
        }
    }

    public void handleTileSave(Tile tile) {
        if (!isSaveMode) return; // 如果不在保存模式，则返回

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

            // 删除与当前 Tile 对应的图片
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

        // 动态调整宽度和高度
        messageLabel.setPrefWidth(200.0); // 设置最大宽度
        messageLabel.setPrefHeight(Region.USE_COMPUTED_SIZE); // 根据内容自动计算高度

        messageLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> messageLabel.setVisible(false));
        pause.play();
    }


    private void handleTurnOver() {
        turnManage.setStep(0);
        turnManage.showRemainSteps();
        changeCurrentPlayer();
        drawAllTreasureCards();
    }
    public void sentSpecialCards(List<TreasureCard> playerBag){
        // 根据概率分配宝藏牌
        List<TreasureCard> availableCards = new ArrayList<>();

        // 每种宝藏牌按其概率加入到 availableCards 中
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(0)); // soil
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(5)); // cloud
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(10)); // water
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(15)); // fire
        for (int i = 0; i < 3; i++) availableCards.add(new TreasureCard(20)); // helicopter
        for (int i = 0; i < 3; i++) availableCards.add(new TreasureCard(26)); // waterrise
        for (int i = 0; i < 2; i++) availableCards.add(new TreasureCard(23)); // sandbags

        // 随机抽取两个宝藏牌
        Random random = new Random();
        TreasureCard card1 = availableCards.get(random.nextInt(availableCards.size()));
        TreasureCard card2 = availableCards.get(random.nextInt(availableCards.size()));



        // 检查是否抽到了 waterrise 卡片
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
            promptDiscardCards(currentBag);  // 弹出丢弃界面
        }

    }

    public void sentSpecialCardsWithoutWaterRise(List<TreasureCard> playerBag){
        // 根据概率分配宝藏牌
        List<TreasureCard> availableCards = new ArrayList<>();

        // 每种宝藏牌按其概率加入到 availableCards 中
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(0)); // soil
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(5)); // cloud
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(10)); // water
        for (int i = 0; i < 5; i++) availableCards.add(new TreasureCard(15)); // fire
        for (int i = 0; i < 3; i++) availableCards.add(new TreasureCard(20)); // helicopter
        for (int i = 0; i < 2; i++) availableCards.add(new TreasureCard(23)); // sandbags

        // 随机抽取两个宝藏牌
        Random random = new Random();
        TreasureCard card1 = availableCards.get(random.nextInt(availableCards.size()));
        TreasureCard card2 = availableCards.get(random.nextInt(availableCards.size()));


        playerBag.add(card1);

        playerBag.add(card2);


    }


    public void promptDiscardCards(List<TreasureCard> bag) {
        showMessage("You have too many cards! Please discard until only 5 remain.");

        // 禁用所有其他控件
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

                // 每个卡片都可点击丢弃
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

                // 每个卡片都可点击丢弃
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


        // 清除 discard 卡牌 UI
        mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));

        drawAllTreasureCards();

        if (currentBags.get(t).size() > 5) {
            promptDiscardCards(currentBag);  // 继续丢弃
        } else {
            showMessage("You now have 5 cards or fewer.");

            // 丢弃完成后，重新启用其他控件
            setAllControlsDisabled(false);
        }

    }


    public void drawAllTreasureCards() {
        // 先移除旧的卡牌 Canvas
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
                cardCanvas.setUserData("treasure");  // 标记方便清除
                GraphicsContext gc = cardCanvas.getGraphicsContext2D();
                gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);
                mainBoard.getChildren().add(cardCanvas);
            }

        }

    }

    private void updateWaterMeter() {
        if (currentWaterMeterIndex < waterMeters.size() - 1) {
            // 从主 Pane 中移除当前 WaterMeter
            mainBoard.getChildren().remove(waterMeters.get(currentWaterMeterIndex));

            // 更新索引到下一个 WaterMeter
            currentWaterMeterIndex++;

            // 添加新的 WaterMeter 到主 Pane
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
        showMessage("Choose the special card you want to use:");

        double centerX = mainBoard.getWidth() / 2;
        double discardAreaY = screenController.getDiverBag().getLayoutY() - 150;

        double cardWidth = 80;
        double cardHeight = 120;
        int offset = 90;

        for (int i = 0; i < getSpecialCards().size(); i++) {
            TreasureCard card = getSpecialCards().get(i);
            double x = 700 ;
            double y = discardAreaY- (currentBag.size() * offset) / 2 + offset * i - 30;

            Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
            cardCanvas.setLayoutX(x);
            cardCanvas.setLayoutY(y);
            cardCanvas.setUserData("discard");

            GraphicsContext gc = cardCanvas.getGraphicsContext2D();
            gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

            int index = i;
            cardCanvas.setOnMouseClicked(e -> useThisCard(index));

            mainBoard.getChildren().add(cardCanvas);
        }
    }

    private void useThisCard(int index) {
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

            // 删除与当前 Tile 对应的图片
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
            mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));
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
            mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));
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


    public void checkTreasureSubmit(){
        int index = 0;
        Tile currentTile = getTileByPlayer(currentPlayer);
        if(currentTile.getName().equals("1") || currentTile.getName().equals("2")){
            for(TreasureCard card : currentBag){
                if(card.getType().equals(SOIL)){
                    index++;
                }
            }
            if(index >= 4){
                showMessage("You have submitted the treasure!");
                int j = 0;
                List<TreasureCard> cardsToRemove = new ArrayList<>();
                for (TreasureCard card : currentBag) {
                    if (card.getType() == SOIL && j < 4) {
                        cardsToRemove.add(card);
                        j++;
                    }
                }
                currentBag.removeAll(cardsToRemove);
                drawAllTreasureCards();
                treasures.get(1).draw();
                isGetSOIL=true;
            }
        }
        if(currentTile.getName().equals("3") || currentTile.getName().equals("4")){
            for(TreasureCard card : currentBag){
                if(card.getType().equals(CLOUD)){
                    index++;
                }
            }
            if(index >= 4){
                showMessage("You have submitted the treasure!");
                int j = 0;
                List<TreasureCard> cardsToRemove = new ArrayList<>();
                for (TreasureCard card : currentBag) {
                    if (card.getType() == CLOUD && j < 4) {
                        cardsToRemove.add(card);
                        j++;
                    }
                }
                currentBag.removeAll(cardsToRemove);
                drawAllTreasureCards();
                treasures.get(0).draw();
                isGetCLOUD=true;

            }
        }
        if(currentTile.getName().equals("7") || currentTile.getName().equals("8")){
            for(TreasureCard card : currentBag){
                if(card.getType().equals(WATER)){
                    index++;
                }
            }
            if(index >= 4){
                showMessage("You have submitted the treasure!");
                int j = 0;
                List<TreasureCard> cardsToRemove = new ArrayList<>();
                for (TreasureCard card : currentBag) {
                    if (card.getType() == WATER && j < 4) {
                        cardsToRemove.add(card);
                        j++;
                    }
                }
                currentBag.removeAll(cardsToRemove);
                drawAllTreasureCards();
                treasures.get(3).draw();
                isGetWATER=true;

            }
        }
        if(currentTile.getName().equals("5") || currentTile.getName().equals("6")){
            for(TreasureCard card : currentBag){
                if(card.getType().equals(FIRE)){
                    index++;
                }
            }
            if(index >= 4){
                showMessage("You have submitted the treasure!");
                int j = 0;
                List<TreasureCard> cardsToRemove = new ArrayList<>();
                for (TreasureCard card : currentBag) {
                    if (card.getType() == FIRE && j < 4) {
                        cardsToRemove.add(card);
                        j++;
                    }
                }
                currentBag.removeAll(cardsToRemove);
                drawAllTreasureCards();
                treasures.get(2).draw();
                isGetFIRE=true;

            }
        }
    }


    public void promptDiscardCardsByExchange(List<TreasureCard> bag) {
        showMessage("You have too many cards! Please discard until only 5 remain.");

        // 禁用所有其他控件
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

            // 每个卡片都可点击丢弃
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
        showMessage("Choose the card you want to sent!");

        List<TreasureCard> bag = selectTreasureCards(currentBag);
        // 禁用所有其他控件
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

            // 每个卡片都可点击给出
            int index = i;
            cardCanvas.setOnMouseClicked(e -> {
                giveCards(index);
            });

            mainBoard.getChildren().add(cardCanvas);
        }
    }

    private void giveCards(int index) {

        // 清除 discard 卡牌 UI
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
        }
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
        int index=0;
        Tile foolLanading = null;
        for(Tile t: tiles){
            if(t.getName().equals("10")){
                t = foolLanading;
            }
        }
        for(Player p : currentPlayers){
            if(p.getX() == foolLanading.getPositionX() && p.getY() == foolLanading.getPositionY()){
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
            showMessage("You lose!");
            screenController.setGameOver();
        }
    }


}