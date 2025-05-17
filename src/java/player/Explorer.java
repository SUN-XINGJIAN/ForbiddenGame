package player;

import board.Tile;

public class Explorer extends Player{
    public Explorer(String name, Tile startingTile) {
        super(name, startingTile);
    }

    protected boolean isAdjacent(Tile targetTile) {
        // 需要 Tile 类实现 getCoordinates() 方法返回坐标对象
        return getCurrentTile().isDiagonallyAdjacent(targetTile); // 扩展相邻判断逻辑
    }

    @Override
    public void shoreUp(Tile targetTile) {
        if (isAdjacent(targetTile) && !targetTile.isRemoved()) { // 允许对角线加固
            super.shoreUp(targetTile);
        }
    }
}
