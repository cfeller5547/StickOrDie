package com.StickOrDie;

import static com.StickOrDie.Platforms.BitmapWidth;
import static com.StickOrDie.Platforms.BitmapHeight;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;


public class PlatformCollection {


    private Paint paint;
    private Platforms[] plats;
    public int maxPlatformCount = 10;
    public int platformCount = maxPlatformCount;
    private int lastPlatformIndex = -1;
    private int lastLastPlatformIndex = -1;
    private Rect rect;
    private int randScreenLocation;
    private int speed = 8; //=7
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Bitmap bitmap;
    private Character character;
    private int prevActivePlatformIndex=-1;
    // Platform that is currently moving into view
    private int currActivePlatformIndex=-1;
    private int iCounter = 0;

    PlatformCollection(Context context, int screenX, int screenY, Character c) {

        this.character = c;
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        plats = new Platforms[maxPlatformCount];
        for (int i = 0; i < maxPlatformCount; i++) {
            plats[i] = new Platforms(context, screenX, screenY, arrangeXSpawn());
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
        int smallestLeftValue = Constants.SCREEN_WIDTH;//=1440
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

    public void freezePlatforms(){
        this.speed=0;
    }

    public int increasePlatformSpeedBy2(){
        return this.speed += 2;
    }

    public void SpawnPlatform(int i){
        plats[i].setBitmap(Bitmap.createScaledBitmap(plats[i].getBitmap(), BitmapWidth(), BitmapHeight(), false));
        plats[i].setY(minY - plats[i].getBitmap().getHeight());
        plats[i].setX(arrangeXSpawn());
        iCounter++;
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



    private int RandGenerator(int lowerBound, int upperBound){
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int num = rand.nextInt(upperBound-lowerBound) + lowerBound;
        return num;
    }

    private boolean isEvenNumber(int x){
        if(x % 2 == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int arrangeXSpawn(){

            int leftSide = RandGenerator(0, ((Constants.SCREEN_WIDTH * 40) / 100) - BitmapWidth()); //between 0 and 576
            int rightSide = RandGenerator(((Constants.SCREEN_WIDTH * 60) / 100), Constants.SCREEN_WIDTH-BitmapWidth()); //864 to 1440
            if (isEvenNumber(iCounter)){
                return leftSide;
            }
            else{
                return rightSide;
            }
    }

   /* public boolean collides(int x){

        boolean collide = false;
        int x1=x;
        // check for platform collision
        for(int i = 0; i < platformCount && !collide; i++){
            Platforms temp = plats[i];
            if (temp != null && temp.getInView()) {
                int x2=x1+temp.getBitmap().getWidth();
                int platformLeftX=temp.getX();
                int platformRightX = platformLeftX + temp.getBitmap().getWidth();
                collide = (x1 > platformLeftX && x1 < platformRightX) || (x2 > platformLeftX && x2 < platformRightX) || (x1 == platformLeftX);
            }
        }
        //here we checking previous and current
        if(collide == false && lastPlatformIndex != -1) { //means there is at last two platforms in view
            Platforms lastCreatedPlatform = plats[lastPlatformIndex];
            int lastPlatformLeftX = lastCreatedPlatform.getX();
            int lastPlatformRightX = lastPlatformLeftX + lastCreatedPlatform.getBitmap().getWidth();
            int x2=x1+lastCreatedPlatform.getBitmap().getWidth(); //width same for all plats; this is current platforms

            if (x2 < lastPlatformLeftX) { //if current platform is to the left of the previous platform
                collide |= distance(x2, lastPlatformLeftX) < character.getWidth();//if previous and current platform left distance is less than character width collide is true

            }
            else if (lastPlatformRightX < x1) {  //if current platform is to the right of the previous platform
                collide |= distance(x1, lastPlatformRightX) < character.getWidth(); //if previous and current platform right distance is less than character width collide is true
            }

        }
        // if we didn't collide with a platform, check collision with character
        if (!collide)
        {
            if (character != null) {
                if (character.getBm() != null) {
                    int x2 = x + character.getBm().getWidth();
                    int characterLeftX = character.getX();
                    int characterRightX = characterLeftX + character.getBm().getWidth();
                    collide = (x1 > characterLeftX && x1 < characterRightX) || (x2 > characterLeftX && x2 < characterRightX);
                }
            }
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

    }*/
}





