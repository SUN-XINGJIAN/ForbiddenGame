package cards;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Card extends Canvas {
    public String cardname;

    public Card(){
        super(50,69);
    }

    // Provides the card image on the panel
    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(cardname)), 0, 0, getWidth(), getHeight());
    }
}