package cards;

import board.Treasure;

public class TreasureCard extends Card {
    public int num;
    private final Treasure.Type treasureType = null;
    private Type type;



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


    public int getCardType() {
        return num;
    }
}
