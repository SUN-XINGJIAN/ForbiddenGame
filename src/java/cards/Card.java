package cards;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

//public abstract class Card {
//    protected final CardType type;
//    protected final String name;
//
//    protected Card(CardType type, String name) {
//        this.type = type;
//        this.name = name;
//    }
//
//    public abstract void playEffect();
//
//    // Getters
//    public CardType getType() { return type; }
//    public String getName() { return name; }
//}
public class Card extends Canvas {

    public String cardname;

    public Card(){
        super(50,69);
    }

    public void draw(){

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(cardname)), 0, 0, getWidth(), getHeight());
    }
}