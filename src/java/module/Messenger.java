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
//public class Messenger extends Player {
//    public Messenger(String name, Tile startingTile) {
//        super(name, startingTile);
//    }
//
////    @Override
////    public void giveTreasureCard(Card card, Player recipient) {
////        // 移除同板块限制
////        if (this.hand.contains(card)) {
////            this.hand.remove(card);
////            recipient.receiveCard(card);
////            actionsRemaining--;
////        }
////    }
//}
public class Messenger extends Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Messenger;
    private List<TreasureCard> MessengerBag = new ArrayList<>();
    public Messenger(String name){
        super("Diver");

        pawnName = "/image/Pawns/@2x/Messenger@2x.png";
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
            if(tile.getName().equals("12")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }
    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("12")){
                y = tile.getPositionY();           }
        }
        return y;
    }
    public String getName(){
        return "Messenger";
    }

    public module.PlayerBag.playerType getType() {
        return type;
    }
    public List<TreasureCard> getBag() {
        return MessengerBag;
    }
}