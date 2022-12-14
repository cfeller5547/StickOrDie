package com.StickOrDie;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Platforms {

    private int x;
    private int y;
    private boolean isFullyInView;
    private boolean isPartiallyInView;
    private Bitmap bitmap;
    public int countFullyInViewPlatforms = 0;

    Platforms(Context context, int screenX, int screenY, int xLocation){
        //we gonna need make this universal
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spikedplatform);
        bitmap = Bitmap.createScaledBitmap(getBitmap(), BitmapWidth(), BitmapHeight(), false);
        x = xLocation;
        y = screenY;
        isFullyInView=false;
        isPartiallyInView=false;
    }

    public int RandGenerator(int lowerBound, int upperBound){
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        lowerBound = 1977;
        upperBound = 2300;
        int num = rand.nextInt(upperBound-lowerBound) + lowerBound;
        return num;
    }

    static public int BitmapWidth()
    {
        return Math.round(Constants.SCREEN_WIDTH / 18f);//=80
    }

     static public int BitmapHeight(){
         Random rand = new Random();
         rand.setSeed(System.currentTimeMillis());
         int lowerBound = Math.round(Constants.SCREEN_HEIGHT/9.885f);//9.885f=200; // 3.51648352f=546
         int upperBound = Math.round(Constants.SCREEN_HEIGHT/2.47125f); //=800
         int result = rand.nextInt(upperBound-lowerBound) + lowerBound;
        return result;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getY(){return y; }

    public int Left() { return x;}
    public int Right() { return Left() + bitmap.getWidth(); }

    public void setFullyInView(boolean y) {
        this.isFullyInView = y;
        countFullyInViewPlatforms++;
    }
    public void setPartiallyInView(boolean y) {
        this.isPartiallyInView = y;
    }

    public boolean getInView(){return this.isFullyInView || this.isPartiallyInView; }

    public boolean getFullyInView(){return this.isFullyInView; }

    public boolean getPartiallyInView(){return this.isPartiallyInView; }

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
















