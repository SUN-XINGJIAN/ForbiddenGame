package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Diver extends module.Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Diver;
    private List<TreasureCard> DiverBag ;

    public Diver(String name){
        super("Diver");
        DiverBag = new ArrayList<>();
        pawnName = "/image/Pawns/@2x/Diver@2x.png";
    }

    @Override
    public void useSpecialAbility(ForbiddenGameStarted forbiddenGameStarted, Player player) {
        super.useSpecialAbility(forbiddenGameStarted, player);

        // When the special skill is triggered, the player clicks on the section of the map
        Tile currentTile = forbiddenGameStarted.getTileByPlayer(player); // Obtain the Tile where Diver is currently located

        // Suppose the target Tile is the section that the player clicks on
        for(Tile t : forbiddenGameStarted.getTiles()){
            t.setOnMouseClicked(e -> {
                int targetX = (int) t.getLayoutX();
                int targetY = (int) t.getLayoutY();

                if (t != null && !t.isRemoved() && canReachByWater(currentTile, t, forbiddenGameStarted)) {
                    player.setLayoutX(targetX+30);
                    player.setX(targetX);
                    player.setLayoutY(targetY);
                    player.setY(targetY);
                    player.draw();
                    forbiddenGameStarted.checkTreasureSubmit();

                    forbiddenGameStarted.turnManage.useStep();
                    forbiddenGameStarted.turnManage.showRemainSteps();
                    forbiddenGameStarted.changeCurrentPlayer();

                    forbiddenGameStarted.exchangeCards();;
                    forbiddenGameStarted.checkTreasureSubmit();
                    forbiddenGameStarted.isVictory();
                } else {
                    // If the target section is inaccessible
                    forbiddenGameStarted.showMessage("No valid water path to this tile.");
                }
            });
        }

    }

    // Determine whether there is a waterway from the current tile to the target tile
    private boolean canReachByWater(Tile startTile, Tile targetTile, ForbiddenGameStarted forbiddenGameStarted) {
        // Use BFS to determine the waterway connections
        Queue<Tile> queue = new LinkedList<>();
        List<Tile> visited = new ArrayList<>();
        queue.add(startTile);
        visited.add(startTile);

        while (!queue.isEmpty()) {
            Tile currentTile = queue.poll();
            // If the current section is the target section, return true
            if (currentTile.equals(targetTile)) {
                return true;
            }

            // Get adjacent tile
            List<Tile> neighbors = getNeighbors(currentTile, forbiddenGameStarted);
            for (Tile neighbor : neighbors) {
                if (!visited.contains(neighbor) && !neighbor.isRemoved()) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        // If no path is found after the BFS is completed, return false
        return false;
    }

    // Obtain all the adjacent sections to the current section
    private List<Tile> getNeighbors(Tile currentTile, ForbiddenGameStarted forbiddenGameStarted) {
        List<Tile> neighbors = new ArrayList<>();
        t = forbiddenGameStarted.getTiles(); // Get all the tiles

        // Obtain the adjacent sections
        for (Tile tile : t) {
            if (isAdjacent(currentTile, tile)) {
                neighbors.add(tile);
            }
        }
        return neighbors;
    }

    // See if two tiles are adjacent
    private boolean isAdjacent(Tile currentTile, Tile neighborTile) {
        return Math.abs(currentTile.getPositionX() - neighborTile.getPositionX()) <= 50 &&
                Math.abs(currentTile.getPositionY() - neighborTile.getPositionY()) <= 50;
    }

    @Override
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(pawnName)),0,0,getWidth(),getHeight());
    }

    @Override
    public int getPositionX(ForbiddenGameStarted forbiddenGameStarted){
        int x=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("9")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }

    @Override
    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("9")){
                y = tile.getPositionY();           }
        }
        return y;
    }

    public void moveWithoutLand(Tile target){

    }

    public String getName(){
        return "Diver";
    }

    public module.PlayerBag.playerType getType() {
        return type;
    }

    @Override
    public void resetSpecialAbility() {

    }

    public List<TreasureCard> getBag() {
        return DiverBag;
    }
}
