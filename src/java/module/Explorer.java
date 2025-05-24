package module;

import board.Tile;
import cards.TreasureCard;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.ForbiddenGameStarted;

import java.util.ArrayList;
import java.util.List;

//
//public class Explorer extends Player {
//    public Explorer(String name, Tile startingTile) {
//        super(name, startingTile);
//    }
//
//    protected boolean isAdjacent(Tile targetTile) {
//        // 需要 Tile 类实现 getCoordinates() 方法返回坐标对象
//        return getCurrentTile().isDiagonallyAdjacent(targetTile); // 扩展相邻判断逻辑
//    }
//
//    @Override
//    public void shoreUp(Tile targetTile) {
//        if (isAdjacent(targetTile) && !targetTile.isRemoved()) { // 允许对角线加固
//            super.shoreUp(targetTile);
//        }
//    }
//}
public class Explorer extends module.Player {
    private List<Tile> t = new ArrayList<>();
    private String pawnName;
    private module.PlayerBag.playerType type = module.PlayerBag.playerType.Explorer;
    private List<TreasureCard> ExplorerBag = new ArrayList<>();
    public Explorer(String name){
        super("Diver");
        pawnName = "/image/Pawns/@2x/Explorer@2x.png";
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
            if(tile.getName().equals("11")){
                x = tile.getPositionX()+30;
            }
        }
        return x;
    }
    public int getPositionY(ForbiddenGameStarted forbiddenGameStarted){
        int y=0;
        t = forbiddenGameStarted.getTiles();
        for(Tile tile : t){
            if(tile.getName().equals("11")){
                y = tile.getPositionY();           }
        }
        return y;
    }
    public String getName(){
        return "Explorer";
    }
    public module.PlayerBag.playerType getType() {
        return type;
    }

    public List<TreasureCard> getBag(){
        return ExplorerBag;
    }
}