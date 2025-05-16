package player;

import canvas.Tile;

public class Messenger extends Player{
    public Messenger(String name, Tile startingTile) {
        super(name, startingTile);
    }

//    @Override
//    public void giveTreasureCard(Card card, Player recipient) {
//        // 移除同板块限制
//        if (this.hand.contains(card)) {
//            this.hand.remove(card);
//            recipient.receiveCard(card);
//            actionsRemaining--;
//        }
//    }
}
