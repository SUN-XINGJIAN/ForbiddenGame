package player;

import map.Tile;

public class Pilot extends Player{
    private boolean specialFlightUsed = false;

    public Pilot(String name, Tile startingTile) {
        super(name, startingTile);
    }

    @Override
    public void move(Tile targetTile) {
        if (!specialFlightUsed && !targetTile.isRemoved()) {
            this.currentTile = targetTile;
            specialFlightUsed = true;
            int actionRemained = getActionsRemaining();
            actionRemained--;
        } else {
            super.move(targetTile);
        }
    }
}
