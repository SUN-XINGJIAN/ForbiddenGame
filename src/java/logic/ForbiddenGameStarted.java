package logic;

import canvas.PawnCanvas;
import controller.ScreenController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

public class ForbiddenGameStarted {

    private ScreenController screenController;
    private PawnCanvas pawnCanvas;
    private Pane mainBoard;
    private Button currentButton;
    private boolean isMoveMode = false;
    private Timeline moveTimeline;

    public ForbiddenGameStarted(ScreenController screenController) {
        this.screenController = screenController;
        mainBoard = screenController.getMainBoard();
        initializeGame();

        // 为move按钮添加点击事件处理程序
        screenController.getMove().setOnAction(event -> {
            isMoveMode =!isMoveMode;
            if (isMoveMode) {
                screenController.getMove().setText("Cancel Move");
                move();
            } else {
                screenController.getMove().setText("move");
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

    public void move() {
        isMoveMode = true;

        // 遍历 mainBoard 的所有子节点
        for (javafx.scene.Node node : mainBoard.getChildren()) {
            if (node instanceof Button) {
                Button tileButton = (Button) node;

                // 添加点击监听器
                tileButton.setOnAction(e -> {
                    if (!isMoveMode) return;

                    int targetX = (int) tileButton.getLayoutX();
                    int targetY = (int) tileButton.getLayoutY();

                    int currX = (int) pawnCanvas.getLayoutX();
                    int currY = (int) pawnCanvas.getLayoutY();

                    // 每个tile的宽度与高度（假设固定）
                    int tileSize = 50; // 替换成你实际Button的尺寸或间距

                    // 计算是否是相邻格（上下左右）
                    boolean isAdjacent =
                            (Math.abs(targetX - currX) == tileSize && targetY == currY) || // 左右相邻
                                    (Math.abs(targetY - currY) == tileSize && targetX == currX);   // 上下相邻

                    if (isAdjacent) {
                        pawnCanvas.setLayoutX(targetX);
                        pawnCanvas.setLayoutY(targetY);
                        pawnCanvas.draw();

                        // 更新记录位置（如果你用x/y属性存）
                        pawnCanvas.setX(targetX);
                        pawnCanvas.setY(targetY);

                        isMoveMode = false;

                        // 可选：还原按钮样式或提示
                        screenController.getMove().setText("Move");
                    } else {
                        System.out.println("Target tile is not adjacent!");
                    }
                });
            }
        }
    }
}