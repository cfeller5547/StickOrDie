package com.example.superherorun;


import static com.example.superherorun.GameView.screenRatioX;
import static com.example.superherorun.GameView.screenRatioY;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Platforms {

    private int x;
    private int y;
    private boolean isInView;

    private Bitmap bitmap;



    Platforms(Context context, int screenX, int screenY, int xLocation){
        //we gonna need make this universal

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.platform1);
        bitmap = Bitmap.createScaledBitmap(getBitmap(), BitmapWidth(), BitmapHeight(), false);

        x = xLocation;//randLocation.nextInt(maxX) - bitmap.getWidth();
        y = screenY;
        isInView=false;

    }

    public int RandGenerator(){
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int lowerBound = 1977;
        int upperBound = 2300;
        int num = rand.nextInt(upperBound-lowerBound) + lowerBound;
        return num;
    }
    static public int BitmapWidth()
    {
        return 78;
    }

     static public int BitmapHeight(){
         /*Random rand = new Random();
         rand.setSeed(System.currentTimeMillis());
         int lowerBound = 500;
         int upperBound = 800;
         int result = rand.nextInt(upperBound-lowerBound) + lowerBound;*/

        return 400;
    }

    // getters and setters
    public void setY(int y) {
        this.y = y;
    }
    public int getY(){return y; }

    public void setInView(boolean y) {
        this.isInView = y;
    }
    public boolean getInView(){return this.isInView; }

    public void setX(int x) {
        this.x = x;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX()
    {
        return x;
    }

    public Bitmap getBitmap() {

        return bitmap;

    }


    }
















