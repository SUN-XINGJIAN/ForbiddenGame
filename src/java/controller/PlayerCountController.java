package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class PlayerCountController {
    @FXML private ComboBox<Integer> playerCount;
    private int selectedCount;

    @FXML
    public void initialize() {
        playerCount.getItems().addAll(2, 3, 4);
    }

    @FXML
    private void startGame() {
        Integer selected = playerCount.getValue();  // 获取选择值
        if (selected == null || selected < 2 || selected > 4) {
            // 显示错误提示（可以添加具体提示逻辑）
            System.out.println("请选择2-4个玩家");
            return;
        }
        selectedCount = selected;  // 正确赋值给成员变量
        ((Stage) playerCount.getScene().getWindow()).close();
    }

    public int getPlayerCount() {
        return selectedCount;
    }
}
