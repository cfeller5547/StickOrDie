package com.example.superherorun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    private Character character;
    private Platforms platform;
    private PlatformCollection platformCollection;
    static MediaPlayer playerJumpSound;
    static MediaPlayer gameMusic;
    private boolean isPlaying = true;
    private boolean isGameOver = false;
    private boolean playerClicked = false;
    private Canvas canvasRetriever;
    private int score = 0;
    private Random random;
    private Paint paint;
    private Bitmap bm;
    private Handler handler;
    private Runnable r;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private int platformCollisionIndex;
    private boolean playerJumpedAndCollided = false;
    private boolean playerStartedMovingLeft = false;
    private boolean playerStartedMovingRight = false;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        gameMusic = MediaPlayer.create(context,R.raw.growingonme);
        gameMusic.start();
        playerJumpSound = MediaPlayer.create(context, R.raw.playerjump);
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.RED);
        character = new Character();
        platformCollection = new PlatformCollection(context, screenX, screenY, character);
        random = new Random();
        character.setWidth(200*Constants.SCREEN_WIDTH/1080);
        character.setHeight(200*Constants.SCREEN_HEIGHT/1920);
        character.setX(400*Constants.SCREEN_WIDTH/1080);
        character.setY(Constants.SCREEN_HEIGHT/2-character.getHeight()/2);
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        bm = Bitmap.createScaledBitmap(bm, character.getWidth(), character.getHeight(), false);
        character.setBm(bm);
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
        canvas.drawText(score + "", screenX / 2f, 164, paint);

        handler.postDelayed(r, 1);

        if(isGameOver){
            canvas.drawText("Game Over", screenX / 4f, screenY / 2f, paint);
            Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
            gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            gameOverIntent.putExtra("score", score);
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
                    } else if (event.getX() > screenX / 2) { //right side of screen
                        character.setMoveX(+15); //moves right in x direction
                        playerStartedMovingRight = true;
                        playerStartedMovingLeft = false;
                        playerClicked = true;
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

            if (Rect.intersects(character.getRect(), platformCollection.getRect(i))) {
                playerClicked = false;//we set playerclicked to false after collision so he can move again
                platformCollisionIndex = i;
                character.setMoveX(0);
                playerJumpedAndCollided = true;
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

    public void continueMovingX(){
        if(playerStartedMovingRight == true){
            character.setMoveX(15);
        }
        else{
            character.setMoveX(-15);
        }
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
