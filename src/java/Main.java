import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    // Initializes and launches the game ui
    @Override
    public void start(Stage stage) throws Exception {
        Director.getInstance().init(stage);
        System.out.println("Initial panel is loaded");

    }

    // Launches JavaFX application
    public static void main(String[] args) {
        Application.launch(args);
    }
}