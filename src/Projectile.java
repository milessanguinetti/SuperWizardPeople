import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Projectile extends StackPane {
    private Projectile Next;
    private Projectile Previous;
    private int Radius;
    private int Speed;
    private double Angle;
    private int Damage;
    private static final int MaxRadiusBonus = 40;
    private static final int MinRadius = 20;
    private static final int MaxSpeedBonus = 150;
    private static final int MinSpeed = 50;
    private static final int MaxDamageBonus = 58;
    private static final int MinDamage = 15;
    public static final long maxChargeTime = 500L;
    public static final long minChargeTime = 250L;
    private ImageView Fire1;
    private ImageView Fire2;
    private int Cycles = 0;
    private static final int soundEffectCooldown = 150;
    private static long lastSoundEffect;


    public Projectile(double angle, int timeCharged, Projectile next, double X, double Y) {
        //System.out.println(angle);
        long time = System.currentTimeMillis();
        //System.out.println(time +"..."+ soundEffectCooldown +"..."+lastSoundEffect);
        if(time - soundEffectCooldown > lastSoundEffect) {
            lastSoundEffect = time;
            if ((long) timeCharged <= 500L) {
                Game.playMedia("DefaultFire");
            } else {
                Game.playMedia("ChargeFire");
            }
        }

        this.setTranslateY(Y);
        this.Radius = MinRadius + Math.round((float)(MaxRadiusBonus * timeCharged) / maxChargeTime);
        this.Speed = MinSpeed + Math.round((float)(MaxSpeedBonus * timeCharged) / maxChargeTime);
        this.Damage = MinDamage + Math.round((float)(MaxDamageBonus * timeCharged) / maxChargeTime);
        this.Angle = angle;

        this.setTranslateX(X);
        this.setTranslateY(Y);
        this.Next = next;

        InputStream imginput;
        Throwable var9;
        try {
            imginput = Files.newInputStream(Paths.get("sprites/Fire 1.png"));
            var9 = null;

            try {
                this.Fire1 = new ImageView(new Image(imginput));
                this.Fire1.setRotate((Math.toDegrees(Angle)-90));


                this.Fire1.setScaleY(Game.getScale() + Game.getScale()*(timeCharged / maxChargeTime));
                this.Fire1.setScaleX(Game.getScale() + Game.getScale()*(timeCharged / maxChargeTime));
                this.getChildren().add(this.Fire1);
            } catch (Throwable var37) {
                var9 = var37;
                throw var37;
            } finally {
                if (imginput != null) {
                    if (var9 != null) {
                        try {
                            imginput.close();
                        } catch (Throwable var36) {
                            var9.addSuppressed(var36);
                        }
                    } else {
                        imginput.close();
                    }
                }

            }
        } catch (IOException var41) {
            System.out.println(var41.getMessage());
            System.out.println("Error loading projectile sprite.");
        }

        try {
            imginput = Files.newInputStream(Paths.get("sprites/Fire 2.png"));
            var9 = null;

            try {
                this.Fire2 = new ImageView(new Image(imginput));
                this.Fire2.setRotate(Math.toDegrees(Angle)-90);


                this.Fire2.setScaleY(Game.getScale() + Game.getScale()*(timeCharged / maxChargeTime));
                this.Fire2.setScaleX(Game.getScale() + Game.getScale()*(timeCharged / maxChargeTime));
                this.getChildren().add(this.Fire2);
                this.Fire2.setVisible(false);
            } catch (Throwable var35) {
                var9 = var35;
                throw var35;
            } finally {
                if (imginput != null) {
                    if (var9 != null) {
                        try {
                            imginput.close();
                        } catch (Throwable var34) {
                            var9.addSuppressed(var34);
                        }
                    } else {
                        imginput.close();
                    }
                }

            }
        } catch (IOException var39) {
            System.out.println(var39.getMessage());
            System.out.println("Error loading projectile sprite.");
        }

        GraphicsRoot.getGamePlayPane().getChildren().add(this);
        if (this.Next != null) {
            this.Next.setPrevious(this);
        }

    }

    public void Remove() {
        GraphicsRoot.getGamePlayPane().getChildren().remove(this);
        if (this.Next != null) {
            this.Next.setPrevious(this.Previous);
        }

    }

    public Projectile moveAndCheckForHit(Player Enemy) {
        double absX = this.getTranslateX() + this.getParent().getTranslateX();
        double absY = this.getTranslateY() + this.getParent().getTranslateY();

        this.setTranslateX(this.getTranslateX() + this.Speed * cos(this.Angle));
        this.setTranslateY(this.getTranslateY() + this.Speed * sin(this.Angle));
        if ((this.Cycles + 1) % 4 == 0) {
            this.Fire1.setVisible(!this.Fire1.isVisible());
            this.Fire2.setVisible(!this.Fire2.isVisible());
        }

        ++this.Cycles;
        boolean xhit = false;
        boolean yhit = false;
        if (Enemy != null) {
            if (this.getTranslateX() - Enemy.getTranslateX() <= (double) (50 + this.Radius) && this.getTranslateX() - Enemy.getTranslateX() >= (double) ((50 + this.Radius) * -1)) {
                xhit = true;
            }

            if (this.getTranslateY() - Enemy.getTranslateY() <= (double) (50 + this.Radius) && this.getTranslateY() - Enemy.getTranslateY() >= (double) ((50 + this.Radius) * -1)) {
                yhit = true;
            }
        }

        if (this.Next != null) {
            this.Next = this.Next.moveAndCheckForHit(Enemy);
        }

        if (xhit && yhit) {
            this.setVisible(false);
            Enemy.Damage(this.Damage);
            this.Damage = 0;
            return this;
        } else if (absX <= Game.getScreenWidth() / 2.0D + 300.0D && absX >= Game.getScreenWidth() / -2.0D - 300.0D
        && absY <= Game.getScreenHeight() / 2.0D + 300.0D && absY >= Game.getScreenHeight() / -2.0D - 300.0D) {
            return this;
        } else {
            this.Remove();
            return this.Next;
        }
    }

    public Projectile getNext() {
        return this.Next;
    }

    public Projectile getPrevious() {
        return this.Previous;
    }

    public void setNext(Projectile toSet) {
        this.Next = toSet;
    }

    public void setPrevious(Projectile toSet) {
        this.Previous = toSet;
    }

    public void recursivelyNullify() {
        this.Damage = 0;
        if (this.Next != null) {
            this.Next.recursivelyNullify();
        }

    }

    private double cos(double angle){
        return Math.cos(angle);
    }

    private double sin(double angle){
        return Math.sin(angle);
    }
}
