import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SplashScreen extends StackPane {
    private boolean loading = false;
    private Rectangle startFlash;
    //PLACEHOLDER

    //PLACEHOLDER


    public SplashScreen(){
        setAlignment(Pos.CENTER);
        Rectangle border = new Rectangle(3000, 3000, Color.BLACK);
        getChildren().add(border);
        ImageView Background;
        try(InputStream imginput = Files.newInputStream(Paths.get("sprites/Turtle Fire Start Screen.png"))){
            Background = new ImageView(new Image(imginput));
            Rectangle2D screensize = Screen.getPrimary().getBounds();
            Background.setScaleY(screensize.getHeight()/480);
            Background.setScaleX(screensize.getHeight()/480);
            getChildren().add(Background); //add sprite to the stackpane.
            startFlash = new Rectangle(screensize.getWidth()/4, screensize.getHeight()/8, Color.BLACK);
            startFlash.setTranslateY(screensize.getHeight()/3);
            getChildren().add(startFlash);
            Timeline flashTimeLine = new Timeline();
            final KeyFrame kf = new KeyFrame(Duration.millis(1000), ae ->{
                if(startFlash.visibleProperty().get()){
                    startFlash.setVisible(false);
                }
                else{
                    startFlash.setVisible(true);
                }
            });
            flashTimeLine.getKeyFrames().add(kf);
            flashTimeLine.setCycleCount(Timeline.INDEFINITE);
            flashTimeLine.play();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Error loading splash art.");
        }
    }

    public void handleRelease(KeyEvent event){
        if(event.getCode() == KeyCode.ESCAPE)
            System.exit(0); //exit the game.
        if(loading)
            return;
        loading = true;
        Timeline audio = new Timeline();
        Timeline start = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(300),
                e -> {
                    Game.playMedia("DefaultFire");
                });
        KeyFrame kf2 = new KeyFrame(Duration.millis(900),
                e -> {
                    Game.getGraphicsRoot().swapToGame();
                    loading = false;
                });
        audio.getKeyFrames().add(kf1);
        audio.setCycleCount(3);
        audio.play();
        start.getKeyFrames().add(kf2);
        start.setCycleCount(1);
        start.play();
        //Game.getGraphicsRoot().swapToGame();
    }
}
