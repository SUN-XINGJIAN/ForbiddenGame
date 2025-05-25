package logic;

import player.*;
import java.util.List;


public class TurnManager {
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private int actionsLeft = 3;
    private long turnStartTime;


    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
        this.actionsLeft = 3;
        recordTurnStartTime();
    }


    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }


    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        actionsLeft = 3;
        recordTurnStartTime();
    }


    public boolean canPerformAction() {
        return actionsLeft > 0;
    }


    public void performAction() {
        if (actionsLeft > 0) {
            actionsLeft--;
        }
    }


    public void resetActions() {
        actionsLeft = 3;
    }


    public void recordTurnStartTime() {
        turnStartTime = System.currentTimeMillis();
    }


    public long getTurnStartTime() {
        return turnStartTime;
    }

    public int getActionsLeft() {
        return actionsLeft;
    }
} 