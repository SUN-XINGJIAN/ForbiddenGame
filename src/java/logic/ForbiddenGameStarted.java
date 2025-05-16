package logic;

import canvas.PawnCanvas;
import controller.ScreenController;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import canvas.Tile;

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

        mainBoard.getChildren().addAll(tiles);
        for (Tile tile : tiles) {
            tile.draw();
        }
        drawPawn();



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
            System.out.println("Tile removed: " + selectedTile); // 打印日志
        } else {
            selectedTile.draw(); // 重新绘制
        }
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
                        System.out.println("Target tile is not adjacent!");
                    }
                });
            }
        }
    }



}
