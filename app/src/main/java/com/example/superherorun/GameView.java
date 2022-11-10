package com.example.superherorun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class GameView extends View {
    private Character character;
    private Platforms platform;
    private PlatformCollection platformCollection;

    static MediaPlayer gameMusic;
    private boolean isPlaying = true;
    private boolean isGameOver = false;
    private boolean playerClicked = false;
    private Canvas canvasRetriever;
    private boolean playerTopTouchedPlatformBottom = false;
    private Random random;
    private Paint paint;
    private Bitmap bm;
    private Bitmap dyingBm;
    private Handler handler;
    private Runnable r;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private int platformCollisionIndex;
    private boolean playerJumpedAndCollided = false;
    private boolean playerStartedMovingLeft = false;
    private boolean playerStartedMovingRight = false;
    private boolean snapBackOccured = false;
    private int characterRectRightAfterSnapback = 0;
    private int characterRectLeftAfterSnapback = 0;
    private int characterRectTopAfterSnapback = 0;
    static int distanceCharRightToPlatformLeft;
    static int distanceCharLeftToPlatformRight;
    static int distanceCharTopToPlatformBottom;
    static int score;

    public GameView(Context context, int screenX, int screenY) {

        super(context);
        gameMusic = MediaPlayer.create(context,R.raw.growingonme);
        gameMusic.start();

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.RED);
        character = new Character();

        random = new Random();
        character.setWidth(200*Constants.SCREEN_WIDTH/1080);
        character.setHeight(200*Constants.SCREEN_HEIGHT/1920);
        character.setX((400*Constants.SCREEN_WIDTH/1080) + 40 ); //adding 60 until universality solved
        character.setY((Constants.SCREEN_HEIGHT/2-character.getHeight()/2) + 300);
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        bm = Bitmap.createScaledBitmap(bm, character.getWidth(), character.getHeight(), false);
        dyingBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.characterdying);
        dyingBm = Bitmap.createScaledBitmap(dyingBm, character.getWidth(), character.getHeight(), false);
        character.setBm(bm);
        platformCollection = new PlatformCollection(context, screenX, screenY, character);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;


        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                //while(isPlaying) {
                invalidate();
                update();
                }
        };
    }



    public void draw(Canvas canvas){
        super.draw(canvas);
        character.draw(canvas);
        platformCollection.draw(canvas);
        canvas.drawText(score + "", Constants.SCREEN_WIDTH / 2f, 164, paint);

        handler.postDelayed(r, 1);

        if(isGameOver){
            Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
            gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            gameOverIntent.putExtra("SCORE", score);
            getContext().startActivity(gameOverIntent);
            gameMusic.stop();


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(playerClicked == false) {
                    if (event.getX() < screenX / 2) { //left side of screen
                        character.setMoveX(-15); //moves left in x direction
                        playerStartedMovingLeft = true;
                        playerStartedMovingRight = false;
                        playerClicked = true;
                        snapBackOccured = false;
                        break;
                    } else if (event.getX() > screenX / 2) { //right side of screen
                        character.setMoveX(+15); //moves right in x direction
                        playerStartedMovingRight = true;
                        playerStartedMovingLeft = false;
                        playerClicked = true;
                        snapBackOccured = false;
                        break;
                    }
                }
                break;
        }
        return true;
    }

    public void update(){

        platformCollection.move();
        score++;

        for (int i = 0; i < platformCollection.platformCount; i++) {
            distanceCharLeftToPlatformRight = difference(platformCollection.getRect(i).right,character.getRect().left);
            distanceCharRightToPlatformLeft = difference(platformCollection.getRect(i).left, character.getRect().right);
            distanceCharTopToPlatformBottom = difference(platformCollection.getRect(i).bottom, character.getRect().top);



            if (Rect.intersects(character.getRect(), platformCollection.getRect(i))) {
                platformCollisionIndex = i;
                snapBack();
                playerClicked = false;//we set playerclicked to false after collision so he can move again
                character.setMoveX(0);
                playerJumpedAndCollided = true;

            }
            if (goingToDie()){
                character.setBm(dyingBm);

            }

            if(playerJumpedAndCollided == true && playerClicked == false){
                if(platformCollection.platformMovedBelowCharacter(platformCollisionIndex)){ //the platform that collided with character has moved below the character
                    Log.d("GameView::update", " platform has moved below character");
                    continueMovingX(); //platform falls off screen in direction it was previously moving
                }
            }
            if(Rect.intersects(character.getRect(), platformCollection.getRect(i)) && platformCollection.characterTopIsTouchingPlatformBottom(platformCollisionIndex)){
                playerClicked = false;
                character.setMoveX(0);
                character.setMoveY(platformCollection.getPlatformSpeed());
                playerTopTouchedPlatformBottom = true;
            }
        }
        if(character.x < 0 - character.getBm().getWidth() ||
                character.x > screenX - character.getBm().getWidth()){
            isGameOver = true;
        }
        if(character.getY() > Constants.SCREEN_HEIGHT){
            isGameOver = true;
        }

    }

    public Boolean goingToDie() {

        Boolean charBeyondAllPlatformsLeft = false;
        Boolean charBeyondAllPlatformsRight = false;

    if(playerStartedMovingRight) {

            int tmpPlatForm = platformCollection.getFurthestRightPlatformLeftXInView();

                if (playerJumpedAndCollided&&playerStartedMovingRight) {
                    if (character.getX()+character.getWidth() > tmpPlatForm) {
                        charBeyondAllPlatformsLeft = true;      //sets to false only if this condition happens
                        Log.d("PFC::move", " charBeyondAllPlatformsLeft = " + charBeyondAllPlatformsLeft);
                    }
                }
                else {
                    if (character.getX()+character.getWidth() > tmpPlatForm) {
                        charBeyondAllPlatformsLeft = true;
                    }
                }
        }

    if(playerStartedMovingLeft) {
            int tmpPlatForm = platformCollection.getFurthestLeftPlatformRightXInView();

                if (playerJumpedAndCollided&&playerStartedMovingLeft) {
                    if (character.getX() < tmpPlatForm) {
                        charBeyondAllPlatformsRight = true;
                    }
                }
                else {
                    if (character.getX() < tmpPlatForm) {
                        charBeyondAllPlatformsRight = true;
                    }
                }
            }
        Boolean GoingToDie= (playerTopTouchedPlatformBottom ||
                (playerStartedMovingRight && snapBackOccured && charBeyondAllPlatformsLeft) ||
                (playerStartedMovingLeft && snapBackOccured && charBeyondAllPlatformsRight) ||
                (!playerJumpedAndCollided && charBeyondAllPlatformsLeft) ||
                (!playerJumpedAndCollided && charBeyondAllPlatformsRight));

        return GoingToDie;
    }

   public void snapBack(){

        if(smallestNum(distanceCharLeftToPlatformRight, distanceCharRightToPlatformLeft, distanceCharTopToPlatformBottom) == distanceCharLeftToPlatformRight){
            character.setX(platformCollection.getRect(platformCollisionIndex).right);
            snapBackOccured = true;
            characterRectLeftAfterSnapback = character.getRect().left;
        }
        if(smallestNum(distanceCharLeftToPlatformRight, distanceCharRightToPlatformLeft, distanceCharTopToPlatformBottom) == distanceCharRightToPlatformLeft){
            character.setX(platformCollection.getRect(platformCollisionIndex).left - character.width);
            snapBackOccured = true;
            characterRectRightAfterSnapback = character.getRect().right;
        }
        if(smallestNum(distanceCharLeftToPlatformRight, distanceCharRightToPlatformLeft, distanceCharTopToPlatformBottom) == distanceCharTopToPlatformBottom){
            character.setY(platformCollection.getRect(platformCollisionIndex).bottom);
            characterRectTopAfterSnapback = character.getY();
        }

    }

    public int smallestNum(int num1, int num2, int num3){
        int s = Math.min(num1, num2);
        s = Math.min(s, num3);
        return s;
    }

    public void continueMovingX(){
        if(playerStartedMovingRight == true){
            character.setMoveX(15);
        }
        else{
            character.setMoveX(-15);
        }
    }

    public int difference(int var1, int var2){
        return Math.abs(var2 - var1);
    }
    public void setCanvas(Canvas canvas){
        this.canvasRetriever = canvas;
    }
    public Canvas getCanvas(){
        return canvasRetriever;
    }
    public static void stopMusic(){
        gameMusic.stop();
    }

}
