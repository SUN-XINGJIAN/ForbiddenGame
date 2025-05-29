package board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class WaterMeter extends Canvas {
    private String pictureName;
    public int stage;

    public WaterMeter(int number) {
        super(105,315);
        this.setLayoutX(541);
        this.setLayoutY(194);
        pictureName = "/image/WaterMeter/" + number + ".png";

        // Determine the number of flood cards drawn based on water level
        if(number <= 2 && number >= 1){
            stage = 2;
        }else if(number <= 5 && number >= 3){
            stage = 3;
        }else if(number <= 7 && number >= 6){
            stage = 4;
        }else if(number <= 9 && number >= 8){
            stage = 5;
        }
    }

    // Provides the water meter on the panel
    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(pictureName)), 0, 0, getWidth(), getHeight());
    }

    public int getStage(){
        return stage;
    }
}
