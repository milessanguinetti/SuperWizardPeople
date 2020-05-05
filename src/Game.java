import javafx.geometry.Rectangle2D;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Game {
    private static GraphicsRoot graphicsRoot;
    private static MediaPlayer musicPlayer; //plays any music that we might need
    private static MediaPlayer stoppablePlayer; //player for effects that occur often enough
                                                //to require stopping in the interest of performance
    private static Game currentGame; //a static reference to the current game.
    private static double ScreenWidth;
    private static double ScreenHeight;
    private static Stage currentStage;
    private static final int EffectsVolume = 1;
    private static final double Scale = .1;

    public Game(Stage stage){
        currentStage = stage;
        currentGame = this;
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        ScreenWidth = primScreenBounds.getWidth();
        ScreenHeight = primScreenBounds.getHeight();
        graphicsRoot = new GraphicsRoot(stage);
        //graphicsRoot.InitializePlayerBars();
        graphicsRoot.requestFocus();
    }

    public static double getScreenWidth(){
        return ScreenWidth;
    }

    public static double getScreenHeight() {
        return ScreenHeight;
    }

    public static void playMedia(String toPlay) {
        Media media = new Media(currentGame.getClass().getResource("AudioFiles/"
                + toPlay + "!.mp3").toExternalForm());
        MediaPlayer tempplayer = new MediaPlayer(media);
        tempplayer.setCycleCount(1);
        tempplayer.play();
    }

    public static void playStoppableMedia(String toPlay){
        if(stoppablePlayer != null){
            stoppablePlayer.stop();
            stoppablePlayer.dispose();
        }
        Media media = new Media(currentGame.getClass().getResource("AudioFiles/"
                + toPlay + "!.mp3").toExternalForm());
        stoppablePlayer = new MediaPlayer(media);
        stoppablePlayer.setCycleCount(1); //repeat indefinitely
        stoppablePlayer.play();
    }

    public static void playMusic(String toPlay){
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
        //mediaPlayer = new MediaPlayer(new Media(currentGame.getClass().getResource(toPlay + ".mp3").toString()));
        Media media = new Media(currentGame.getClass().getResource(toPlay).toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); //repeat indefinitely
        musicPlayer.play();
    }

    public static GraphicsRoot getGraphicsRoot(){
        return graphicsRoot;
    }

    public static double getScale(){
        return Scale;
    }
}
