package module;

import cards.TreasureCard;
import logic.ForbiddenGameStarted;

import java.util.List;

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