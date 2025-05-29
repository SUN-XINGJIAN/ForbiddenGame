package board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Treasure extends Canvas {
    private String treasureName;
    public enum Type {
        EARTH_STONE,
        CRYSTAL_OF_FIRE,
        OCEAN_CHALICE,
        WIND_STATUE
    }

    public Treasure(int number,int x,int y) {
        super(50,50);
        this.setLayoutX(x);
        this.setLayoutY(y);
        treasureName = "/image/Treasure/" + number + ".png";
    }

    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(treasureName)), 0, 0, getWidth(), getHeight());
    }

}
