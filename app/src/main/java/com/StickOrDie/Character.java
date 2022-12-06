package com.StickOrDie;
import android.graphics.Canvas;
import android.transition.ArcMotion;

public class Character extends BaseObject {


    private int moveX;
    private int moveY;
    private int speed;


    public Character() {
        this.moveX = 0;
        this.moveY = 0;
    }


    private void moveCharacterPiecesVertically(Character piece1, Character piece2, Character piece3, Character piece4, int[] arrBounds){

    }

    /*private boolean insideDeathBoundInterval(int[] deathBoundsArr, int x){
        if(x > deathBoundsArr[0] && x < deathBoundsArr[1]){
            return true;
        }
        else{
            return false;
        }
    }*/

    public boolean movingRight(){
        int directionMoving = this.getMoveX();
        if(directionMoving > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean movingLeft(){
        int directionMoving = this.getMoveX();
        if(directionMoving < 0){
            return true;
        }
        else{
            return false;
        }
    }

    public void setMoveX(int move) {
        this.moveX = move;
    }
    public int getMoveX(){
        return moveX;
    }

    public void setMoveY(int move) {
        this.moveY = move;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }
    public int getSpeed(){
        return speed;
    }

    public void draw(Canvas canvas) {
        moveX();
        moveY();
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
    }

    private void moveX(){
        this.x += this.moveX;
    }
    private void moveY(){
        this.y += this.moveY;
    }

}

