import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

public class GraphicsRoot extends StackPane {
    private static StackPane gameRootPane;
    private static StackPane gamePlayPane; //pane for all gameplay elements except player
    private static StackPane playerPane; //pane strictly for player sprite
    private static SplashScreen splashscreen;
    private int Mode = 0;
    /*
        0 = start menu
        1 = gameplay
        2 = high score screen
    */
    private Stage PrimaryStage;
    private final int UpdateTime = 40;
    private static Player PlayerOne;
    //private Player PlayerTwo;
    private static Projectile PlayerOneProjectiles;
    //private Projectile PlayerTwoProjectiles;
    private boolean isGameOver = false;
    private Text gameOverText;
    private int Cycles = 0;

    public GraphicsRoot(Stage stage){
        if(PrimaryStage == null) {
            PrimaryStage = stage;
        }
        if(gameRootPane == null){
            gameRootPane = new StackPane();
            gameRootPane.setAlignment(Pos.CENTER);
            playerPane = new StackPane();
            playerPane.setAlignment(Pos.CENTER);
            gamePlayPane = new StackPane();
            gamePlayPane.setAlignment(Pos.CENTER);
            gameRootPane.getChildren().add(gamePlayPane);
            gameRootPane.getChildren().add(playerPane);
            splashscreen = new SplashScreen();
            swapToStartMenu();
            PrimaryStage.setScene(new Scene(this));
        }
        Initialize();
    }

    public void Initialize(){
        Mode = 0;
        gameOverText = new Text();
        gameOverText.setFont(Font.font("Typewriter", 70));
        gameOverText.setFill(Color.DARKGOLDENROD);
        gameOverText.setTranslateX(0);
        gameOverText.setTranslateY(0);

        Rectangle background = new Rectangle(Game.getScreenWidth(), Game.getScreenHeight() + 100);
        background.setFill(Color.BLACK);

        PrimaryStage.setTitle("Turtle Fire");
        PrimaryStage.setFullScreen(true);
        PrimaryStage.setResizable(false);
        PrimaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        setAlignment(Pos.CENTER);
        gameRootPane.getChildren().add(background);
        background.toBack();
        //TEST
        gameStage.setCurrentGameStage(new gameStage("background"));
        gamePlayPane.getChildren().add(gameStage.getCurrentGameStage());
        double testTangleX = 200;
        double testTangleY   = 250;
        GameplayEntity testTangle = new GameplayEntity(testTangleX, testTangleY, null, null);
        gameStage.getCurrentGameStage().addEntity(testTangle);
        testTangle.setTranslateX(300);
        testTangle.setTranslateY(50);
        Rectangle testShape = new Rectangle(testTangleX, testTangleY);
        testShape.setFill(Color.BLACK);
        testTangle.addNode(testShape);

        double testTangleX2 = 300;
        double testTangleY2   = 150;
        GameplayEntity testTangle2 = new GameplayEntity(testTangleX2, testTangleY2, null, null);
        gameStage.getCurrentGameStage().addEntity(testTangle2);
        testTangle2.setTranslateX(-300);
        testTangle2.setTranslateY(50);
        Rectangle testShape2 = new Rectangle(testTangleX2, testTangleY2);
        testShape2.setFill(Color.MAGENTA);
        testTangle2.addNode(testShape2);
        //TEST

        setOnKeyPressed(event -> {
            if(Mode == 1)
                handlePress(event);
            event.consume();
        });

        setOnKeyReleased(event1 -> {
            if(Mode == 0){
                splashscreen.handleRelease(event1);
            }
            else if(Mode == 1){
                handleRelease(event1);
            }
            else if(Mode == 2){

            }
            event1.consume();
        });

        setOnMouseReleased(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY))
                handleClick(false, true);
            else
                handleClick(false, false);
            event.consume();
        });

        setOnMousePressed(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY))
                handleClick(true, true);
            else
                handleClick(true, false);
            event.consume();
        });



        PrimaryStage.show();
        Update();


    }

    public void swapToStartMenu(){
        Game.playMusic("AudioFiles/Theme.mp3");
        Mode = 0;
        getChildren().remove(gameRootPane);
        getChildren().add(splashscreen);
    }

    public void swapToGame(){
        Game.playMusic("AudioFiles/Theme.mp3");
        Mode = 1;
        InitializePlayerBars();
        isGameOver = false;
        getChildren().remove(splashscreen);
        getChildren().add(gameRootPane);
    }

    public void InitializePlayerBars(){
        gameRootPane.getChildren().remove(gameOverText);
        if(PlayerOne != null)
            PlayerOne.RemoveFromPane();
        //if(PlayerTwo != null)
            //PlayerTwo.RemoveFromPane();
        PlayerOne = new Player();
        //PlayerTwo = new Player(1);
        gameRootPane.getChildren().add(PlayerOne);
        //gameRootPane.getChildren().add(PlayerTwo);

        PlayerOne.InitializeBars();
        //PlayerTwo.InitializeBars();
    }

    public void Update(){
        Timeline MovementTimeLine = new Timeline();
        final KeyFrame kf = new KeyFrame(
                Duration.millis(UpdateTime),
                ae -> {
                    if(PlayerOne == null)
                        return;
                    if(PlayerOne.isDead() && !isGameOver){
                        PlayerOne.endMovement(); //ensure sprites don't keep moving after the game ends
                        //PlayerTwo.endMovement();
                        if(PlayerOneProjectiles != null)
                            PlayerOneProjectiles.recursivelyNullify();
                        //if(PlayerTwoProjectiles != null)
                        //    PlayerTwoProjectiles.recursivelyNullify();
                        isGameOver = true;
                        if(PlayerOne.isDead()) {
                            gameOverText.setText("Player Two Wins!");
                            Timeline victory = new Timeline();
                            final KeyFrame vkf = new KeyFrame(Duration.millis(350),
                                    e -> {
                                        Game.playMedia("Win");
                                    });
                            victory.getKeyFrames().add(vkf);
                            victory.setCycleCount(1);
                            victory.play();
                        }
                        else {
                            gameOverText.setText("Player One Wins!");
                            Timeline victory = new Timeline();
                            final KeyFrame vkf = new KeyFrame(Duration.millis(350),
                                    e -> {
                                        Game.playMedia("Win");
                                    });
                            victory.getKeyFrames().add(vkf);
                            victory.setCycleCount(1);
                            victory.play();
                        }
                        gameRootPane.getChildren().remove(gameOverText);
                        gameRootPane.getChildren().add(gameOverText);
                        MovementTimeLine.setCycleCount(0);
                    }
                    if(!isGameOver) {
                        if (PlayerOne.isInvincible()) {
                            if (!PlayerOne.DecrementStamina(1, true))
                                PlayerOne.setInvulnerable(false);
                        }
                        PlayerOne.Move();
                        //PlayerTwo.Move();

                        if (Cycles % 2 == 0) {
                            PlayerOne.DecrementStamina(-4, false);
                            //PlayerTwo.DecrementStamina(-2, false);
                        } else {
                            PlayerOne.DecrementStamina(-2, false);
                            //PlayerTwo.DecrementStamina(-1, false);
                        }
                    }
                    if (PlayerOneProjectiles != null)
                        PlayerOneProjectiles = PlayerOneProjectiles.moveAndCheckForHit(null);
                    //if (PlayerTwoProjectiles != null)
                    //    PlayerTwoProjectiles = PlayerTwoProjectiles.moveAndCheckForHit(PlayerOne);
                });
        MovementTimeLine.getKeyFrames().add(kf);
        MovementTimeLine.setCycleCount(Timeline.INDEFINITE);
        MovementTimeLine.play();
    }

    public javafx.collections.ObservableList getRootChildren(){
        return gameRootPane.getChildren();
    }

    public void handlePress(KeyEvent event) {
        if (!isGameOver) {
            switch (event.getCode()) {
                case W: {
                    PlayerOne.PressYKey(-1);
                    break;
                }
                case A: {
                    PlayerOne.PressXKey(-1);
                    break;
                }
                case S: {
                    PlayerOne.PressYKey(1);
                    break;
                }
                case D: {
                    PlayerOne.PressXKey(1);
                    break;
                }
                case E: {
                    PlayerOne.setCharge();
                    break;
                }
                case DIGIT1: {
                    break;
                }
                case BACK_QUOTE: {
                    PlayerOne.setInvulnerable(true);
                    break;
                }
                case Q: {
                    PlayerOne.setInvulnerable(true);
                    break;
                }
            }
        }
    }

    public void handleRelease(KeyEvent event){
        if (!isGameOver) {
            switch (event.getCode()) {
                case W: {
                    PlayerOne.ReleaseYKey(-1);
                    break;
                }
                case A: {
                    PlayerOne.ReleaseXKey(-1);
                    break;
                }
                case S: {
                    PlayerOne.ReleaseYKey(1);
                    break;
                }
                case D: {
                    PlayerOne.ReleaseXKey(1);
                    break;
                }
                case Q: {
                    PlayerOne.setInvulnerable(false);
                    break;
                }
                case BACK_QUOTE: {
                    PlayerOne.setInvulnerable(false);
                }
                case E: {
                    Fire();
                    break;
                }
                case DIGIT1: {
                    new MoveTest(1000);
                    break;
                }
                case ESCAPE: {
                    System.exit(0); //exit the game.
                    break;
                }
            }
        } else {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0); //exit the game.
            }
            if (event.getCode() == KeyCode.ENTER) {
                //swapToHighScoreScreen();
            }
        }
    }

    public void handleClick(boolean isDown, boolean isPrimary){
        if (!isGameOver && Mode == 1) {
            if(!isDown && isPrimary) {
                Fire();
            } else if(isDown && isPrimary) {
                PlayerOne.setCharge();
            } else if(isDown && !isPrimary){
                //presently does nothing
            } else if(!isDown && !isPrimary){
                PlayerOne.endMovement();
                PlayerOne.applyMovementVector(5, calcMouseAngle());
            }
        }
    }

    private double calcMouseAngle(){
        double mousex = Game.getScreenWidth()/-2 + MouseInfo.getPointerInfo().getLocation().x/1.5;
        double mousey = Game.getScreenHeight()/2-MouseInfo.getPointerInfo().getLocation().y/1.58823529412;
        //System.out.println(Game.getScreenHeight() + "..." + MouseInfo.getPointerInfo().getLocation().y + "..." + mousey);
        if(mousex == PlayerOne.getTranslateX()) {
            if(mousey < PlayerOne.getTranslateX())
                return Math.PI;
            return 0;
        }
        double ydif = mousey + PlayerOne.getTranslateY();
        double xdif = mousex - PlayerOne.getTranslateX();
        double returnval = Math.PI/2;
        if (ydif > 0)
            returnval += Math.PI;
        return returnval + Math.atan(xdif/ydif);
    }

    private void Fire() {
        long timeSpentCharging = PlayerOne.releaseCharge();
        if (timeSpentCharging == -1) {
            return;
        }
        if (PlayerOne.DecrementStamina(Math.round(15 + (timeSpentCharging / Projectile.maxChargeTime * 80f)),
                false)) {
            double Angle = calcMouseAngle();
            PlayerOneProjectiles = new Projectile(Angle, Math.round(timeSpentCharging), PlayerOneProjectiles,
                    gamePlayPane.getTranslateX()*-1, gamePlayPane.getTranslateY()*-1);
            if(timeSpentCharging == Projectile.maxChargeTime)
                PlayerOne.applyMovementVector(2, Angle +  Math.PI);
        }
    }

    public static Player getPlayer(){
        return PlayerOne;
    }

    public static Projectile getPlayerOneProjectiles(){
        return PlayerOneProjectiles;
    }

    public static void setPlayerOneProjectiles(Projectile toSet){
        PlayerOneProjectiles = toSet;
    }

    public static void translateGameplayPaneX(double x){
        gamePlayPane.setTranslateX(x*-1);
    }

    public static void translateGameplayPaneY(double y){
        gamePlayPane.setTranslateY(y*-1);
    }

    public static double getGameplayX(){
        return gamePlayPane.getTranslateX()*-1;
    }

    public static double getGameplayY(){
        return gamePlayPane.getTranslateY()*-1;
    }

    public static StackPane getGamePlayPane(){
        return gamePlayPane;
    }

    public static StackPane getPlayerPane(){
        return playerPane;
    }
}