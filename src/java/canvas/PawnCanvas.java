package canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PawnCanvas extends Canvas {

    private int x,y;

    public PawnCanvas(int x, int y) {
        super(20, 50);
        this.setLayoutX(x);
        this.setLayoutY(y);

    }
    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream("/image/Pawns/@2x/Diver@2x.png")),0,0,getWidth(),getHeight());
    }


    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public  int getX() {
        return x;
    }
    public  int getY() {
        return y;
    }
}
