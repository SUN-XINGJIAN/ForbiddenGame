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
    private boolean specialFlightUsed = false;

    public Pilot(String name){
        super("Pilot");

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

    public void resetSpecialFlight() {
        specialFlightUsed = false;
    }

    public boolean useSpecialFlight() {
        if (!specialFlightUsed) {
            specialFlightUsed = true;
            return true;
        }
        return false;
    }

    public boolean isSpecialFlightUsed() {
        return specialFlightUsed;
    }

    @Override
    public void resetSpecialAbility() {
        this.specialFlightUsed = false;
    }

    public void setSpecialFlightUsed(boolean used) {
        this.specialFlightUsed = used;
    }

    public void syncState(Pilot other) {
        this.specialFlightUsed = other.specialFlightUsed;
    }

    @Override
    public void useSpecialAbility(ForbiddenGameStarted forbiddenGameStarted,Player player) {
        super.useSpecialAbility(forbiddenGameStarted, player);
            for (Tile tile : forbiddenGameStarted.getTiles()) {
                tile.setOnMouseClicked(e -> {
                    if(!forbiddenGameStarted.isSpecialMode){
                        return;
                    }
                    if (tile.getState() == 0) {
                        int targetX = (int) tile.getLayoutX();
                        int targetY = (int) tile.getLayoutY();

                        player.setLayoutX(targetX + 30);
                        player.setX(targetX);
                        player.setLayoutY(targetY);
                        player.setY(targetY);
                        player.draw();
                        forbiddenGameStarted.checkTreasureSubmit();
                        forbiddenGameStarted.exchangeCards();

                        forbiddenGameStarted.turnManage.useStep();
                        forbiddenGameStarted.turnManage.showRemainSteps();
                        forbiddenGameStarted.step = forbiddenGameStarted.turnManage.getStep();
                        forbiddenGameStarted.changeCurrentPlayer();


                        forbiddenGameStarted.pilotCount++;
                        forbiddenGameStarted.isSpecialMode = false;
                    }else{
                        forbiddenGameStarted.showMessage("This tile is already flood!");
                    }

                });
                }


    }
}