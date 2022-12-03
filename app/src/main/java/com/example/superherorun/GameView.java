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
    private long currentGameOverTime = 0;
    static MediaPlayer gameMusic;
    MyTimer timer;
    MyTimer deathTimer;
    private boolean firstClick = false;
    private boolean isPlaying = true;
    private boolean isGameOver = false;
    private boolean playerClicked = false;
    private boolean playerTopTouchedPlatformBottom = false;
    private boolean dyingBmInit = false;
    private boolean dyingLeftBmInit = false;
    private boolean dyingRightBmInit = false;
    private boolean dyingScreamInit = false;
    private boolean phase1ChangeSoundInit = false;
    private boolean phase2ChangeSoundInit = false;
    private boolean phase3ChangeSoundInit = false;
    private boolean phase4ChangeSoundInit = false;
    private boolean phase5ChangeSoundInit = false;
    private boolean phase6ChangeSoundInit = false;
    private boolean phase7ChangeSoundInit = false;
    private boolean playerUnderSpikesSoundInit = false;
    private Random random;
    private Paint paint, paint2;
    private Bitmap bm;
    private Bitmap dyingBm, leftBm, rightBm, dyingRightBm, dyingLeftBm;
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
    boolean speedPhase5 = false;
    boolean speedPhase6 = false;
    boolean speedPhase7 = false;
    boolean dyingPauseInit = false;
    boolean playSound = false;
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
        paint2 = new Paint();
        paint2.setTextSize(Math.round(Constants.SCREEN_WIDTH/8.4375));
        paint2.setTypeface(audioWideFont);
        paint2.setColor(getResources().getColor(R.color.grey_light));
        character = new Character();
        random = new Random();
        character.setWidth(200*Constants.SCREEN_WIDTH/1080);
        character.setHeight(200*Constants.SCREEN_HEIGHT/1920);
        character.setX((Constants.SCREEN_WIDTH/2) - (character.getWidth()/2));
        character.setY((int) ((Constants.SCREEN_HEIGHT/2-character.getHeight()/2) + (Constants.SCREEN_WIDTH/3.6)));

        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        bm = Bitmap.createScaledBitmap(bm, character.getWidth(), character.getHeight(), false);
        dyingBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.characterdying);
        dyingBm = Bitmap.createScaledBitmap(dyingBm, character.getWidth(), character.getHeight(), false);
        dyingRightBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.characterdyingright);
        dyingRightBm = Bitmap.createScaledBitmap(dyingRightBm, character.getWidth(), character.getHeight(), false);
        dyingLeftBm = BitmapFactory.decodeResource(context.getResources(), R.drawable.characterdyingleft);
        dyingLeftBm = Bitmap.createScaledBitmap(dyingLeftBm, character.getWidth(), character.getHeight(), false);
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

        if(firstClick == false){
            canvas.drawText("tap to play", Constants.SCREEN_WIDTH / 8f, character.getY()-character.getBm().getHeight(), paint2); //fix universality

        }
        canvas.drawText(score + "", Constants.SCREEN_WIDTH / 2f, Math.round(Constants.SCREEN_WIDTH/6.58536f), paint); //fix universality

        handler.postDelayed(r, 1);

        if(isGameOver){
            platformCollection.freezePlatforms();
            if(snapBackOccured && playerTopTouchedPlatformBottom){ //hes dead under spikes stop scream
                sound.stopDyingScreamSound();
                if(playerUnderSpikesSoundInit == false) {
                    sound.playPlayerUnderSpikesSound();
                    playerUnderSpikesSoundInit = true;
                }
            }
            //dont start activity until 4 seconds passed
            if(System.currentTimeMillis()-currentGameOverTime > 3000){
                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("SCORE", score);
                getContext().startActivity(gameOverIntent);
                gameMusic.stop();
            }
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
                        character.setMoveX(-character.getSpeed()); //moves left in x direction; FIX UNIVERSALITY
                        sound.playJumpSound();
                        playerStartedMovingLeft = true;
                        playerStartedMovingRight = false;
                        playerClicked = true;
                        snapBackOccured = false;
                        playerJumpedAndCollided = false;
                        break;
                    } else if (event.getX() > screenX / 2) { //right side of screen
                        character.setMoveX(character.getSpeed()); //moves right in x direction; FIX UNIVERSALITY
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
            character.setSpeed(platformCollection.getPlatformSpeed() + 8);

            if (Rect.intersects(character.getRect(), platformCollection.getRect(i))) {
                collideCount++;
                playerJumpedAndCollided = true;
                platformCollisionIndex = i;
                snapBack();
                playerClicked = false;//we set playerclicked to false after collision so he can move again
                character.setMoveX(0);
            }

            bitmapEvaluator();

            if(playerJumpedAndCollided == true && playerClicked == false){
                if(platformCollection.platformMovedBelowCharacter(platformCollisionIndex)){ //the platform that collided with character has moved below the character
                    continueMovingX(); //platform falls off screen in direction it was previously moving
                    playerClicked = true;
                    snapBackOccured = false;
                }
            }

            if(snapBackOccured && playerTopTouchedPlatformBottom) { //touched under the spikes
                character.setMoveX(0);
                playerClicked = true;
                gameOver();
            }
        }

        increasePlatSpeedIntervals();

        if(character.x < 0 - character.getBm().getWidth() ||
                character.x > screenX - character.getBm().getWidth()){
            gameOver();
        }

    }

    public void gameOver(){
        if(isGameOver == false) {
            currentGameOverTime = System.currentTimeMillis();
            timer.stopTimer();
        }
        isGameOver = true;
    }

    public void increasePlatSpeedIntervals(){

        if(score >= 15 && !speedPhase1){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase1 = true;
            if(!phase1ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }
        if(score >= 30 && !speedPhase2){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase2 = true;
            if(!phase2ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }
        if(score >= 45 && !speedPhase3){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase3 = true;
            if(!phase3ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }
        if(score >= 60 && !speedPhase4){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase4 = true;
            if(!phase4ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }
        if(score >= 75 && !speedPhase5){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase5 = true;
            if(!phase5ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }
        if(score >= 90 && !speedPhase6){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase6 = true;
            if(!phase6ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }
        if(score >= 105 && !speedPhase7){
            platformCollection.increasePlatformSpeedBy2();
            speedPhase7 = true;
            if(!phase7ChangeSoundInit){
                sound.playPhaseChangeSound();
            }
        }

    }

    public void bitmapEvaluator() {
        if (goingToDie()) {
            //under spikes condition
            if (!dyingBmInit&& playerTopTouchedPlatformBottom) {
                character.setBm(dyingBm);
                dyingBmInit = true;

            }
            //dyingrightdirection
            if (!dyingRightBmInit&& character.movingRight()) {
                character.setBm(dyingRightBm);
                dyingRightBmInit = true;
                if(dyingScreamInit == false) {
                    sound.playDyingScreamSound();
                    dyingScreamInit = true;
                }
            }
            //dyingleftdirection
            if (!dyingLeftBmInit&& character.movingLeft()) {
                character.setBm(dyingLeftBm);
                dyingLeftBmInit = true;
                if(dyingScreamInit == false) {
                    sound.playDyingScreamSound();
                    dyingScreamInit = true;

                }
            }
        }
        //alive conditions
        else {
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
            character.setMoveX(character.getSpeed());
        }
        else{
            character.setMoveX(-character.getSpeed());
        }
    }

    public int difference(int var1, int var2){
        return Math.abs(var2 - var1);
    }



}
