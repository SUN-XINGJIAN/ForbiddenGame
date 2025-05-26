package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

//package module;
//
//import board.Tile;
//
//public class Messenger extends Player {
//    public Messenger(String name, Tile startingTile) {
//        super(name, startingTile);
//    }
//
////    @Override
////    public void giveTreasureCard(Card card, Player recipient) {
////        // 移除同板块限制
////        if (this.hand.contains(card)) {
////            this.hand.remove(card);
////            recipient.receiveCard(card);
////            actionsRemaining--;
////        }
////    }
//}
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
        forbiddenGameStarted.messengerCount++;

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

            final int index = i;

            cardCanvas.setOnMouseClicked(e -> {
                forbiddenGameStarted.mainBoard.getChildren().removeIf(node -> "discard".equals(node.getUserData()));
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
                            });
                        }
                    }
                }
            });

            forbiddenGameStarted.mainBoard.getChildren().add(cardCanvas);
        }


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