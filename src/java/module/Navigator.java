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
//public class Navigator extends Player {
//    public Navigator(String name, Tile startingTile) {
//        super(name, startingTile);
//    }
//
//    public void directMove(Player otherPlayer, Tile targetTile) {
//        if (this.getCurrentTile().equals(otherPlayer.getCurrentTile())
//                && targetTile.isAdjacent(otherPlayer.getCurrentTile())
//                && !targetTile.isRemoved()) {
//            otherPlayer.move(targetTile);
//            int actionRemained = getActionsRemaining();
//            actionRemained--;
//        }
//    }
//}
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

        // 启用对其他玩家的监听
        for (Player p : forbiddenGameStarted.currentPlayers) {
            if (!p.equals(player)) { // 排除 currentPlayer
                for (PlayerBag pb : forbiddenGameStarted.playerBags) {
                    if (pb.getPlayerType().equals(p.getType())) {
                        pb.setDisable(false); // 启用该玩家的控件
                        pb.setOnMouseClicked(e -> {
                            forbiddenGameStarted.isMoveMode = !forbiddenGameStarted.isMoveMode;  // 切换移动模式
                            if (forbiddenGameStarted.isMoveMode) {
                                forbiddenGameStarted.enableTileMovement(p);  // 启动每次点击时可以移动棋子的模式
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