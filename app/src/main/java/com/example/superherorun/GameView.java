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
    private Platforms plat;
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
    int bound = 800;

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



        //character.setArrBms(arrBms);
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                //while(isPlaying) {
                invalidate();
                update();


                }
            //}
        };
    }



    public void draw(Canvas canvas){
        super.draw(canvas);

        character.draw(canvas);
        platformCollection.draw(canvas);

        canvas.drawText(score + "", screenX / 2f, 164, paint);

        handler.postDelayed(r, 10);

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
                    playerClicked = true;
                    if (event.getX() < screenX / 2) { //left side of screen
                        playerJumpSound.start();
                        character.setMove(-15);
                        //Log.d("GameView::onTouchEvent", "Character x moved now");

                    } else if (event.getX() > screenX / 2) { //right side of screen
                        playerJumpSound.start();
                        character.setMove(+15);
                        //Log.d("GameView::onTouchEvent", "Character x moved now");
                    }
                }
                break;
        }
        return true;
    }

    public void update(){

        platformCollection.movePlatformCollection();

        score++;

        for (int i = 0; i < platformCollection.platformCount; i++) {

            if (Rect.intersects(character.getRect(), platformCollection.getRect(i))) {
                character.setMove(0); //is there a more dynamic way to make it that character stop
                playerClicked = false;
            }
        }


        if(character.x < 0 - character.getBm().getWidth() ||
                character.x > screenX - character.getBm().getWidth()){
            isGameOver = true;

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
