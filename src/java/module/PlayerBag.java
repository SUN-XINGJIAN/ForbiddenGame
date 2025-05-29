package module;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerBag extends Canvas {
    private String bagName;
    private playerType p;

    public enum playerType{
        Diver,Engineer,Explorer,Messenger,Navigator,Pilot
    }

    public PlayerBag(playerType playerType){
        super(50, 69);
        p = playerType;
        switch (playerType){
            case Diver:
                bagName = "/image/Adventurers/Diver.png";
                break;
            case Engineer:
                bagName = "/image/Adventurers/Engineer.png";
                break;
            case Explorer:
                bagName = "/image/Adventurers/Explorer.png";
                break;
            case Messenger:
                bagName = "/image/Adventurers/Messenger.png";
                break;
            case Navigator:
                bagName = "/image/Adventurers/Navigator.png";
                break;
            case Pilot:
                bagName = "/image/Adventurers/Pilot.png";
                break;
        }
    }

    public void draw(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.drawImage(new Image(getClass().getResourceAsStream(bagName)), 0, 0, getWidth(), getHeight());
    }

    public String getBagName(){
        return bagName;
    }
    public playerType getPlayerType(){
        return p;
    }
}
