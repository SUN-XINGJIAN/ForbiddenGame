package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

public class Messenger extends Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Messenger;
    private List<TreasureCard> MessengerBag = new ArrayList<>();
    public Messenger(String name){
        super("Diver");

        pawnName = "/image/Pawns/@2x/Messenger@2x.png";
    }

    @Override
    public void useSpecialAbility(ForbiddenGameStarted forbiddenGameStarted, Player player) {
        super.useSpecialAbility(forbiddenGameStarted, player);

        if (forbiddenGameStarted.selectTreasureCards(forbiddenGameStarted.currentBag).isEmpty()) {
            forbiddenGameStarted.showMessage("You have no treasure cards to give");
            return;
        }

        forbiddenGameStarted.showMessage("Choose a card to give!");

        forbiddenGameStarted.setAllControlsDisabled(true);

        List<TreasureCard> bag = forbiddenGameStarted.selectTreasureCards(forbiddenGameStarted.currentBag);

        double centerX = forbiddenGameStarted.mainBoard.getWidth() / 2;
        double discardAreaY = forbiddenGameStarted.screenController.getDiverBag().getLayoutY() - 150;

        double cardWidth = 80;
        double cardHeight = 120;
        int offset = 90;

        // 创建一个列表来保存当前添加的UI元素，以便稍后清除
        List<Node> tempUIElements = new ArrayList<>();

        for (int i = 0; i < bag.size(); i++) {
            TreasureCard card = bag.get(i);
            double x = centerX - (bag.size() * offset) / 2 + offset * i;
            double y = discardAreaY;

            Canvas cardCanvas = new Canvas(cardWidth, cardHeight);
            cardCanvas.setLayoutX(x);
            cardCanvas.setLayoutY(y);
            cardCanvas.setUserData("sendcard");

            GraphicsContext gc = cardCanvas.getGraphicsContext2D();
            gc.drawImage(new Image(getClass().getResourceAsStream(card.cardname)), 0, 0, cardWidth, cardHeight);

            final int index = i;

            cardCanvas.setOnMouseClicked(e -> {
                forbiddenGameStarted.mainBoard.getChildren().removeIf(node -> "sendcard".equals(node.getUserData()));
                forbiddenGameStarted.showMessage("Choose a player to give the card to!");

                for (Player p : forbiddenGameStarted.currentPlayers) {
                    for (PlayerBag pb : forbiddenGameStarted.playerBags) {
                        if (pb.getPlayerType().equals(p.getType())) {
                            pb.setDisable(false);
                            pb.setOnMouseClicked(e2 -> {
                                TreasureCard selectedCard = forbiddenGameStarted.selectTreasureCards(forbiddenGameStarted.currentBag).get(index);

                                p.getBag().add(selectedCard);
                                if (p.getBag().size() > 5) {
                                    forbiddenGameStarted.promptDiscardCards(p.getBag());
                                }

                                forbiddenGameStarted.currentBag.remove(selectedCard);
                                forbiddenGameStarted.drawAllTreasureCards();

                                forbiddenGameStarted.setAllControlsDisabled(false);
                                pb.setDisable(true);

                                forbiddenGameStarted.turnManage.useStep();
                                forbiddenGameStarted.turnManage.showRemainSteps();
                                forbiddenGameStarted.turnManage.step = forbiddenGameStarted.turnManage.getStep();
                                forbiddenGameStarted.changeCurrentPlayer();

                                // 清理临时UI元素
                                clearTempUIElements(tempUIElements,forbiddenGameStarted);

                                forbiddenGameStarted.messengerCount++;
                            });
                        }
                    }
                }
            });

            forbiddenGameStarted.mainBoard.getChildren().add(cardCanvas);
            tempUIElements.add(cardCanvas); // 保存到临时UI列表
        }

        // 添加取消按钮
        Button cancelButton = new Button("Cancel");
        cancelButton.setLayoutX(centerX - 50); // 按钮中心位置
        cancelButton.setLayoutY(discardAreaY + 200); // 按钮位置调整
        cancelButton.setOnAction(e -> {
            // 清理临时UI元素
            clearTempUIElements(tempUIElements,forbiddenGameStarted);

            // 恢复控件状态
            forbiddenGameStarted.setAllControlsDisabled(false);
        });

        forbiddenGameStarted.mainBoard.getChildren().add(cancelButton);
        tempUIElements.add(cancelButton); // 保存到临时UI列表
    }

    // 清理临时UI元素的方法
    private void clearTempUIElements(List<Node> tempUIElements,ForbiddenGameStarted forbiddenGameStarted) {
        for (Node node : tempUIElements) {
            forbiddenGameStarted.mainBoard.getChildren().remove(node);
        }
        tempUIElements.clear(); // 清空列表以避免重复清理
    }



    @Override
    public void draw() {

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(pawnName)),0,0,getWidth(),getHeight());
    }


    public int getPositionX(ForbiddenGameStarted forbiddenGameStarted){
        int x=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("12")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }
    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("12")){
                y = tile.getPositionY();           }
        }
        return y;
    }
    public String getName(){
        return "Messenger";
    }

    public module.PlayerBag.playerType getType() {
        return type;
    }

    @Override
    public void resetSpecialAbility() {

    }

    public List<TreasureCard> getBag() {
        return MessengerBag;
    }
}