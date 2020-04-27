import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Player extends StackPane {
    private int Health = 100;
    private int Stamina = 100;
    private double YMovement = 0;
    private double XMovement = 0;
    private int Facing;
    private int turtlingExponentiation = 0;
    private Bar HPBar;
    private Bar StaminaBar;
    private boolean Charging = false;
    private boolean Invincible = false;
    private long startedCharging;
    private ColorAdjust Adjust = new ColorAdjust();
    private ImageView block;
    private ImageView fire;
    private ImageView neutral;
    private static final int Speed = 25;
    private static final long FireCoolDown = 300L;
    private static final long MaxChargeTime = 2500L;
    public static final int Radius = 50;
    public static final long MinChargeTime = 500L;
    private boolean Grounded = false;
    final private double Gravity = .6;

    public Player() {
        this.Facing = -1;

        this.setTranslateY(-100.0D);
        this.setTranslateX((double)this.Facing * (Game.getScreenWidth() / 2.0D - 100.0D));
        this.setAlignment(Pos.CENTER);
        String spriteColor = new String();
        int whichCharacter = 1;
        switch(whichCharacter) {
            case 1:
                spriteColor = "Green";
                break;
            case 2:
                spriteColor = "Blue";
                break;
            case 3:
                spriteColor = "Pink";
                break;
            case 4:
                spriteColor = "Yellow";
        }

        InputStream imginput;
        Throwable var4;
        try {
            imginput = Files.newInputStream(Paths.get("sprites/" + spriteColor + " Turtle Block.png"));
            var4 = null;

            try {
                this.block = new ImageView(new Image(imginput));
                this.block.setEffect(this.Adjust);
                this.block.setVisible(false);
                this.block.setScaleX(Game.getScale());
                this.block.setScaleY(Game.getScale());
                this.block.setRotate((double)(this.Facing * -90));
                this.getChildren().add(this.block);
            } catch (Throwable var59) {
                var4 = var59;
                throw var59;
            } finally {
                if (imginput != null) {
                    if (var4 != null) {
                        try {
                            imginput.close();
                        } catch (Throwable var58) {
                            var4.addSuppressed(var58);
                        }
                    } else {
                        imginput.close();
                    }
                }

            }
        } catch (IOException var65) {
            System.out.println(var65.getMessage());
            System.out.println("Error loading player sprite.");
        }

        try {
            imginput = Files.newInputStream(Paths.get("sprites/" + spriteColor + " Turtle Neutral.png"));
            var4 = null;

            try {
                this.neutral = new ImageView(new Image(imginput));
                this.neutral.setEffect(this.Adjust);
                this.neutral.setVisible(true);
                this.neutral.setScaleX(Game.getScale());
                this.neutral.setScaleY(Game.getScale());
                this.neutral.setRotate((double)(this.Facing * -90));
                this.getChildren().add(this.neutral);
            } catch (Throwable var57) {
                var4 = var57;
                throw var57;
            } finally {
                if (imginput != null) {
                    if (var4 != null) {
                        try {
                            imginput.close();
                        } catch (Throwable var56) {
                            var4.addSuppressed(var56);
                        }
                    } else {
                        imginput.close();
                    }
                }

            }
        } catch (IOException var63) {
            System.out.println(var63.getMessage());
            System.out.println("Error loading player sprite.");
        }

        try {
            imginput = Files.newInputStream(Paths.get("sprites/" + spriteColor + " Turtle Fire.png"));
            var4 = null;

            try {
                this.fire = new ImageView(new Image(imginput));
                this.fire.setEffect(this.Adjust);
                this.fire.setVisible(false);
                this.fire.setScaleX(Game.getScale());
                this.fire.setScaleY(Game.getScale());
                this.fire.setRotate((double)(this.Facing * -90));
                this.getChildren().add(this.fire);
            } catch (Throwable var55) {
                var4 = var55;
                throw var55;
            } finally {
                if (imginput != null) {
                    if (var4 != null) {
                        try {
                            imginput.close();
                        } catch (Throwable var54) {
                            var4.addSuppressed(var54);
                        }
                    } else {
                        imginput.close();
                    }
                }

            }
        } catch (IOException var61) {
            System.out.println(var61.getMessage());
            System.out.println("Error loading player sprite.");
        }

    }

    public void InitializeBars() {
        this.HPBar = new Bar(Color.RED, "HP", this.Facing);
        this.HPBar.setTranslateX((double)this.Facing * Game.getScreenWidth() / 3.0D);
        this.HPBar.setTranslateY(Game.getScreenHeight() / -2.0D + 30.0D);
        Game.getGraphicsRoot().getRootChildren().add(this.HPBar);
        this.StaminaBar = new Bar(Color.DARKBLUE, "MP", this.Facing);
        this.StaminaBar.setTranslateX((double)this.Facing * Game.getScreenWidth() / 3.0D);
        this.StaminaBar.setTranslateY(Game.getScreenHeight() / -2.0D + 80.0D);
        Game.getGraphicsRoot().getRootChildren().add(this.StaminaBar);
    }

    public void PressXKey(int key) {
        this.XMovement = key;
    }

    public void ReleaseXKey(int key) {
        if (key == this.XMovement) {
            this.XMovement = 0;
        }

    }

    public void PressYKey(int key) {
        if(key > 0){
            if(this.YMovement < 0)
                this.YMovement = 0;
            this.YMovement += .5;
        }
        else if(Grounded)
            this.YMovement = -3.5;
        //System.out.println(Grounded + "..." + this.getTranslateY()+ "..." + key + "..." + this.YMovement);
    }

    public void ReleaseYKey(int key) {
        //if (key == this.YMovement) {
        //    this.YMovement = 0;
        //}
    }

    public void endMovement() {
        this.YMovement = 0;
        this.XMovement = 0;
    }

    public void Move() {
        if(this.XMovement != 0) {
            if (this.Facing == 1 && this.XMovement < 0) {
                this.neutral.setRotate(-90D);
                this.block.setRotate(-90D);
                this.fire.setRotate(-90D);
            } else if (this.Facing == -1 && this.XMovement > 0) {
                this.neutral.setRotate(90D);
                this.block.setRotate(90D);
                this.fire.setRotate(90D);
            }
            this.Facing = (int) this.XMovement / (int)Math.abs(this.XMovement);

            //System.out.println(this.Facing + "..." + this.XMovement);

            if (this.getTranslateX() + 25 * this.XMovement < Game.getScreenWidth() / 2.0D - 50.0D && this.getTranslateX() + (double) (25 * this.XMovement) > -Game.getScreenWidth() / 2.0D + 50.0D) {
                this.setTranslateX(this.getTranslateX() + (double) (25 * this.XMovement));
            }
        }
        //System.out.println(Game.getScreenHeight()/2.0D - 130.0D+"..."+this.getTranslateY());
        if(this.getTranslateY() == Game.getScreenHeight() / 2.0D - 40.0D)
            Grounded = true;
        else
            Grounded = false;

        //if (this.getTranslateY() + 25 * this.YMovement < Game.getScreenHeight() / 2.0D - 30.0D && this.getTranslateY() + (25 * this.YMovement) > Game.getScreenHeight() / -2.0D + 130.0D) {
        //    applyGravity();
        //    this.setTranslateY(Math.min(this.getTranslateY() + 25 * this.YMovement, Game.getScreenHeight() / -2.0D + 130.0D));
        //}
        applyGravity();
        if(YMovement > 0)
            this.setTranslateY(Math.min(this.getTranslateY()+25*this.YMovement, Game.getScreenHeight()/2-40));
        if(YMovement < 0 )
            this.setTranslateY(Math.max(this.getTranslateY()+25*this.YMovement, Game.getScreenHeight()/-2+130));
        //em.out.println(Grounded + "..." + this.getTranslateY() + "..." + YMovement);
    }

    private void applyGravity(){
        if(!Grounded && this.YMovement < 5){
            this.YMovement = this.YMovement + Gravity;
        }
    }

    public void Damage(int damage) {
        if (!this.Invincible) {
            this.Health -= damage;
            if (damage > 0) {
                Game.playMedia("DamageTaken");
                this.HPBar.setTempColor(Color.DARKRED);
                this.addTempColor(-0.5D, 200);
            }

            if (this.Health <= 0) {
                this.Die();
            }

            this.HPBar.setPercentFull((float)this.Health / 100.0F);
        } else {
            Game.playMedia("Blocked");
        }

    }

    public void setInvulnerable(boolean toset) {
        if (!this.Charging) {
            if (!this.Invincible && toset) {
                Game.playMedia("ShellUp");
            }

            this.Invincible = toset;
            if (toset) {
                this.fire.setVisible(false);
                this.neutral.setVisible(false);
                this.block.setVisible(true);
            } else {
                this.fire.setVisible(false);
                this.neutral.setVisible(true);
                this.block.setVisible(false);
                this.turtlingExponentiation = 0;
            }
        }

    }

    public boolean isInvincible() {
        return this.Invincible;
    }

    public boolean DecrementStamina(int loss, boolean isDueToTurtling) {
        if (isDueToTurtling) {
            loss += loss * this.turtlingExponentiation;
            ++this.turtlingExponentiation;
        }

        if (this.Stamina - loss < 0) {
            this.StaminaBar.setTempColor(Color.ORANGE);
            Game.playMedia("StaminaDepleted");
            return false;
        } else if (this.Stamina - loss > 100) {
            this.Stamina = 100;
            this.StaminaBar.setPercentFull(1.0F);
            return false;
        } else {
            this.Stamina -= loss;
            this.StaminaBar.setPercentFull((float)this.Stamina / 100.0F);
            return true;
        }
    }

    public void Die() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyValue kv1 = new KeyValue(this.translateXProperty(), Game.getScreenWidth() / 2.0D * (double)this.Facing, Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(this.translateYProperty(), Game.getScreenHeight(), Interpolator.EASE_IN);
        KeyFrame kf1 = new KeyFrame(Duration.millis(2000.0D), new KeyValue[]{kv1, kv2});
        KeyValue kv3 = new KeyValue(this.translateYProperty(), -200, Interpolator.EASE_OUT);
        KeyValue kv4 = new KeyValue(this.rotateProperty(), this.Facing * 20, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.millis(800.0D), new KeyValue[]{kv3, kv4});
        timeline.getKeyFrames().add(kf1);
        timeline.getKeyFrames().add(kf2);
        timeline.play();
    }

    public void setCharge() {
        if (!this.Charging && !this.Invincible) {
            long time = System.currentTimeMillis();
            if (time - this.startedCharging > 300L) {
                this.fire.setVisible(true);
                this.block.setVisible(false);
                this.neutral.setVisible(false);
                this.Charging = true;
                this.startedCharging = time;
            }
        }

    }

    public long releaseCharge() {
        if (this.Charging && !this.Invincible) {
            long timeSpentCharging = System.currentTimeMillis() - this.startedCharging;
            if (timeSpentCharging < 500L) {
                timeSpentCharging = 0L;
            }

            if (timeSpentCharging > 2500L) {
                timeSpentCharging = 2500L;
            }

            this.Charging = false;
            this.fire.setVisible(false);
            this.block.setVisible(false);
            this.neutral.setVisible(true);
            return timeSpentCharging;
        } else {
            return -1L;
        }
    }

    public void addTempColor(double toSet, int duration) {
        Timeline colorTimeline = new Timeline();
        this.Adjust.setHue(toSet);
        this.Adjust.setBrightness(0.3D);
        KeyFrame kf = new KeyFrame(Duration.millis((double)duration), (ae) -> {
            this.Adjust.setHue(0.0D);
            if (this.isDead()) {
                this.Adjust.setBrightness(-0.5D);
            } else {
                this.Adjust.setBrightness(0.0D);
            }

        }, new KeyValue[0]);
        colorTimeline.getKeyFrames().add(kf);
        colorTimeline.setCycleCount(1);
        colorTimeline.play();
    }

    public void addTempColor(double toSet) {
        new Timeline();
        if (this.Adjust.getHue() == 0.0D) {
            this.Adjust.setHue(toSet);
        } else {
            this.Adjust.setHue(toSet);
        }

    }

    public boolean isDead() {
        return this.Health <= 0;
    }

    public void RemoveFromPane() {
        ObservableList toRemoveFrom = Game.getGraphicsRoot().getRootChildren();
        toRemoveFrom.remove(this);
        toRemoveFrom.remove(this.HPBar);
        toRemoveFrom.remove(this.StaminaBar);
    }

    public int getFacing(){
        return this.Facing;
    }
}
