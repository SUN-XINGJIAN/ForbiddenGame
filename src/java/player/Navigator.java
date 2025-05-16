package player;

import map.Tile;

public class Navigator extends Player{
    public Navigator(String name, Tile startingTile) {
        super(name, startingTile);
    }

    public void directMove(Player otherPlayer, Tile targetTile) {
        if (this.getCurrentTile().equals(otherPlayer.getCurrentTile())
                && targetTile.isAdjacent(otherPlayer.getCurrentTile())
                && !targetTile.isRemoved()) {
            otherPlayer.move(targetTile);
            int actionRemained = getActionsRemaining();
            actionRemained--;
        }
    }
}
