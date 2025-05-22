package cards;

import board.Treasure;

public class TreasureCard extends Card {
    public int num;
    private final Treasure.Type treasureType = null;
    public TreasureCard(int number) {
        cardname = "/image/TreasureCards/" + number + ".png";
        num = number;
    }
    public Treasure.Type getTreasureType() { return treasureType; }

    public int getCardType() {
        return num;
    }
}

//    // 需要 board.Treasure.Type 枚举
//    private final Treasure.Type treasureType;
//
//    public TreasureCard(Treasure.Type treasureType) {
//        super(CardType.TREASURE, treasureType.name());
//        this.treasureType = treasureType;
//    }
//
//    // 收集4张触发效果
//    @Override
//    public void playEffect() {
//    }
//
//    public Treasure.Type getTreasureType() { return treasureType; }
