import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
            GraphicsRoot.setPlayerOneProjectiles(new Projectile(Angle, 0,
                    GraphicsRoot.getPlayerOneProjectiles(), GraphicsRoot.getGameplayX(), GraphicsRoot.getGameplayY()));
            ++Ticks;
        }, new KeyValue[0]);
        moveTimeline.getKeyFrames().add(kf);
        moveTimeline.setCycleCount(Cycles);
        moveTimeline.play();
    }
}
