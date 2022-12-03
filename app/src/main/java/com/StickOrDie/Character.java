package com.StickOrDie;
import android.graphics.Canvas;

public class Character extends BaseObject {


    private int moveX;
    private int moveY;
    private int speed;

    public Character() {

        this.moveX = 0;
        this.moveY = 0;
    }

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

