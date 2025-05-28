package module;

import cards.TreasureCard;
import logic.ForbiddenGameStarted;

import java.util.List;

//
//import board.Treasure;
//import board.Tile;
//import cards.Card;
//import cards.TreasureCard;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public abstract class Player {
//    private final String name;
//    protected Tile currentTile; // -> map 中 tile类
//    private final List<Card> hand = new ArrayList<>(); // 需要定义 Card 基类（包含 TreasureCard/SpecialActionCard）
//    private int actionsRemaining;
//    private final List<Treasure.Type> capturedTreasures = new ArrayList<>();
//
//    public Player(String name, Tile startingTile) {
//        this.name = name;
//        this.currentTile = startingTile;
//        this.actionsRemaining = 3;
//    }
//
//    // Basic movement
//    public void move(Tile targetTile) {
//        if (Tile.isAdjacent(targetTile) && !targetTile.isRemoved()) {
//            this.currentTile = targetTile;
//            actionsRemaining--;
//        }
//    }
//    // 判断手牌是否有指定类型卡片（至少指定数量）
//    public boolean hasCardsOfType(Treasure.Type type, int requiredCount) {
//        return hand.stream()
//                .filter(card -> card instanceof TreasureCard)
//                .map(card -> (TreasureCard) card)
//                .filter(tc -> tc.getTreasureType() == type)
//                .count() >= requiredCount;
//    }
//
//    // 丢弃指定类型的卡片
//    public void discardCards(Treasure.Type type, int count) {
//        List<Card> toDiscard = hand.stream()
//                .filter(card -> card instanceof TreasureCard)
//                .map(card -> (TreasureCard) card)
//                .filter(tc -> tc.getTreasureType() == type)
//                .limit(count)
//                .collect(Collectors.toList());
//
//        hand.removeAll(toDiscard);
//    }
//
//    // Basic shore-up
//    public void shoreUp(Tile targetTile) {
//        if (!targetTile.isRemoved() && targetTile.isFlooded()) {
//            targetTile.unflood();
//            actionsRemaining--;
//        }
//    }
//
//    // 传递卡片功能
//    public void giveCard(Card card, Player recipient) {
//        if (this.currentTile.equals(recipient.currentTile) && hand.contains(card)) {
//            hand.remove(card);
//            recipient.receiveCard(card);
//            actionsRemaining--;
//        }
//    }
//
//    // 捕获宝藏功能
//    public boolean captureTreasure() {
//        if (!currentTile.isTreasureSite()) return false;
//
//        Treasure.Type treasureType = currentTile.getTreasureType();
//        if (treasureType != null && hasCardsOfType(treasureType, 4)) {
//            discardCards(treasureType, 4);
//            capturedTreasures.add(treasureType); // 类型匹配
//            return true;
//        }
//        return false;
//    }
//
//    public void receiveCard(Card card) {
//        hand.add(card);
//    }
//
//
//
//    //Getters
//    public Tile getCurrentTile() { return currentTile; }
//    public int getActionsRemaining() { return actionsRemaining; }
//    public List<Card> getHand() { return new ArrayList<>(hand); }
//    public List<Treasure.Type> getCapturedTreasures() { return new ArrayList<>(capturedTreasures); }
//}
public abstract class Player extends PawnCanvas {
    public String name;
    private List<TreasureCard> bag;
    private module.PlayerBag.playerType type;
    public Player(String name) {
        super(name);
        this.name = name;
    }

    public void useSpecialAbility(ForbiddenGameStarted forbiddenGameStarted,Player player) {

    }



    public abstract int getPositionX(ForbiddenGameStarted forbiddenGameStarted);

    public abstract int getPositionY(ForbiddenGameStarted forbiddenGameStarted);

    public String getName() {
        return name;
    }

    public List<TreasureCard> getBag() {
        return bag;
    }
    public module.PlayerBag.playerType getType() {
        return type;
    }

    public boolean isPilot() {
        return this instanceof Pilot;
    }
    public abstract void resetSpecialAbility();
}