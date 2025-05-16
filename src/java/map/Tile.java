package map;

public class Tile {
    // 为进行player类的书写：tile类需实现：
    // 坐标系统相关方法
    public static boolean isAdjacent(Tile other) { return false;}
    public boolean isDiagonallyAdjacent(Tile other) { return false;} // Explorer
    public static boolean isPathValidForDiver(Tile target) {
        return false;
    } // Diver

    // 状态相关
    private boolean removed;
    private boolean flooded;
    private String treasureType;

    public boolean isRemoved() { return removed; }
    public boolean isFlooded() { return flooded; }
    public void unflood() { flooded = false; }
    public String getTreasureType() { return treasureType; }
}
