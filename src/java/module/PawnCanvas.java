package module;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PawnCanvas extends Canvas {

    private int x,y;
    private String pawnName;

    public PawnCanvas(String name) {
        super(20, 50);

        pawnName = "/image/Pawns/@2x/" + name +"@2x.png";
    }
    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(pawnName)),0,0,getWidth(),getHeight());
    }


    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public  int getY() {
        return y;
    }
}
