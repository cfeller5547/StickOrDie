package com.example.superherorun;

import static com.example.superherorun.Platforms.BitmapHeight;
import static com.example.superherorun.Platforms.BitmapWidth;

import android.content.Context;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;


public class PlatformCollection {

    private Paint paint;
    private Platforms[] plats;
    public int maxPlatformCount = 10;
    public int platformCount = maxPlatformCount;
    private Rect rect;
    private int randScreenLocation;
    private int speed = 4;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Bitmap bitmap;
    private Character character;
    private int prevActivePlatformIndex=-1;
    // Platform that is currently moving into view
    private int currActivePlatformIndex=-1;


    PlatformCollection(Context context, int screenX, int screenY, Character c) {

        this.character = c;
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        plats = new Platforms[maxPlatformCount];
        for (int i = 0; i < maxPlatformCount; i++) {
            plats[i] = new Platforms(context, screenX, screenY, getNextX());
        }
        SpawnPlatform(0);
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    /*public void movePlatformCollection() {

        for(int i=0; i<platformCount; i++){
            move();
        }
    }*/

    public int getNextAvailablePlatformIndex() {
        for (int i = 0; i < platformCount; i++) {
            if (!plats[i].getInView()){
                return i; //first index not in view
            }
        }
        // all platforms were in view
        if (platformCount>=maxPlatformCount)
        {
            return -1;
        }
        return platformCount;
    }

  /*  public void LogPlatformsInView() {
        for (int i = 0; i < platformCount; i++) {
            Log.d("PFC::LogPlatformsInView", " i = " + i);
            Log.d("PFC::LogPlatformsInView", " in View = " + plats[i].getInView());
        }
    }*/


    public void move() {
        // figure out active platform
        for (int i = 0; i < platformCount; i++) {
            plats[i].setFullyInView(false);
            plats[i].setPartiallyInView(false);
            int platFormHeight=plats[i].getBitmap().getHeight();
            int platformY1=plats[i].getY();
            int platformY2=platformY1+platFormHeight;

            if (platformY2 >= 0 && platformY1<0){
                currActivePlatformIndex=i;
            }

            boolean partiallyinView = (platformY1<0&&platformY2>0) || (platformY1<maxY && platformY2>maxY);
            boolean fullyinView = platformY1>=0 && platformY2<maxY;
            if (fullyinView||partiallyinView) {
                plats[i].setFullyInView(fullyinView);
                plats[i].setPartiallyInView(partiallyinView);
            }
            // log data
       /*     Log.d("PFC::move", " i = " + i);
            Log.d("PFC::move", " platformCount = " + platformCount);
            Log.d("PFC::move", " platFormHeight = " + platFormHeight);
            Log.d("PFC::move", " platformY1 = " + platformY1);
            Log.d("PFC::move", " platformY2 = " + platformY2);*/
        }

       /* LogPlatformsInView();
        Log.d("PFC::move", " currActivePlatformIndex = " + currActivePlatformIndex);
        Log.d("PFC::move", " prevActivePlatforIndex = " + prevActivePlatformIndex);*/
        // figure out if the active platform has changedm
        if (currActivePlatformIndex != prevActivePlatformIndex) //it has changed
        {
            if (plats[currActivePlatformIndex].getFullyInView() == true) {
                int nextIndex = getNextAvailablePlatformIndex();
                /*Log.d("PFC::move", " THE active platform has changed - spawn the next platform");
                Log.d("PFC::move", " nextIndex = " + nextIndex);*/
                if (nextIndex != -1) { //not max platforms reached
                    // it changed - need to create the next platform
                    SpawnPlatform(nextIndex);
                }
                prevActivePlatformIndex = currActivePlatformIndex;
            }
        }
        for (int i = 0; i < platformCount; i++) {
            if (plats[i].getY() < maxY) {
                movePlatformDown(i);
            }
         }
    }

    public int getPlatformSpeed(){
            return this.speed;
    }

    public void movePlatformDown(int i){
        plats[i].setY(plats[i].getY() + this.speed); //11 because it counts number of platforms as 10
                                                        //then it adds a speed of 1
        Log.d("PFC::movePlatformDown", "current index = " + i);
        //Log.d("PFC::movePlatformDown", "speed = " + speed);
        Log.d("PFC::movePlatformDown", "plats[i].setY((int) plats[i].getY() + speed) = "+plats[i].getY());
    }

    public void SpawnPlatform(int i){
        plats[i].setY(minY - BitmapHeight());
        plats[i].setX(getNextX());
        plats[i].setBitmap(Bitmap.createScaledBitmap(plats[i].getBitmap(), BitmapWidth(), BitmapHeight(), false));
    }

    public int RandGenerator(){
            int num = 0;
           Random rand = new Random();
           rand.setSeed(System.currentTimeMillis());
           int lowerBound = -2000;
           int upperBound = 0;
           num = rand.nextInt(upperBound - lowerBound) + lowerBound;

        return num;
    }

    public void draw(Canvas canvas){
        for(int i = 0; i < platformCount; i++) {
            bitmap = plats[i].getBitmap();
            canvas.drawBitmap(bitmap, plats[i].getX(), plats[i].getY(), null);
            //Log.d("PFC::draw", "plats[i].getBitmap().getHeight() = "+plats[i].getBitmap().getHeight());
        }
    }

    public Rect getRect(int i){
        rect = new Rect((int) plats[i].getX(), (int) plats[i].getY(),
                    (int) plats[i].getX() + plats[i].getBitmap().getWidth(),
                    (int) plats[i].getY() + plats[i].getBitmap().getHeight());
            return rect;
    }

    public int getRectBottom(int i){
        return this.getRect(i).bottom;
    }

    /*public void LogPlatformInfo() {
        for(int i = 0; i < platformCount; i++) {
            Platforms temp = plats[i];
            Log.d("PFC::LogPlatformInfo", "i="+i+"; temp.getX()="+temp.getX()+ "; temp.getBitmap().getWidth() = "+temp.getBitmap().getWidth());
        }
    }*/


    public boolean platformMovedBelowCharacter(int i) {
        if (plats[i] != null) {
            if (character.getY2() < plats[i].getY()) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public boolean characterTopIsTouchingPlatformBottom(int i){
        if(plats[i] != null){
            if(this.getRectBottom(i) == (character.getRectTop() + 1) || this.getRectBottom(i) == (character.getRectTop() + 2)){
                //setting to +1 and +2 until can figure out why collision is not at exact coordinates
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public boolean collides(int x){

        //Log.d("PFC::collides", "number passed into collides = "+x);
        boolean collide = false;

        // check for platform collision
        for(int i = 0; i < platformCount && !collide; i++){
            Platforms temp = plats[i];
            if (temp != null && temp.getInView()) {
                //Log.d("PFC::collides", " i="+"temp.getX()="+temp.getX()+"temp.getBitmap().getWidth()="+temp.getBitmap().getWidth());
                int x1=x;
                int x2=x+temp.getBitmap().getWidth();
                int platformLeftX=temp.getX();
                //Log.d("PFC::collides", "platform.getX() = "+temp.getX());
                int platformRightX = platformLeftX + temp.getBitmap().getWidth();
                collide = (x1 > platformLeftX && x1 < platformRightX) || (x2 > platformLeftX && x2 < platformRightX) || x1 == platformLeftX;
            }
        }
        // if we didn't collide with a platform, check collision with character
        if (!collide)
        {
            if (character != null) {
                if (character.getBm() != null) {
                    int x1 = x;
                    int x2 = x + character.getBm().getWidth();
                    int characterLeftX = character.getX();
                    //Log.d("PFC::collides", "character.getX() = " + character.getX());
                    int characterRightX = characterLeftX + character.getBm().getWidth();
                    collide = (x1 > characterLeftX && x1 < characterRightX) || (x2 > characterLeftX && x2 < characterRightX);
                }
            }
        }
        if (collide)
        {
            //Log.d("PFC::collides", "  Collision Detected");
        }
        else
        {
            //Log.d("PFC::collides", "  No collision");
        }
        return collide;
    }

    public int getNextX() {

        Random random = new Random();
        do {
            randScreenLocation = random.nextInt(Constants.SCREEN_WIDTH - BitmapWidth());
            //i++;
        } while (this.collides(randScreenLocation));
        //Log.d("PFC", "Found non-collision after " +i + "tries [" +randScreenLocation +"]");
        return randScreenLocation;

    }



}





