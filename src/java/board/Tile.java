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
        // If state >= 2, don't draw
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

    // State related
    private boolean removed;

    public boolean isRemoved() { return removed; }




    public String getName() {
        return String.valueOf(num);
    }
}