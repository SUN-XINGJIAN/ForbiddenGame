import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Director {

    private static Director instance = new Director();
    private Stage stage;
    public static final int FRAME_WIDTH = 900,FRAME_HEIGHT = 900;


    public Director(){}

    public void init(Stage stage){
        this.stage = stage;
        InitialScene(stage);

    }

    public void InitialScene(Stage stage){
        Frame.load(stage);
    }


    public static Director getInstance(){
        return instance;
    }

    public Stage getStage() {
        return stage;
    }
}