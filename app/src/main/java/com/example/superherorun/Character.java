package com.example.superherorun;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Character extends BaseObject {


    private int moveX;
    private int moveY;

    public Character() {

        this.moveX = 0;
        this.moveY = 0;
    }

    public void setMoveX(int move) {
        this.moveX = move;
    }

    public void setMoveY(int move) {
        this.moveY = move;
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

//wheres rect