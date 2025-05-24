package board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile extends Canvas {
    private String tileName1,tileName2;
    private int num;
    private int state;
    private int positionX,positionY;

    public Tile(int number, int x, int y){
        super(50,50);
        this.positionX = x;
        this.positionY = y;
        this.setLayoutX(x);
        this.setLayoutY(y);
        num = number;
        tileName1 = "/image/Tiles/" + number + ".png";

        tileName2 = "/image/SubmersedTiles/" + number + ".png";

    }




    public void draw(){
        // 如果 state >= 2，不绘制
        if (state >= 2) {
            return;
        }

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        if(state == 0) {
            gc.drawImage(new Image(getClass().getResourceAsStream(tileName1)), 0, 0, getWidth(), getHeight());
        }else if(state==1){
            gc.drawImage(new Image(getClass().getResourceAsStream(tileName2)), 0, 0, getWidth(), getHeight());
        }
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public void incrementState() {
        this.state++;
    }


    public String getTileName1() {
        return tileName1;
    }

    public int getPositionX(){
        return positionX;
    }

    public int getPositionY(){
        return positionY;
    }


    // **下面的是新加的，为其他类正确运行所需要的方法，还没有进行整理
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
    private Treasure.Type treasureType;

    public boolean isRemoved() { return removed; }
    /* 检查状态 */
    public boolean isFlooded() { return flooded; }
    public void unflood() {
        if(flooded) {
            this.flooded = false;
            decrementState();
        }
    }
    private void decrementState() {
        state = Math.max(0, state-1);
    }

    public Treasure.Type getTreasureType() {
        return treasureType;
    }

    // 洪水牌相关逻辑
    /* 从地图移除 */
    public void removeFromBoard() {
        this.removed = true;
        this.flooded = false;
    }

    /* 设置为淹没状态 */
    public void flood() {
        if(!flooded) {
            this.flooded = true;
            incrementState();
        }
    }

    /* 返回坐标对象 */
    public boolean getPosition() {
        return false;
    }

    private boolean isTreasureSite;
    public boolean isTreasureSite() {
        return isTreasureSite;
    }

    public String getName() {
        return String.valueOf(num);
    }
}