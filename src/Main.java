import javafx.application.Application;
import javafx.stage.Stage;

/**
 *Main class for an RPG-platformer with no working title
 *
 */
public class Main extends Application {
    public static void main(String[] args){ launch(); }

    public void start(Stage primaryStage) throws Exception{
        Game newGame = new Game(primaryStage);
    }
}
