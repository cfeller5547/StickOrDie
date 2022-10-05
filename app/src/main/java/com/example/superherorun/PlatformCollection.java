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
    public int platformCount = 6;
    private Rect rect;
    private int randScreenLocation;
    private int nextYSpawn;
    private int speed = 3;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Bitmap bitmap;
    private Character character;
    private GameView gameview;
    private Handler h;
    private boolean readyForNextSpawn;

    static int count = 0;
    static int initialYStored;
    static int initialYStoredStored;

    PlatformCollection(Context context, int screenX, int screenY, Character c) {


        this.character = c;
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;


        plats = new Platforms[platformCount];

//        character.setWidth(200*Constants.SCREEN_WIDTH/1080);
//        character.setHeight(200*Constants.SCREEN_HEIGHT/1920);
//        character.setX(400*Constants.SCREEN_WIDTH/1080);
//        character.setY(Constants.SCREEN_HEIGHT/2-character.getHeight()/2);
//        ArrayList<Bitmap> arrBms = new ArrayList<>();
//        arrBms.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.character));
//        character.setArrBms(arrBms);


        for (int i = 0; i < platformCount; i++) {
            plats[i] = new Platforms(context, screenX, screenY, getNextX());

        }


        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    public void move() {

        //decreasing y coordinate so that enemy will move bottom to top
        //LogPlatformInfo()
    for (int i = 0; i < platformCount; i++) { //setting i to 1 as test

        plats[i].setY((int) plats[i].getY() + speed);
        //Log.d("PFC::move", "Called here: plats[i].setY((int) plats[i].getY() + Speed); = " + plats[i].getY());
        //Log.d("PFC::move", " after setting, plats.getY() = " + plats[i].getY());
        //Log.d("PFC::move", " current index i = " + i);
        //Log.d("PFC::move", " maxY = " + maxY);

        //Log.d("PFC::move", " Platforms.BitmapWidth() = " + Platforms.BitmapWidth());

        //if the enemy reaches the bottom of screen
        if (plats[i].getY() > maxY - BitmapWidth()) {
            //adding the enemy again to the top of screen
            //Log.d("PFC::move", " ENEMY REACHED TOP OF SCREEN HERE");


            plats[i].setX(getNextX());


            plats[i].setY(getNextY()); //fix here we no longer want it to be random

            Log.d("PFC::move", " plats[i].setY(getNextY) = " + plats[i].getY());



            plats[i].setBitmap(Bitmap.createScaledBitmap(plats[i].getBitmap(), BitmapWidth(), BitmapHeight(), false));

        }
    }
}


    public void movePlatformCollection() {

        //updating the enemy coordinate with respect to player speed
        for(int i=0; i<platformCount; i++){
            move();
        }
    }

    public int difference(int num1, int num2){
        return Math.abs(num1 - num2);
    }

    public int getNextY() {
        final int initialY = RandGenerator();
        int difference1;
        int difference2;
        int temp = 0;

        count++;

        if (count == 1) {
            initialYStored = initialY;
            return initialY;
        }
        else if(count == 2) {
            do {
                nextYSpawn = RandGenerator();
                difference1 = difference(initialYStored,nextYSpawn);
            }while(!fallsBetweenConditions(difference1));
            initialYStoredStored = initialYStored; //storing initial into second before initial gets changed
            initialYStored = nextYSpawn;
            return nextYSpawn;
        }
        else{
            do{
                nextYSpawn = RandGenerator();
                difference1  = difference(initialYStored, nextYSpawn);
                difference2 = difference(initialYStoredStored, nextYSpawn);
            }while(!(fallsBetweenConditions(difference1) && fallsBetweenConditions(difference2))); //why is it freezing here,
            initialYStoredStored = initialYStored;
            initialYStored = nextYSpawn;
            return nextYSpawn;
        }
    }

    public boolean reachedTopOfScreen(int i){
            if (plats[i].getY() > Constants.SCREEN_HEIGHT) {
                return true;
            } else {
                return false;
            }
        }


    public boolean fallsBetweenConditions(int num){
        if(num >= 300 && num <= 600){
            return true;
        }
        else{
            return false;
        }
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


    public int getGap(int theRandomNum){
       int spawnGap = 0;

        for(int i = 1; i < platformCount; i++) {// i counts to 3 then stays at 3

                if (plats[i] != null) {
                    spawnGap = (plats[i].getY() - plats[i -1].getY());
                    Log.d("PFC::getSpawnGap", " current index i = " + i);

                    Log.d("PFC::getSpawnGap", " plats[i].getY = " + plats[i].getY());
                    Log.d("PFC::getSpawnGap", " plats[i-1].getY = " + plats[i-1].getY());
                }
                Log.d("PFC::getSpawnGap", "spawnGap = " + Math.abs(spawnGap));
            }

        return Math.abs(spawnGap);
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

    /*public void LogPlatformInfo() {
        for(int i = 0; i < platformCount; i++) {
            Platforms temp = plats[i];
            Log.d("PFC::LogPlatformInfo", "i="+i+"; temp.getX()="+temp.getX()+ "; temp.getBitmap().getWidth() = "+temp.getBitmap().getWidth());
        }
    }*/



    public boolean collides(int x){

        //Log.d("PFC::collides", "number passed into collides = "+x);
        boolean collide = false;

        // check for platform collision
        for(int i = 0; i < platformCount && !collide; i++){
            Platforms temp = plats[i];
            if (temp != null) {
                //Log.d("PFC::collides", " i="+"temp.getX()="+temp.getX()+"temp.getBitmap().getWidth()="+temp.getBitmap().getWidth());
                int x1=x;
                int x2=x+temp.getBitmap().getWidth();
                int platformLeftX=temp.getX();
                //Log.d("PFC::collides", "platform.getX() = "+temp.getX());
                int platformRightX = platformLeftX + temp.getBitmap().getWidth();
                collide = (x1 > platformLeftX && x1 < platformRightX) || (x2 > platformLeftX && x2 < platformRightX);
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
        //int i=0;
        do {
            randScreenLocation = random.nextInt(Constants.SCREEN_WIDTH - BitmapWidth());
            //i++;
        } while (this.collides(randScreenLocation));
        //Log.d("PFC", "Found non-collision after " +i + "tries [" +randScreenLocation +"]");
        return randScreenLocation;

    }



}





   /* public void move(int charSpeed) {
        //decreasing y coordinate so that enemy will move bottom to top
        for(int i=0; i < platformCount; i++) {
            y -= charSpeed;
            y -= speed;
            //if the enemy reaches the top of screen
            if (y < minY - bitmap.getHeight()) {
                //adding the enemy again to the bottom of screen
                Random generator = new Random();
                speed = 5;
                x = generator.nextInt(maxX) - bitmap.getWidth();
                ;
                y = maxY;
            }
        }
    }
*/




