package com.example.superherorun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class GameView extends View {
    private Character character;
    private PlatformCollection platformCollection;
    private SoundPlayer sound;
    static MediaPlayer gameMusic;
    MyTimer timer;
    private boolean firstClick = false;
    private boolean isPlaying = true;
    private boolean isGameOver = false;
    private boolean playerClicked = false;
    private boolean playerTopTouchedPlatformBottom = false;
    private Random random;
    private Paint paint;
    private Bitmap bm;
    private Bitmap dyingBm, leftBm, rightBm;
    private Handler handler;
    private Runnable r;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private int platformCollisionIndex;
    private boolean playerJumpedAndCollided = false;
    private boolean playerStartedMovingLeft = false;
    private boolean playerStartedMovingRight = false;
    private boolean goingToContinueMovingX = false;
    private boolean snapBackOccured = false;
    private int collideCount = 0;
    static int distanceCharRightToPlatformLeft;
    static int distanceCharLeftToPlatformRight;
    static int distanceCharTopToPlatformBottom;
    public int score;
    boolean speedPhase1 = false;
    boolean speedPhase2 = false;
    boolean speedPhase3 = false;
    boolean speedPhase4 = false;
    private int moveNum15 = (Constants.SCREEN_WIDTH/72); //=15

    public GameView(Context context, int screenX, int screenY) {

        super(context);

        gameMusic = MediaPlayer.create(context, R.raw.growingonme);
        gameMusic.start();
        sound = new SoundPlayer(this.getContext());

        paint = new Paint();
        Typeface audioWideFont = ResourcesCompat.getFont(context, R.font.audiowide);
        paint.setTextSize(Math.round(Constants.SCREEN_WIDTH/8.4375));
        paint.setTypeface(audioWideFont);
        paint.setColor(Color.BLACK);
        character = new Character();
        random = new Random();
        character.setWidth(200*Constants.SCREEN_WIDTH/1080);
        character.setHeight(200*Constants.SCREEN_HEIGHT/1920);
        character.setX((400*Constants.SCREEN_WIDTH/1080) + (Constants.SCREEN_WIDTH/27));
        character.setY((int) ((Constants.SCREEN_HEIGHT/2-character.getHeight()/2) + (Constants.SCREEN_WIDTH/3.6)));
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        bm = Bitmap.createScaledBitmap(bm, character.getWidth(), character.getHeight(), false);
        dyingBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.characterdying);
        dyingBm = Bitmap.createScaledBitmap(dyingBm, character.getWidth(), character.getHeight(), false);
        leftBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.charactermovingleft);
        leftBm = Bitmap.createScaledBitmap(leftBm, character.getWidth(), character.getHeight(), false);
        rightBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.charactermovingright);
        rightBm = Bitmap.createScaledBitmap(rightBm, character.getWidth(), character.getHeight(), false);
        character.setBm(bm);
        platformCollection = new PlatformCollection(context, screenX, screenY, character);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;
        timer = new MyTimer();
        handler = new Handler();
        gameMusic.start();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
                update();
                }
        };
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        character.draw(canvas);
        platformCollection.draw(canvas);
        canvas.drawText(score + "", Constants.SCREEN_WIDTH / 2f, Math.round(Constants.SCREEN_WIDTH/6.58536f), paint); //fix universality

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

                if(firstClick ==  false){
                    timer.startTimer();
                }
                firstClick = true;

                if(playerClicked == false) {
                    if (event.getX() < screenX / 2) { //left side of screen
                        character.setMoveX(-moveNum15); //moves left in x direction; FIX UNIVERSALITY
                        sound.playJumpSound();
                        playerStartedMovingLeft = true;
                        playerStartedMovingRight = false;
                        playerClicked = true;
                        snapBackOccured = false;
                        playerJumpedAndCollided = false;
                        break;
                    } else if (event.getX() > screenX / 2) { //right side of screen
                        character.setMoveX(moveNum15); //moves right in x direction; FIX UNIVERSALITY
                        sound.playJumpSound();
                        playerStartedMovingRight = true;
                        playerStartedMovingLeft = false;
                        playerClicked = true;
                        snapBackOccured = false;
                        playerJumpedAndCollided = false;
                        break;
                    }
                }
                break;
        }
        return true;
    }

    public void update(){

        platformCollection.move();
        score = timer.getTimerSeconds();

        for (int i = 0; i < platformCollection.platformCount; i++) {
            distanceCharLeftToPlatformRight = difference(platformCollection.getRect(i).right,character.getRect().left);
            distanceCharRightToPlatformLeft = difference(platformCollection.getRect(i).left, character.getRect().right);
            distanceCharTopToPlatformBottom = difference(platformCollection.getRect(i).bottom, character.getRect().top);

            if (Rect.intersects(character.getRect(), platformCollection.getRect(i))) {
                collideCount++;
                playerJumpedAndCollided = true;
                platformCollisionIndex = i;
                snapBack();
                playerClicked = false;//we set playerclicked to false after collision so he can move again
                character.setMoveX(0);
            }

            if (goingToDie()){
                character.setBm(dyingBm);
                sound.playDyingScreamSound();
            }
            else{
                bitmapEvaluator();
            }

            if(playerJumpedAndCollided == true && playerClicked == false){
                if(platformCollection.platformMovedBelowCharacter(platformCollisionIndex)){ //the platform that collided with character has moved below the character
                    continueMovingX(); //platform falls off screen in direction it was previously moving
                    playerClicked = true;
                    snapBackOccured = false;
                }
            }

            if(snapBackOccured && playerTopTouchedPlatformBottom) {
                character.setMoveX(0);
                character.setMoveY(platformCollection.getPlatformSpeed());
                playerClicked = true;
            }
        }

        increasePlatSpeedIntervals();

        if(character.x < 0 - character.getBm().getWidth() ||
                character.x > screenX - character.getBm().getWidth()){
            isGameOver = true;
        }
        if(character.getY() > Constants.SCREEN_HEIGHT){
            isGameOver = true;
        }

    }

    public void increasePlatSpeedIntervals(){

        if(score >= 15 && !speedPhase1){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase1 = true;
        }
        if(score >= 30 && !speedPhase2){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase2 = true;
        }
        if(score >= 45 && !speedPhase3){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase3 = true;
        }
        if(score >= 60 && !speedPhase4){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase4 = true;
        }

    }

    public void bitmapEvaluator(){
            if (character.movingRight()) {
                character.setBm(rightBm);
            }
            if (character.movingLeft()) {
                character.setBm(leftBm);
            }
            if (character.getMoveX() == 0) {
                character.setBm(bm);
            }
        }


    public Boolean goingToDie() {

        Boolean charBeyondAllPlatformsLeft = false;
        Boolean charBeyondAllPlatformsRight = false;

    if(character.movingRight()) {

            int tmpPlatForm = platformCollection.getFurthestRightPlatformLeftXInView();

                if (snapBackOccured) {
                        if (character.getX() + character.getWidth() > tmpPlatForm) { //character right greater than furthest rightPlatLeftXCoordinate
                            charBeyondAllPlatformsLeft = true;
                        }
                }
                if(collideCount == 0){
                    if (character.getX() + character.getWidth() > tmpPlatForm) { //character right greater than furthest rightPlatLeftXCoordinate
                        charBeyondAllPlatformsLeft = true;
                    }
                }

        }

    if(character.movingLeft()) {

            int tmpPlatForm = platformCollection.getFurthestLeftPlatformRightXInView();

                if (snapBackOccured) {
                        if (character.getX() < tmpPlatForm) {
                            charBeyondAllPlatformsRight = true;
                        }
                }
            if(collideCount == 0){
                if (character.getX() < tmpPlatForm) {
                    charBeyondAllPlatformsRight = true;
                }
            }

    }
        Boolean GoingToDie= (playerTopTouchedPlatformBottom || charBeyondAllPlatformsRight || charBeyondAllPlatformsLeft );

        return GoingToDie;
    }

   public void snapBack(){

        if(smallestNum(distanceCharLeftToPlatformRight, distanceCharRightToPlatformLeft, distanceCharTopToPlatformBottom) == distanceCharLeftToPlatformRight){
            character.setX(platformCollection.getRect(platformCollisionIndex).right);
            snapBackOccured = true;
        }
        if(smallestNum(distanceCharLeftToPlatformRight, distanceCharRightToPlatformLeft, distanceCharTopToPlatformBottom) == distanceCharRightToPlatformLeft){
            character.setX(platformCollection.getRect(platformCollisionIndex).left - character.width);
            snapBackOccured = true;
        }
        if(smallestNum(distanceCharLeftToPlatformRight, distanceCharRightToPlatformLeft, distanceCharTopToPlatformBottom) == distanceCharTopToPlatformBottom){
            character.setY(platformCollection.getRect(platformCollisionIndex).bottom);
            snapBackOccured = true;
            playerTopTouchedPlatformBottom = true;
        }

    }

    public int smallestNum(int num1, int num2, int num3){
        int s = Math.min(num1, num2);
        s = Math.min(s, num3);
        return s;
    }

    public void continueMovingX(){
        if(playerStartedMovingRight == true){
            character.setMoveX(moveNum15);
        }
        else{
            character.setMoveX(-moveNum15);
        }
    }

    public int difference(int var1, int var2){
        return Math.abs(var2 - var1);
    }



}
