package cards;

import board.Tile;

public class FloodCard extends Card{
    private final Tile targetTile; // 需要 board.Tile 类

    public FloodCard(Tile tile) {
        super();
        this.targetTile = tile;
    }



    public Tile getTargetTile() { return targetTile; }
}
