package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;


public class Navigator extends Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Navigator;
    private List<TreasureCard> NavigatorBag = new ArrayList<>();

    public Navigator(String name){
        super("Diver");
        pawnName = "/image/Pawns/@2x/Navigator@2x.png";
    }

    @Override
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(pawnName)),0,0,getWidth(),getHeight());
    }

    @Override
    public void useSpecialAbility(ForbiddenGameStarted forbiddenGameStarted, Player player) {
        super.useSpecialAbility(forbiddenGameStarted, player);
        forbiddenGameStarted.navigatorCount++;

        forbiddenGameStarted.showMessage("Select a player to move!");

        // Enable controlling to other players
        for (Player p : forbiddenGameStarted.currentPlayers) {
            if (!p.equals(player)) { // Exclude currentPlayer
                for (PlayerBag pb : forbiddenGameStarted.playerBags) {
                    if (pb.getPlayerType().equals(p.getType())) {
                        pb.setDisable(false); // Enable the controls for this player
                        pb.setOnMouseClicked(e -> {
                            forbiddenGameStarted.isMoveMode = !forbiddenGameStarted.isMoveMode;  // Switch to the move mode
                            if (forbiddenGameStarted.isMoveMode) {
                                forbiddenGameStarted.enableTileMovement(p);  // Enable the mode where the pawns can be moved each time a click occurs
                            }
                        });
                    }
                }
            }
        }
    }

    public int getPositionX(ForbiddenGameStarted forbiddenGameStarted){
        int x=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("13")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }

    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("13")){
                y = tile.getPositionY();           }
        }
        return y;
    }

    public String getName(){
        return "Navigator";
    }

    public module.PlayerBag.playerType getType() {
        return type;
    }

    @Override
    public void resetSpecialAbility() {

    }

    public List<TreasureCard> getBag() {
        return NavigatorBag;
    }
}