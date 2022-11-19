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
import java.util.Random;


public class PlatformCollection {

    private Paint paint;
    private Platforms[] plats;
    public int maxPlatformCount = 10;
    public int platformCount = maxPlatformCount;
    private int lastPlatformIndex = -1;
    private Rect rect;
    private int randScreenLocation;
    private int speed = Math.round(Constants.SCREEN_WIDTH / 154.2857f); //=7
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

        if (currActivePlatformIndex != prevActivePlatformIndex) //it has changed
        {
            if (plats[currActivePlatformIndex].getFullyInView() == true) {
                int nextIndex = getNextAvailablePlatformIndex();

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

    public int getFurthestLeftPlatformRightXInView(){
        int smallestLeftValue = Constants.SCREEN_WIDTH;//=1080
        for(int i=0; i < platformCount; i++) {
            if (plats[i].getFullyInView()) {
                int platsRight = plats[i].getX() + plats[i].getBitmap().getWidth();
                if (platsRight < smallestLeftValue) {
                    smallestLeftValue = platsRight;
                }
            }
        }
        return smallestLeftValue;
    }

    public int getFurthestRightPlatformLeftXInView(){
        int LargestRightValue = 0;
        for(int i=0; i < platformCount; i++) {
            if (plats[i].getFullyInView()) {
                int platsLeft = plats[i].getX();
                if (platsLeft > LargestRightValue) {
                    LargestRightValue = platsLeft;
                }
            }
        }
        return LargestRightValue;
    }

    public int getPlatformSpeed(){
            return this.speed;
    }

    public void movePlatformDown(int i){
        plats[i].setY(plats[i].getY() + this.speed);
    }

    public int increasePlatformSpeedBy2(){
        return this.speed += 2;
    }

    public void SpawnPlatform(int i){
        plats[i].setBitmap(Bitmap.createScaledBitmap(plats[i].getBitmap(), BitmapWidth(), BitmapHeight(), false));
        plats[i].setY(minY - plats[i].getBitmap().getHeight());
        plats[i].setX(getNextX());

        lastPlatformIndex = i;
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

    public int distance(int var1, int var2){
        return Math.abs(var2 - var1);
    }

    public boolean collides(int x){
        
        boolean collide = false;
        int x1=x;
        Log.d("PFC::collides1", "x1 /left = " + x1);

        // check for platform collision
        for(int i = 0; i < platformCount && !collide; i++){
            Platforms temp = plats[i];
            if (temp != null && temp.getInView()) {
                Log.d("PFC::collides1", "current index i = " + i);
                int x2=x1+temp.getBitmap().getWidth();
                Log.d("PFC::collides1", "x2 /right = " + x2);
                int platformLeftX=temp.getX();
                Log.d("PFC::collides1", "platformLeftX  = " + platformLeftX);
                int platformRightX = platformLeftX + temp.getBitmap().getWidth();
                Log.d("PFC::collides1", "platformRightX = " + platformRightX);
                collide = (x1 > platformLeftX && x1 < platformRightX) || (x2 > platformLeftX && x2 < platformRightX) || (x1 == platformLeftX);
                //(distance(x2, platformLeftX) < character.getWidth() || distance(x1, platformRightX) < character.getWidth()); //collide true
            }
        }

        if(collide == false && lastPlatformIndex != -1) {
            Log.d("PFC::collides2", "New Platform did not collide with a previous platform");
            Platforms lastCreatedPlatform = plats[lastPlatformIndex];
            int platformLeftX = lastCreatedPlatform.getX();
            int platformRightX = platformLeftX + lastCreatedPlatform.getBitmap().getWidth();
            int x2=x1+lastCreatedPlatform.getBitmap().getWidth();

            if (x2 < platformLeftX) {
                collide |= distance(x2, platformLeftX) < character.getWidth(); //if new platform spawns left of old one, collide = true
                if(collide == true){
                    Log.d("PFC::collides2", "collide true case 1 : platformRightX= " + platformRightX);
                    Log.d("PFC::collides2", "collide true case 1 :PlatformLeftX= " + platformLeftX);
                    Log.d("PFC::collides2", "collide true case 1 : x1= " + x1);
                    Log.d("PFC::collides2", "collide true case 1 :x2= " + x2);

                }
            } else if (platformRightX < x1) { //platform spawned right of character, collide = true
                collide |= distance(x1, platformRightX) < character.getWidth(); //defines width  in between previous and next platform
                Log.d("PFC::collides2", "collide true case 2 : platformLeftX= " + platformLeftX);
                Log.d("PFC::collides2", "collide true case 2 : platformRightX= " + platformRightX);
                Log.d("PFC::collides2", "collide true case 2 : x1= " + x1);
                Log.d("PFC::collides2", "collide true case 2 :x2= " + x2);
            } else {
                Log.d("PFC::collides2", "bullshit = " + platformRightX);

            }
        }

        // if we didn't collide with a platform, check collision with character
        if (!collide)
        {
            if (character != null) {
                if (character.getBm() != null) {
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





