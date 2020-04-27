import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
    private static final int MaxRadiusBonus = 50;
    private static final int MinRadius = 25;
    private static final int MaxSpeedBonus = 150;
    private static final int MinSpeed = 50;
    private static final int MaxDamageBonus = 38;
    private static final int MinDamage = 12;
    private ImageView Fire1;
    private ImageView Fire2;
    private int Cycles = 0;

    public Projectile(double angle, int timeCharged, Projectile next, double X, double Y) {
        //System.out.println(angle);
        if ((long)timeCharged <= 500L) {
            Game.playMedia("DefaultFire");
        } else {
            Game.playMedia("ChargeFire");
        }

        this.setTranslateY(Y);
        this.Radius = 25 + Math.round((float)(50 * timeCharged) / 2500.0F);
        this.Speed = 50 + Math.round((float)(150 * timeCharged) / 2500.0F);
        this.Damage = 12 + Math.round((float)(38 * timeCharged) / 2500.0F);
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


                this.Fire1.setScaleY(Game.getScale() + Game.getScale()*(timeCharged / 3500d));
                this.Fire1.setScaleX(Game.getScale() + Game.getScale()*(timeCharged / 3500d));
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


                this.Fire2.setScaleY(Game.getScale() + Game.getScale()*(timeCharged / 3500d));
                this.Fire2.setScaleX(Game.getScale() + Game.getScale()*(timeCharged / 3500d));
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

        Game.getGraphicsRoot().getChildren().add(this);
        if (this.Next != null) {
            this.Next.setPrevious(this);
        }

    }

    public void Remove() {
        Game.getGraphicsRoot().getChildren().remove(this);
        if (this.Next != null) {
            this.Next.setPrevious(this.Previous);
        }

    }

    public Projectile moveAndCheckForHit(Player Enemy) {
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
        } else if (this.getTranslateX() <= Game.getScreenWidth() / 2.0D + 300.0D && this.getTranslateX() >= Game.getScreenWidth() / -2.0D - 300.0D) {
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
