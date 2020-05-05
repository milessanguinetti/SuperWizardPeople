import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

public class MoveTest {
    private long Start;
    private double Duration;
    private int Ticks = 0;
    private final int Cycles = 40;

    public MoveTest(double dur){
        Duration = dur;
        Timeline moveTimeline = new Timeline();
        KeyFrame kf = new KeyFrame(javafx.util.Duration.millis(Duration/Cycles), (ae) -> {
            double Angle = -Ticks*(Math.PI/20);
            Player p = GraphicsRoot.getPlayer();
            GraphicsRoot.setPlayerOneProjectiles(new Projectile(Angle, 20*Ticks,
                    GraphicsRoot.getPlayerOneProjectiles(), GraphicsRoot.getGameplayX(), GraphicsRoot.getGameplayY()));
            ColorAdjust Adjust = new ColorAdjust();
            Adjust.setHue(-1+(Ticks%20)*.1);
            GraphicsRoot.getPlayerOneProjectiles().getChildren().get(0).setEffect(Adjust);
            GraphicsRoot.getPlayerOneProjectiles().getChildren().get(1).setEffect(Adjust);
            ++Ticks;
        }, new KeyValue[0]);
        moveTimeline.getKeyFrames().add(kf);
        moveTimeline.setCycleCount(Cycles);
        moveTimeline.play();
    }
}
