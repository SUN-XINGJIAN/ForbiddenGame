package canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile extends Canvas {
    private String tileName1,tileName2;
    private int state;


    public Tile(int number, int x, int y){
        super(50,50);
        this.setLayoutX(x);
        this.setLayoutY(y);

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
}
