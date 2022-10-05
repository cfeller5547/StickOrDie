package com.example.superherorun;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Character extends BaseObject {


    private int move;

    public Character() {

        this.move = 0;
    }

    public float getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public void draw(Canvas canvas) {
        move();
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
    }

    private void move(){
        this.x += this.move;
    }

}

//wheres rect