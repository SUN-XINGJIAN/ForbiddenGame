package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

//package module;
//
//import board.Tile;
//
//public class Pilot extends Player {
//    private boolean specialFlightUsed = false;
//
//    public Pilot(String name, Tile startingTile) {
//        super(name, startingTile);
//    }
//
//    @Override
//    public void move(Tile targetTile) {
//        if (!specialFlightUsed && !targetTile.isRemoved()) {
//            this.currentTile = targetTile;
//            specialFlightUsed = true;
//            int actionRemained = getActionsRemaining();
//            actionRemained--;
//        } else {
//            super.move(targetTile);
//        }
//    }
//}
public class Pilot extends Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Pilot;
    private List<TreasureCard> PilotBag = new ArrayList<>();
    public Pilot(String name){
        super("Diver");

        pawnName = "/image/Pawns/@2x/Pilot@2x.png";
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
            if(tile.getName().equals("14")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }
    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("14")){
                y = tile.getPositionY();           }
        }
        return y;
    }
    public String getName(){
        return "Pilot";
    }

    public module.PlayerBag.playerType getType() {
        return type;
    }

    @Override
    public List<TreasureCard> getBag() {
        return PilotBag;
    }
}