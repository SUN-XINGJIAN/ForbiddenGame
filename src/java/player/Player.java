package player;

import map.Tile;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final String name;
    protected Tile currentTile; // -> map 中 tile类
    // private final List<Card> hand = new ArrayList<>(); // 需要定义 Card 基类（包含 TreasureCard/SpecialActionCard）
    private int actionsRemaining;
    private final List<String> capturedTreasures = new ArrayList<>();

    public Player(String name, Tile startingTile) {
        this.name = name;
        this.currentTile = startingTile;
        this.actionsRemaining = 3;
    }

    // Basic movement
    public void move(Tile targetTile) {
        if (Tile.isAdjacent(targetTile) && !targetTile.isRemoved()) {
            this.currentTile = targetTile;
            actionsRemaining--;
        }
    }
//    protected abstract boolean isAdjacent(Tile targetTile);
//    private boolean hasFourMatchingCards(String type) {
//        return hand.stream().filter(c -> c.getType().equals(type)).count() >= 4;
//    }
//    private void discardCards(String type) {
//        hand.removeIf(c -> c.getType().equals(type));
//    }

    // Basic shore-up
    public void shoreUp(Tile targetTile) {
        if (!targetTile.isRemoved() && targetTile.isFlooded()) {
            targetTile.unflood();
            actionsRemaining--;
        }
    }

    // 传递卡片（需 Card 类实现 getType() 等方法）
//    public void giveTreasureCard(Card card, Player recipient) {
//        if (this.currentTile.equals(recipient.currentTile)) {
//            this.hand.remove(card);
//            recipient.receiveCard(card);
//            actionsRemaining--;
//        }
//    }
//
//    // 捕获宝藏（需 Tile 类实现 getTreasureType() 方法）
//    public void captureTreasure() {
//        String treasureType = currentTile.getTreasureType();
//        if (hasFourMatchingCards(treasureType) && currentTile.isTreasureSite()) {
//            discardCards(treasureType);
//            capturedTreasures.add(treasureType);
//        }
//    }
//
//
//
//    // 需要的外部方法
//    public void receiveCard(Card card) {
//        hand.add(card);
//    }

    // Getters
    public Tile getCurrentTile() { return currentTile; }
    public int getActionsRemaining() { return actionsRemaining; }
}
