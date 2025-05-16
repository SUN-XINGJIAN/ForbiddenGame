package player;

import map.Tile;

public class Engineer extends Player{

    public Engineer(String name, Tile startingTile) {
        super(name, startingTile);
    }

    @Override
    public void shoreUp(Tile targetTile) {
        if (getActionsRemaining() > 0 && !targetTile.isRemoved() && targetTile.isFlooded()) {
            targetTile.unflood();
            int actionRemained = getActionsRemaining();
            actionRemained--;
        }
    }
}
