package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

//
//public class Engineer extends Player {
//
//    public Engineer(String name, Tile startingTile) {
//        super(name, startingTile);
//    }
//
//    @Override
//    public void shoreUp(Tile targetTile) {
//        if (getActionsRemaining() > 0 && !targetTile.isRemoved() && targetTile.isFlooded()) {
//            targetTile.unflood();
//            int actionRemained = getActionsRemaining();
//            actionRemained--;
//        }
//    }
//}
public class Engineer extends module.Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Engineer;
    private List<TreasureCard> EngineerBag = new ArrayList<>();



    public Engineer(String name){
        super("Diver");
        pawnName = "/image/Pawns/@2x/Engineer@2x.png";
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

    public String getName(){
        return "Engineer";
    }
    public module.PlayerBag.playerType getType() {
        return type;
    }

    @Override
    public List<TreasureCard> getBag() {
        return EngineerBag;
    }

}