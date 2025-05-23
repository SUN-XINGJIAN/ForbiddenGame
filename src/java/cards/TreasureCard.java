package cards;

import board.Tile;
import board.Treasure;

public class TreasureCard extends Card {
    public int num;
    private final Treasure.Type treasureType = null;
    private Type type;

    public String getImagePath() {
        return cardname;
    }

    public enum Type {
        SOIL, CLOUD, WATER, FIRE, HELICOPTER, SANDBAGS, WATERRISE
    }
    public TreasureCard(int number) {
        cardname = "/image/TreasureCards/" + number + ".png";
        num = number;

        switch (number) {
            case 0:
                this.type = Type.SOIL;
                break;
            case 5:
                this.type = Type.CLOUD;
                break;
            case 10:
                this.type = Type.FIRE;
                break;
            case 15:
                this.type = Type.WATER;
                break;
            case 23:
                this.type = Type.SANDBAGS;
                break;
            case 20:
                this.type = Type.HELICOPTER;
                break;
        }
    }

    public Type getType() {
        return type;
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
