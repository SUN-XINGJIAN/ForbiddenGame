package cards;

import board.Tile;

public class FloodCard extends Card{
    private final Tile targetTile; // 需要 board.Tile 类

    public FloodCard(Tile tile) {
        super(CardType.FLOOD, "Flood: " + tile.getPosition().toString());
        this.targetTile = tile;
    }

    @Override
    public void playEffect() {
        // 需要调用 Tile 类的双重淹没逻辑：
        if (targetTile.isFlooded()) {
            targetTile.removeFromBoard(); // Tile 被永久移除
        } else {
            targetTile.flood(); // 首次淹没
        }
    }

    public Tile getTargetTile() { return targetTile; }
}
