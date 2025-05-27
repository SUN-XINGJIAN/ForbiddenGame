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

        // 触发特殊技能时，玩家点击地图的版块
        Tile currentTile = forbiddenGameStarted.getTileByPlayer(player); // 获取 Diver 当前所在的 Tile

        // 假设目标 Tile 是玩家点击的目标版块（你可以通过 UI 传递目标 Tile）
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
                    // 如果目标版块不可到达，显示提示
                    forbiddenGameStarted.showMessage("No valid water path to this tile.");
                }
            });
        }

    }

    // 判断是否存在水路从当前版块到目标版块
    private boolean canReachByWater(Tile startTile, Tile targetTile, ForbiddenGameStarted forbiddenGameStarted) {
        // 使用 BFS 判断水路连接
        Queue<Tile> queue = new LinkedList<>();
        List<Tile> visited = new ArrayList<>();
        queue.add(startTile);
        visited.add(startTile);

        while (!queue.isEmpty()) {
            Tile currentTile = queue.poll();
            // 如果当前版块就是目标版块，返回 true
            if (currentTile.equals(targetTile)) {
                return true;
            }

            // 获取相邻的版块
            List<Tile> neighbors = getNeighbors(currentTile, forbiddenGameStarted);
            for (Tile neighbor : neighbors) {
                if (!visited.contains(neighbor) && !neighbor.isRemoved()) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        // 如果 BFS 完成后没有找到路径，返回 false
        return false;
    }

    // 获取与当前版块相邻的所有版块
    private List<Tile> getNeighbors(Tile currentTile, ForbiddenGameStarted forbiddenGameStarted) {
        List<Tile> neighbors = new ArrayList<>();
        t = forbiddenGameStarted.getTiles(); // 获取所有的 Tile

        // 获取相邻的版块（根据当前版块位置判断上下左右）
        for (Tile tile : t) {
            if (isAdjacent(currentTile, tile)) {
                neighbors.add(tile);
            }
        }
        return neighbors;
    }

    // 判断两个版块是否相邻
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
