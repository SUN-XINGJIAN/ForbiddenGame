package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

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
