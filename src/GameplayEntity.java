import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class GameplayEntity extends StackPane {
    private double halfWidth;
    private double halfHeight;
    private GameplayEntity Previous = null;
    private GameplayEntity Next = null;
    private Node child;

    public GameplayEntity(double width, double height, GameplayEntity next, GameplayEntity prev){
        halfWidth = width/2;
        halfHeight = height/2;
        this.setAlignment(Pos.CENTER);
        if(next != null){
            Next = next;
            Next.Previous = this;
        }
        if(prev != null){
            Previous = prev;
            Previous.Next = this;
        }
    }

    public void addNode(Node toAdd){
        this.getChildren().add(toAdd);
        this.child = toAdd;
    }

    public double checkXCollision(GameplayEntity toCheck, double movement) {
        double retval = this.getAbsX() - toCheck.getAbsX();
        if (movement == 0){
            if(retval < 0){
                if (retval + (this.halfWidth + toCheck.halfWidth) <= 0)
                    return 0; //no existing overlap
            }else if(retval > 0){
                if(retval - (this.halfWidth + toCheck.halfWidth) >= 0)
                    return 0; //no existing overlap
            }
            return -1; //movement is valid, but objects overlap
        }else if(movement > 0) {
            if(retval < 0) { //if we're moving in the opposite direction of the coord difference
                if(retval + (this.halfWidth + toCheck.halfWidth) < 0) //if the entity clears the other entity
                    return movement; //movement is valid regardless
                return -1; //essentially an error stating that while the movement is valid,
                           //the entities still have overlapping bounds on the x axis
            }
            retval -= (this.halfWidth + toCheck.halfWidth); //factor in entity breadth
            if(retval < 0) //no movement possible if dimensions overlap
                return 0;
            return Math.min(retval, movement);
        }else{ //negative movement
            if(retval > 0) {
                if(retval - (this.halfWidth + toCheck.halfWidth) > 0)
                    return movement;
                return 1;
            }
            retval += (this.halfWidth + toCheck.halfWidth);
            if(retval > 0)
                return 0;
            return Math.max(retval, movement);
        }
    }

    public double checkYCollision(GameplayEntity toCheck, double movement){
        double retval = this.getAbsY() - toCheck.getAbsY();
        if (movement == 0){ //if there's no movement
            if(retval < 0){
                if (retval + this.halfHeight + toCheck.halfHeight <= 0)
                    return 0; //no existing overlap
            }else if(retval > 0){
                if(retval - (this.halfHeight + toCheck.halfHeight) >= 0)
                    return 0; //no existing overlap
            }
            return -1; //movement is valid, but objects overlap
        }else if(movement > 0) {
            if(retval < 0) { //if we're moving in the opposite direction of the coord difference
                if(retval + (this.halfHeight + toCheck.halfHeight) < 0) //if the entity clears the other entity
                    return movement; //movement is valid regardless
                return -1; //essentially an error stating that while the movement is valid,
                //the entities still have overlapping bounds on the x axis
            }
            retval -= (this.halfHeight + toCheck.halfHeight); //factor in entity breadth
            if(retval < 0) //no movement possible if dimensions overlap
                return 0;

            return Math.min(retval, movement);
        }else{
            if(retval > 0) {
                if(retval - (this.halfHeight + toCheck.halfHeight) > 0)
                    return movement;
                return 1;
            }
            retval += (this.halfHeight + toCheck.halfHeight);
            if(retval > 0)
                return 0;
            return Math.max(retval, movement);
        }
    }

    public boolean checkOnTop(GameplayEntity toCheck) {
        double dif = this.getAbsY() - toCheck.getAbsY();
        if (dif < 0) {
            if (dif + this.halfHeight + toCheck.halfHeight == 0)
                return true;
        } else if (dif > 0) {
            if (dif - (this.halfHeight + toCheck.halfHeight) == 0)
                return true; //no existing overlap
        }
        return false;
    }

    public double [] checkCollision(GameplayEntity toCheck, double xmove, double ymove){
        double [] retval = new double[2]; //return value is a pair of maximum possible movement values
        retval[0] = xmove;
        retval[1] = ymove;
        double xpossible, ypossible; //vars for possible movement per each entity in list
        GameplayEntity current = this;
        while(current != null){
            xpossible = current.checkXCollision(toCheck, retval[0]);
            ypossible = current.checkYCollision(toCheck, retval[1]);
            if(xpossible != retval[0] && ypossible != retval[1]){
                if(retval[0] != 0 &&!current.checkOnTop(toCheck)) { //0 movement will stay 0 movement
                    if (retval[0] < 0) { //negative movement cases
                        if (!(xpossible > 0)) //positive values for negative movement imply valid overlap; skip
                            retval[0] = Math.max(retval[0], xpossible); //otherwise, find the value closest to 0
                    } else { //positive movement cases
                        if (!(xpossible < 0)) //negative values for positive movement imply valid overlap; skip
                            retval[0] = Math.min(retval[0], xpossible); //otherwise, find the value closest to 0
                    }
                }
                if(retval[1] != 0) { //0 movement will stay 0 movement
                    if (retval[1] < 0) { //negative movement cases
                        if (!(ypossible > 0)) //positive values for negative movement imply valid overlap; skip
                            retval[1] = Math.max(retval[1], ypossible); //otherwise, find the value closest to 0
                    } else { //positive movement cases
                        if (!(ypossible < 0)) //negative values for positive movement imply valid overlap; skip
                            retval[1] = Math.min(retval[1], ypossible); //otherwise, find the value closest to 0
                    }
                }
            }
            current = current.Next;
        }
        return retval;
    }

    public void recursivelyClean(){
        if(Next != null)
            Next.recursivelyClean();
        GraphicsRoot.getGamePlayPane().getChildren().remove(this);
    }

    public void setNext(GameplayEntity toSet){
        Next = toSet;
        if(Next != null)
            Next.Previous = this;
    }

    private double getAbsX(){
        //System.out.println(this.getParent().getTranslateX() + this.getTranslateX());
        //if(child != null)
            //System.out.println(child.getTranslateX() +"/"+child.getParent().getTranslateX());
        if(this.getParent() != null)
            return this.getParent().getTranslateX() + this.getTranslateX();
        return this.getTranslateX();
    }

    private double getAbsY(){
        if(this.getParent() != null)
            return this.getParent().getTranslateY() + this.getTranslateY();
        return this.getTranslateY();
    }
}
