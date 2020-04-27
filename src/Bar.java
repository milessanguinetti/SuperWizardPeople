import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Bar extends StackPane {
    private static int barWidth = 300;
    private static int barHeight = 40;
    private Rectangle BarShape;
    private Rectangle CoverShape;
    private Color original;

    public Bar(Color barColor, String name, int Facing) {
        this.setAlignment(Pos.CENTER);
        this.original = barColor;
        this.BarShape = new Rectangle((double)barWidth, (double)barHeight);
        this.BarShape.setFill(barColor);
        this.BarShape.setTranslateX(0.0D);
        this.BarShape.setTranslateY(0.0D);
        this.getChildren().add(this.BarShape);
        this.CoverShape = new Rectangle(0.0D, (double)barHeight);
        this.CoverShape.setFill(Color.BLACK);
        this.CoverShape.setTranslateX((double)(barWidth / 2));
        this.CoverShape.setTranslateY(0.0D);
        this.getChildren().add(this.CoverShape);
        Text text = new Text();
        text.setFont(Font.font("Typewriter", 20.0D));
        text.setFill(Color.WHITE);
        text.setText(name);
        text.setTranslateX((double)(barWidth / -2 * Facing - 25 * Facing));
        text.setTranslateY(0.0D);
        this.getChildren().add(text);
    }

    public void setPercentFull(float percent) {
        if (percent < 0.0F) {
            percent = 0.0F;
        }

        this.CoverShape.setWidth((double)((float)barWidth - (float)barWidth * percent + 1.0F));
        this.CoverShape.setTranslateX((double)((float)barWidth * percent / 2.0F));
    }

    public void setTempColor(Color toChange) {
        Timeline colorTimeline = new Timeline();
        this.BarShape.setFill(toChange);
        KeyFrame kf = new KeyFrame(Duration.millis(200.0D), (ae) -> {
            this.BarShape.setFill(this.original);
        }, new KeyValue[0]);
        colorTimeline.getKeyFrames().add(kf);
        colorTimeline.setCycleCount(1);
        colorTimeline.play();
    }
}
