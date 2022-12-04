package com.StickOrDie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

public class GameOverShield {

    public int shieldWidth = (Constants.SCREEN_WIDTH*3) / 4;
    public int shieldX1 = (Constants.SCREEN_WIDTH/2) - (shieldWidth/2);
    public int shieldX2 = shieldX1+shieldWidth;
    public int shieldHeight = Constants.SCREEN_HEIGHT/2;
    public int shieldY1 = (Constants.SCREEN_HEIGHT/2) - (shieldHeight/2);
    public int shieldY2 = shieldY1 + shieldHeight;
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Bitmap shieldBitmap;

    //play again button rect
    float rect1Leftx = Math.round(Constants.SCREEN_HEIGHT/6.5025); // =400
    float rect1Topy = Math.round(Constants.SCREEN_HEIGHT/2.0808); // =1250
    float rect1Rightx = Math.round(Constants.SCREEN_HEIGHT/2.601); // =1000;
    float rect1Bottomy = Math.round(Constants.SCREEN_HEIGHT/2.477142); // =1050;

    //main menu button rect
    float rect2Leftx = Math.round(Constants.SCREEN_HEIGHT/5.78); // =450
    float rect2Topy = Math.round(Constants.SCREEN_HEIGHT/1.678); // =1550
    float rect2Rightx = Math.round(Constants.SCREEN_HEIGHT/2.7378); // =950;
    float rect2Bottomy = Math.round(Constants.SCREEN_HEIGHT/1.7937); // =1450;
    float rect2Height = Math.abs(rect2Topy - rect2Bottomy);

    //playagaintext
    float playAgainTextYLocation;
    float playAgainTextXLocation;

    //mainmenutext
    float mainMenuTextYLocation;
    float mainMenuTextXLocation;

    GameOverShield(Context context) {
        shieldBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameovershield);
        shieldBitmap = Bitmap.createScaledBitmap(shieldBitmap, shieldWidth, shieldHeight, false);
        paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(100);

        paint2 = new Paint();
        paint2.setColor(Color.DKGRAY);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setStrokeWidth(100);

        //playagaintext
        paint3 = new Paint();
        paint3.setColor(Color.WHITE);
        Typeface audioWideFont = ResourcesCompat.getFont(context, R.font.audiowide);
        paint3.setTextSize(Math.round(Constants.SCREEN_WIDTH/16));
        paint3.setTypeface(audioWideFont);

        //mainmenutext
        paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        audioWideFont = ResourcesCompat.getFont(context, R.font.audiowide);
        paint4.setTextSize(Math.round(Constants.SCREEN_WIDTH/16));
        paint4.setTypeface(audioWideFont);

        //playagaintext

        playAgainTextYLocation = (rect1Bottomy+rect1Topy) /2;
        playAgainTextXLocation = ((rect1Leftx+rect1Rightx) /2) - 3*(paint3.getTextSize());

        //mainmenutext
        mainMenuTextYLocation = (rect2Bottomy+rect2Topy) /2;
        mainMenuTextXLocation = ((rect2Leftx+rect2Rightx)/2)- 3*(paint4.getTextSize());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(shieldBitmap, shieldX1, shieldY1, null);
        canvas.drawRect(rect1Leftx, rect1Topy, rect1Rightx, rect1Bottomy, paint);
        canvas.drawRect(rect2Leftx, rect2Topy, rect2Rightx, rect2Bottomy, paint2);
        canvas.drawText("Play Again", playAgainTextXLocation, playAgainTextYLocation, paint3);
        canvas.drawText("Main Menu", mainMenuTextXLocation, mainMenuTextYLocation, paint3);


    }




}