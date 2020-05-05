import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class gameStage extends StackPane {
    private ImageView backgroundImage;
    private static gameStage currentGameStage;
    private GameplayEntity entityList;

    public gameStage(String filename){
        this.setAlignment(Pos.CENTER);
        InputStream imginput;
        Throwable toThrow;
        try {
            imginput = Files.newInputStream(Paths.get("sprites/" + filename + ".jpg"));
            toThrow = null;

            try {
                this.backgroundImage = new ImageView(new Image(imginput));
                this.backgroundImage.setScaleX(Game.getScale()*10);
                this.backgroundImage.setScaleY(Game.getScale()*10);
                this.getChildren().add(this.backgroundImage);
            } catch (Throwable toThrow2) {
                toThrow = toThrow2;
                throw toThrow2;
            } finally {
                if (imginput != null) {
                    if (toThrow != null) {
                        try {
                            imginput.close();
                        } catch (Throwable exception1) {
                            toThrow.addSuppressed(exception1);
                        }
                    } else {
                        imginput.close();
                    }
                }

            }
        } catch (IOException exception2) {
            System.out.println(exception2.getMessage());
            System.out.println("Error loading background.");
        }
    }

    public static gameStage getCurrentGameStage() {
        return currentGameStage;
    }

    public static void setCurrentGameStage(gameStage currentGameStage) {
        gameStage.currentGameStage = currentGameStage;
    }

    public static void translateCurrentBackgroundX(){

    }

    public static void translateCurrentBackgroundY(){

    }

    public GameplayEntity getEntityList(){
        return entityList;
    }

    public void addEntity(GameplayEntity toAdd){
        toAdd.setNext(currentGameStage.entityList);
        currentGameStage.entityList = toAdd;
        GraphicsRoot.getGamePlayPane().getChildren().add(toAdd);
    }

    public void cleanGameplayPane(){
        if(entityList != null)
            entityList.recursivelyClean();
        GraphicsRoot.getGamePlayPane().getChildren().remove(this);
    }
}
