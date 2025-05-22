package logic;

import canvas.PawnCanvas;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ForbiddenGameStarted {

    private ScreenController screenController;
    private PawnCanvas pawnCanvas;
    private Pane mainBoard;
    private Button currentButton;
    private boolean isMoveMode = false;
    private int[] random1= new int[24];
    private List<Tile> tiles = new ArrayList<>();
    private List<TreasureCard> treasureCards = new ArrayList<>();
    private boolean isSaveMode = false; // 是否进入 "save the island" 模式
    private Label messageLabel;
    private List<Canvas> floodedTileCanvases = new ArrayList<>(); // 用于保存 FloodDeck 后面的所有图片
    private List<TreasureCard> DiverBag = new ArrayList<>(); // 用于保存 TreasureDeck 后面的所有图片
    private Button turnOverButton;


    public ForbiddenGameStarted(ScreenController screenController) {
        // 初始化random1数组
        random1 = new int[24];
        List<Integer> numbers = new ArrayList<>();

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
                enableTileMovement();  // 启动每次点击时可以移动棋子的模式
            } else {
                screenController.getMove().setText("Move");
            }
        });

        // 为SelectFloodCards按钮添加点击事件处理程序
        screenController.getSelectFloodCards().setOnAction(event -> {
            selectRandomTile(); // 随机选择一个Tile并更改状态
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
        });

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
        drawPawn();
    }

    private void setAllControlsDisabled(boolean disable) {
        for (Node node : mainBoard.getChildren()) {
            if (node instanceof Button || node instanceof Canvas) {
                node.setDisable(disable); // 禁用或启用控件
            }
        }
    }


    private void drawPawn() {
        pawnCanvas = new PawnCanvas(282, 194);
        mainBoard.getChildren().add(pawnCanvas);
        pawnCanvas.draw();
    }

    private void selectRandomTile() {
        if (tiles.isEmpty()) return; // 确保列表不为空

        Random random = new Random();
        int randomIndex = random.nextInt(tiles.size());
        Tile selectedTile = tiles.get(randomIndex);

        selectedTile.incrementState(); // 增加状态值

        if (selectedTile.getState() >= 2) {
            // 如果 state >= 2，从列表和 UI 中移除
            tiles.remove(selectedTile); // 从列表中移除
            mainBoard.getChildren().remove(selectedTile); // 从 UI 中移除
        } else {
            selectedTile.draw(); // 重新绘制
        }

        // 在 FloodDeck 后面绘制选中的 Tile 的图片
        drawFloodedTileOnDeck(selectedTile);
    }


    private void enableTileMovement() {
        // 遍历 mainBoard 的所有子节点
        for (javafx.scene.Node node : mainBoard.getChildren()) {
            if (node instanceof Tile  tile) {
                tile.setOnMouseClicked(e -> {
                    if (!isMoveMode) return;

                    int targetX = (int) tile.getLayoutX();
                    int targetY = (int) tile.getLayoutY();

                    int currX = (int) pawnCanvas.getLayoutX();
                    int currY = (int) pawnCanvas.getLayoutY();

                    int tileSize = 50;

                    boolean isAdjacent =
                            (Math.abs(targetX - currX) == tileSize && targetY == currY) ||
                                    (Math.abs(targetY - currY) == tileSize && targetX == currX);

                    if (isAdjacent) {
                        pawnCanvas.setLayoutX(targetX);
                        pawnCanvas.setLayoutY(targetY);
                        pawnCanvas.draw();

                        isMoveMode = false;
                        screenController.getMove().setText("Move");
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

    private void removeFloodedTileFromDeckByTile(Tile tile) {
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



    private void enableSaveMode() {
        // 为所有 Tile 添加点击事件
        for (Tile tile : tiles) {
            tile.setOnMouseClicked(event -> handleTileSave(tile)); // 添加鼠标点击事件
        }
    }

    private void disableSaveMode() {
        // 禁用所有 Tile 的点击事件
        for (Tile tile : tiles) {
            tile.setOnMouseClicked(null); // 移除鼠标点击事件
        }
    }

    private void handleTileSave(Tile tile) {
        if (!isSaveMode) return; // 如果不在保存模式，则返回

        int targetX = (int) tile.getLayoutX();
        int targetY = (int) tile.getLayoutY();

        int currX = (int) pawnCanvas.getLayoutX();
        int currY = (int) pawnCanvas.getLayoutY();

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


    private void showMessage(String message) {
        if (messageLabel == null) {
            messageLabel = new Label();
            messageLabel.setLayoutX(409.0);
            messageLabel.setLayoutY(23.0);
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

        DiverBag.add(card1);
        DiverBag.add(card2);

        drawAllTreasureCards();

        if (DiverBag.size() > 5) {
            promptDiscardCards();  // 弹出丢弃界面
        }
    }

    private void promptDiscardCards() {
        showMessage("You have too many cards! Please discard until only 5 remain.");

        // 禁用所有其他控件
        setAllControlsDisabled(true);

        double centerX = mainBoard.getWidth() / 2;
        double discardAreaY = screenController.getDiverBag().getLayoutY() - 150;

        double cardWidth = 80;
        double cardHeight = 120;
        int offset = 90;

        for (int i = 0; i < DiverBag.size(); i++) {
            TreasureCard card = DiverBag.get(i);
            double x = centerX - (DiverBag.size() * offset) / 2 + offset * i;
            double y = discardAreaY;

            Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
            cardCanvas.setLayoutX(x);
            cardCanvas.setLayoutY(y);
            cardCanvas.setUserData("discard");

            GraphicsContext gc = cardCanvas.getGraphicsContext2D();
            gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

            // 每个卡片都可点击丢弃
            int index = i;
            cardCanvas.setOnMouseClicked(e -> confirmDiscard(index));

            mainBoard.getChildren().add(cardCanvas);
        }
    }



    private void confirmDiscard(int index) {
        DiverBag.remove(index);

        // 清除 discard 卡牌 UI
        mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));

        drawAllTreasureCards();

        if (DiverBag.size() > 5) {
            promptDiscardCards();  // 继续丢弃
        } else {
            showMessage("You now have 5 cards or fewer.");

            // 丢弃完成后，重新启用其他控件
            setAllControlsDisabled(false);
        }
    }


    private void drawAllTreasureCards() {
        // 先移除旧的卡牌 Canvas
        mainBoard.getChildren().removeIf(node -> node instanceof Canvas && "treasure".equals(node.getUserData()));

        double diverBagX = screenController.getDiverBag().getLayoutX();
        double diverBagY = screenController.getDiverBag().getLayoutY();
        double cardWidth = 50;
        double cardHeight = 69;
        int offset = 50;

        for (int i = 0; i < DiverBag.size(); i++) {
            TreasureCard card = DiverBag.get(i);
            double x = diverBagX + offset * i + 50;
            double y = diverBagY;

            Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
            cardCanvas.setLayoutX(x);
            cardCanvas.setLayoutY(y);
            cardCanvas.setUserData("treasure");  // 标记方便清除
            GraphicsContext gc = cardCanvas.getGraphicsContext2D();
            gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);
            mainBoard.getChildren().add(cardCanvas);
        }
    }

}