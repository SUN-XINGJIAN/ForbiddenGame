package logic;

import canvas.PawnCanvas;
import controller.ScreenController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ForbiddenGameStarted {

    private ScreenController screenController;
    private PawnCanvas pawnCanvas;
    private Pane mainBoard;
    private Button currentButton;
    private boolean isMoveMode = false;

    public ForbiddenGameStarted(ScreenController screenController) {
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
                resetTileListeners();  // 取消按钮监听器
            }
        });
    }

    private void initializeGame() {
        drawPawn();
    }

    private void drawPawn() {
        pawnCanvas = new PawnCanvas(282, 194);
        mainBoard.getChildren().add(pawnCanvas);
        pawnCanvas.draw();
    }

    private void enableTileMovement() {
        // 遍历 mainBoard 的所有子节点
        for (javafx.scene.Node node : mainBoard.getChildren()) {
            if (node instanceof Button tileButton && tileButton.getStyleClass().contains("tile-button")) {
                tileButton.setOnAction(e -> {
                    if (!isMoveMode) return;

                    int targetX = (int) tileButton.getLayoutX();
                    int targetY = (int) tileButton.getLayoutY();

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
                        resetTileListeners();
                        screenController.getMove().setText("Move");
                    } else {
                        System.out.println("Target tile is not adjacent!");
                    }
                });
            }
        }
    }


    // 取消棋盘按钮的点击事件监听器
    private void resetTileListeners() {
        for (javafx.scene.Node node : mainBoard.getChildren()) {
            if (node instanceof Button) {
                Button tileButton = (Button) node;
                tileButton.setOnAction(null);  // 移除事件监听
            }
        }
        // 重新为 Move 按钮添加监听器
        screenController.getMove().setOnAction(event -> {
            isMoveMode = !isMoveMode;  // 切换移动模式
            if (isMoveMode) {
                screenController.getMove().setText("Cancel Move");
                enableTileMovement();  // 启动每次点击时可以移动棋子的模式
            } else {
                screenController.getMove().setText("Move");
                resetTileListeners();  // 取消棋盘按钮的监听器
            }
        });
    }
}
