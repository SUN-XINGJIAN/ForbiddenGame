package player;

import map.Tile;

public class Diver extends Player{
    public Diver(String name, Tile startingTile) {
        super(name, startingTile);
    }

    @Override
    public void move(Tile targetTile) {
        if (!targetTile.isRemoved() && Tile.isPathValidForDiver(targetTile)) {
            this.currentTile = targetTile;
            int actionRemained = getActionsRemaining();
            actionRemained--;
        }
    }
}
