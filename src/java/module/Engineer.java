package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

public class Engineer extends module.Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Engineer;
    private List<TreasureCard> EngineerBag = new ArrayList<>();
    private int saveTiles = 0;

    public Engineer(String name){
        super("Engineer");
        pawnName = "/image/Pawns/@2x/Engineer@2x.png";
    }

    // Engineer's special ability: shore up to 2 flooded adjacent tiles per turn
    public void useSpecialAbility(ForbiddenGameStarted forbiddenGameStarted, Player player) {
        super.useSpecialAbility(forbiddenGameStarted, player);

        saveTiles = 0; // Each time this method is called, the saved count is reset

        if (countSaveableTilesInRange(forbiddenGameStarted, player) < 2) {
            forbiddenGameStarted.showMessage("Not enough islands to save in range!");
            return;
        }

        for (Tile tile : forbiddenGameStarted.getTiles()) {
            tile.setOnMouseClicked(e -> {
                int targetX = (int) tile.getLayoutX();
                int targetY = (int) tile.getLayoutY();

                int currX = (int) player.getLayoutX() - 30;
                int currY = (int) player.getLayoutY();

                int tileSize = 50;

                boolean isAdjacentOrSame =
                        (Math.abs(targetX - currX) == tileSize && targetY == currY) ||
                                (Math.abs(targetY - currY) == tileSize && targetX == currX) ||
                                (targetX == currX && targetY == currY);

                if (!isAdjacentOrSame) {
                    forbiddenGameStarted.showMessage("The island is too far to save!");
                    return;
                }

                if (tile.getState() != 1) {
                    forbiddenGameStarted.showMessage("The island has either submerged or has not been flooded!");
                    return;
                }

                // Perform rescue if possible
                tile.setState(0);
                tile.draw();
                forbiddenGameStarted.removeFloodedTileFromDeckByTile(tile);

                saveTiles++;

                forbiddenGameStarted.showMessage("Island saved successfully!");

                // Check if 2 is saved
                if (saveTiles == 1) {
                    forbiddenGameStarted.showMessage("You can save one more island!");
                } else if (saveTiles == 2) {
                    // If up to 2, end the operation
                    forbiddenGameStarted.disableSaveMode();
                    forbiddenGameStarted.engineerCount++;

                    forbiddenGameStarted.turnManage.useStep();
                    forbiddenGameStarted.turnManage.showRemainSteps();
                    forbiddenGameStarted.step = forbiddenGameStarted.turnManage.getStep();
                    forbiddenGameStarted.changeCurrentPlayer();
                }
            });
        }
    }

    // Counts flooded tiles adjacent to player's position
    private int countSaveableTilesInRange(ForbiddenGameStarted forbiddenGameStarted, Player player) {
        int saveableCount = 0;
        int currX = (int) player.getLayoutX() - 30;
        int currY = (int) player.getLayoutY();

        for (Tile tile : forbiddenGameStarted.getTiles()) {
            int targetX = (int) tile.getLayoutX();
            int targetY = (int) tile.getLayoutY();

            int tileSize = 50;
            boolean isAdjacentOrSame =
                    (Math.abs(targetX - currX) == tileSize && targetY == currY) ||
                            (Math.abs(targetY - currY) == tileSize && targetX == currX) ||
                            (targetX == currX && targetY == currY);

            if (isAdjacentOrSame && tile.getState() == 1) {
                saveableCount++;
            }
        }

        return saveableCount; // Return the number of tiles that can be saved
    }


    @Override
    public void draw() {

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(pawnName)),0,0,getWidth(),getHeight());
    }


    public int getPositionX(ForbiddenGameStarted forbiddenGameStarted){
        int x=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("10")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }
    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("10")){
                y = tile.getPositionY();           }
        }
        return y;
    }

    private int countFloodTiles(List<Tile> tiles,Player player){
        List<Tile> floodTiles = new ArrayList<>();

        int currX = (int) player.getLayoutX() - 30;
        int currY = (int) player.getLayoutY();

        for(Tile t : tiles){

            int targetX = (int) t.getLayoutX();
            int targetY = (int) t.getLayoutY();

            int tileSize = 50;
            boolean isAdjacentOrSame =
                    (Math.abs(targetX - currX) == tileSize && targetY == currY) ||
                            (Math.abs(targetY - currY) == tileSize && targetX == currX) ||
                            (targetX == currX && targetY == currY);
            if(isAdjacentOrSame && t.getState() == 1){
                floodTiles.add(t);
            }
        }
        return floodTiles.size();
    }

    public String getName(){
        return "Engineer";
    }

    public module.PlayerBag.playerType getType() {
        return type;
    }

    @Override
    public void resetSpecialAbility() {

    }

    @Override
    public List<TreasureCard> getBag() {
        return EngineerBag;
    }
}