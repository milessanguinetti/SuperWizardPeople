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

public class Player extends GameplayEntity {
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
    private static final long FireCoolDown = 300L;
    private boolean Grounded = false;
    final private double Gravity = .35;
    final private double Friction = .8;
    final private int pixelScale = 10;
    private boolean isRunning = false;
    private boolean runningRight = true;
    private final double baseRunSpeed = 3.5;
    private final double baseJumpSpeed = -4.5;

    public Player() {
        super(35, 35, null, null);
        this.Facing = -1;

        //this.setTranslateY(-100.0D);
        //this.setTranslateX((double)this.Facing * (Game.getScreenWidth() / 2.0D - 100.0D));
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
        Game.getGraphicsRoot().getPlayerPane().getChildren().add(this.HPBar);
        this.StaminaBar = new Bar(Color.DARKBLUE, "MP", this.Facing);
        this.StaminaBar.setTranslateX((double)this.Facing * Game.getScreenWidth() / 3.0D);
        this.StaminaBar.setTranslateY(Game.getScreenHeight() / -2.0D + 80.0D);
        Game.getGraphicsRoot().getPlayerPane().getChildren().add(this.StaminaBar);

    }

    public void PressXKey(int key) {
        if(Grounded)
            this.XMovement = key*baseRunSpeed;
        this.isRunning = true;
        if(key > 0)
            runningRight = true;
        else
            runningRight = false;
    }


    public void ReleaseXKey(int key) {
        if (key > 0 && runningRight) {
            //if(Grounded)
            //    this.XMovement = 0;
            this.isRunning = false;
        }else if (key < 0 && !runningRight){
            //if(Grounded)
            //    this.XMovement = 0;
            this.isRunning = false;
        }

    }

    public void PressYKey(int key) {

    }

    public void ReleaseYKey(int key) {
        if(key > 0){
            if(this.YMovement < 0)
                this.YMovement = 0;
            this.YMovement += 2      ;
        }
        else if(Grounded) {
            this.YMovement = baseJumpSpeed;
        }
        //System.out.println(Grounded + "..." + this.getTranslateY()+ "..." + key + "..." + this.YMovement);
    }

    public void endMovement() {
        this.YMovement = 0;
        this.XMovement = 0;
    }

    /*public void Move() {
        //System.out.println(Game.getScreenHeight()/2.0D - 130.0D+"..."+this.getTranslateY());
        double yColl = gameStage.getCurrentGameStage().getEntityList().recursivelyCheckYCollision(this, 0);
        double xColl = gameStage.getCurrentGameStage().getEntityList().recursivelyCheckXCollision(this, 0);
        if(xColl != 0 && yColl != 0)
            System.out.println(yColl + "/"+ xColl);
        if(GraphicsRoot.getGameplayY() == Game.getScreenHeight() / 2.0D - 40.0D || (yColl < 0 && xColl != 0)) {
            Grounded = true;
            if(isRunning && XMovement < baseRunSpeed && XMovement > baseRunSpeed*-1){
                if(runningRight)
                    XMovement = baseRunSpeed;
                else
                    XMovement = baseRunSpeed*-1;
            }
            if(YMovement > 0)
                YMovement = 0;
        }
        else
            Grounded = false;
        if(this.XMovement != 0) {
            //System.out.println(this.Facing +"/"+ this.XMovement);
            if(Grounded && isRunning) {
                if (this.Facing == 1 && this.XMovement < 0) {
                    this.neutral.setRotate(-90D);
                    this.block.setRotate(-90D);
                    this.fire.setRotate(-90D);
                } else if (this.Facing == -1 && this.XMovement > 0) {
                    this.neutral.setRotate(90D);
                    this.block.setRotate(90D);
                    this.fire.setRotate(90D);
                }
                this.Facing = (int) (this.XMovement / Math.abs(this.XMovement));
            }

            //System.out.println(this.Facing + "..." + this.XMovement);
            if(xColl > 0 && XMovement > 0 && yColl != 0){
                XMovement = 0;
            }
            if(xColl < 0 && XMovement < 0 && yColl != 0){
                XMovement = 0;
            }

            if (GraphicsRoot.getGameplayX() + 25 * this.XMovement < Game.getScreenWidth() / 2.0D - 50.0D && GraphicsRoot.getGameplayX() + (double) (25 * this.XMovement) > -Game.getScreenWidth() / 2.0D + 50.0D) {
                GraphicsRoot.translateGameplayPaneX(GraphicsRoot.getGameplayX() + 25 * this.XMovement);
            }
        }
        //if (this.getTranslateY() + 25 * this.YMovement < Game.getScreenHeight() / 2.0D - 30.0D && this.getTranslateY() + (25 * this.YMovement) > Game.getScreenHeight() / -2.0D + 130.0D) {
        //    applyGravity();
        //    this.setTranslateY(Math.min(this.getTranslateY() + 25 * this.YMovement, Game.getScreenHeight() / -2.0D + 130.0D));
        //}
        if(yColl > 0 && YMovement > 0 && xColl != 0)
            YMovement = 0;

        applyGravity();
        chargeIndication();
        if(YMovement > 0)
            GraphicsRoot.translateGameplayPaneY(Math.min(GraphicsRoot.getGameplayY()+25*this.YMovement, Game.getScreenHeight()/2-40));
        if(YMovement < 0 )
            GraphicsRoot.translateGameplayPaneY(Math.max(GraphicsRoot.getGameplayY()+25*this.YMovement, Game.getScreenHeight()/-2+130));
        //em.out.println(Grounded + "..." + this.getTranslateY() + "..." + YMovement);
    }*/

    public void Move() {
        //System.out.println(Game.getScreenHeight()/2.0D - 130.0D+"..."+this.getTranslateY());
        if (Grounded && isRunning && XMovement < baseRunSpeed && XMovement > baseRunSpeed * -1) {
            if (runningRight)
                XMovement = baseRunSpeed;
            else
                XMovement = baseRunSpeed * -1;
        }
        if (this.XMovement != 0) {
            //System.out.println(this.Facing +"/"+ this.XMovement);
            if (Grounded && isRunning) {
                if (this.Facing == 1 && this.XMovement < 0) {
                    this.neutral.setRotate(-90D);
                    this.block.setRotate(-90D);
                    this.fire.setRotate(-90D);
                } else if (this.Facing == -1 && this.XMovement > 0) {
                    this.neutral.setRotate(90D);
                    this.block.setRotate(90D);
                    this.fire.setRotate(90D);
                }
                this.Facing = (int) (this.XMovement / Math.abs(this.XMovement));
            }
        }


        double [] maxMovement
                = gameStage.getCurrentGameStage().getEntityList().checkCollision(this,
                XMovement*pixelScale, YMovement*pixelScale);
        //System.out.println("*"+XMovement+"/"+YMovement);
        XMovement = maxMovement[0]/pixelScale;
        YMovement = maxMovement[1]/pixelScale;
        //System.out.println("*"+YMovement+"/"+YMovement);

        if(YMovement == 0)
            Grounded = true;
        if (GraphicsRoot.getGameplayY() == Game.getScreenHeight() / 2.0D - 40.0D && YMovement > 0) {
            Grounded = true;
            YMovement = 0;
        }
        if(YMovement != 0)
            Grounded = false;

        if (GraphicsRoot.getGameplayX() + pixelScale * this.XMovement < Game.getScreenWidth() / 2.0D - 50.0D && GraphicsRoot.getGameplayX() + (double) (25 * this.XMovement) > -Game.getScreenWidth() / 2.0D + 50.0D)
            GraphicsRoot.translateGameplayPaneX(GraphicsRoot.getGameplayX() + pixelScale * this.XMovement);

        if (YMovement > 0)
            GraphicsRoot.translateGameplayPaneY(Math.min(GraphicsRoot.getGameplayY() + pixelScale * this.YMovement, Game.getScreenHeight() / 2 - 40));
        if (YMovement < 0)
            GraphicsRoot.translateGameplayPaneY(Math.max(GraphicsRoot.getGameplayY() + pixelScale * this.YMovement, Game.getScreenHeight() / -2 + 130));
                //em.out.println(Grounded + "..." + this.getTranslateY() + "..." + YMovement);
        applyGravity();
        chargeIndication();
        }

    private void applyGravity(){
        if(this.YMovement < 5){
            this.YMovement += Gravity;
        }
        if(Grounded && this.XMovement != 0){
            double speedFloor = 0;
            if(isRunning)
                speedFloor = baseRunSpeed;
            if(this.XMovement < 0){
                this.XMovement = Math.min(this.XMovement + Friction, speedFloor*-1);
                if(runningRight && this.XMovement == speedFloor*-1)
                    this.XMovement = speedFloor;
            }else{
                this.XMovement = Math.max(this.XMovement - Friction, speedFloor);
                if(!runningRight && this.XMovement == speedFloor)
                    this.XMovement = speedFloor*-1;
            }
        }
    }

    private void chargeIndication(){
        if(Charging){
            if(System.currentTimeMillis() - this.startedCharging > Projectile.maxChargeTime)
                this.addTempColor(1, 300);
        }
    }

    public void applyMovementVector(double magnitude, double direction){
        //System.out.println("before:"+this.Grounded+"..."+this.YMovement);
        //System.out.println(Math.cos(direction) +"/"+ Math.sin(direction));
        this.XMovement += magnitude*Math.cos(direction);
        this.YMovement += magnitude*Math.sin(direction);
        //System.out.println("after:"+this.Grounded+"..."+this.YMovement);
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
            if (timeSpentCharging < Projectile.minChargeTime) {
                timeSpentCharging = 0L;
            }

            if (timeSpentCharging > Projectile.maxChargeTime) {
                timeSpentCharging = Projectile.maxChargeTime;
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
        ObservableList toRemoveFrom = Game.getGraphicsRoot().getPlayerPane().getChildren();
        toRemoveFrom.remove(this);
        toRemoveFrom.remove(this.HPBar);
        toRemoveFrom.remove(this.StaminaBar);
    }

    public int getFacing(){
        return this.Facing;
    }
}
