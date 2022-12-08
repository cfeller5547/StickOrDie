package com.StickOrDie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class GameOverShield {

    public int shieldWidth = (Constants.SCREEN_WIDTH*3) / 4; //=1080
    public int shieldX1 = (Constants.SCREEN_WIDTH/2) - (shieldWidth/2); //=180
    public int shieldX2 = shieldX1 + shieldWidth;
    public int shieldHeight = Constants.SCREEN_HEIGHT/2; //=1310
    public int shieldY1 = (Constants.SCREEN_HEIGHT/2) - (shieldHeight/2); //=655
    public int shieldY2 = shieldY1+shieldHeight; //=1965
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Bitmap shieldBitmap;


    //play again button rect
    int rect1Leftx = ((shieldX1)+(shieldWidth/4)); // =360
    int shieldX1Rect1LeftxDiff = Math.abs(rect1Leftx - shieldX1); //=180
    int rect1Topy = shieldY1+(shieldHeight/2); // =1250
    int rect1Rightx = (shieldX2-shieldX1Rect1LeftxDiff); // =1050;
    int rect1Bottomy = Math.round((Constants.SCREEN_WIDTH*3)/4); // =1050;



    //main menu button rect
    int rect2Leftx = ((rect1Leftx)+(rect1Leftx/8)); // =450
    int shieldX1Rect2LeftxDiff = Math.abs(rect2Leftx - shieldX1);
    float rect2Topy = Math.round(Constants.SCREEN_HEIGHT/1.678f); // =1550
    int rect2Rightx = (shieldX2 - shieldX1Rect2LeftxDiff); // =950;
    float rect2Bottomy = Math.round(Constants.SCREEN_HEIGHT/1.7937f); // =1450;
    float rect2Height = Math.abs(rect2Topy - rect2Bottomy);

    //playagaintext
    float playAgainTextYLocation;
    float playAgainTextXLocation;

    //mainmenutext
    float mainMenuTextYLocation;
    float mainMenuTextXLocation;

    GameOverShield(Context context) {
        shieldBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield10);
        shieldBitmap = Bitmap.createScaledBitmap(shieldBitmap, shieldWidth, shieldHeight, false);
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.grey_dark2));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(50);

        paint2 = new Paint();
        paint2.setColor(ContextCompat.getColor(context, R.color.grey_dark2));
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setStrokeWidth(50);

        //playagaintext
        paint3 = new Paint();
        paint3.setColor(Color.WHITE);
        Typeface audioWideFont = ResourcesCompat.getFont(context, R.font.audiowide);
        paint3.setTextSize(Math.round(Constants.SCREEN_WIDTH/16f));
        paint3.setTypeface(audioWideFont);

        //mainmenutext
        paint4 = new Paint();
        paint4.setColor(Color.WHITE);
        audioWideFont = ResourcesCompat.getFont(context, R.font.audiowide);
        paint4.setTextSize((paint3.getTextSize()*3)/4);
        paint4.setTypeface(audioWideFont);

        //playagaintext

        playAgainTextYLocation = (rect1Bottomy+rect1Topy) /2f;
        playAgainTextXLocation = ((rect1Leftx+rect1Rightx) /2f) - 3f*(paint3.getTextSize());

        //mainmenutext
        mainMenuTextYLocation = (rect2Bottomy+rect2Topy) /2f;
        mainMenuTextXLocation = ((rect2Leftx+rect2Rightx)/2f)- 3f*(paint4.getTextSize());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(shieldBitmap, shieldX1, shieldY1, null);
        canvas.drawRect(rect1Leftx, rect1Topy, rect1Rightx, rect1Bottomy, paint);
        canvas.drawRect(rect2Leftx, rect2Topy, rect2Rightx, rect2Bottomy, paint2);
        canvas.drawText("Play Again", playAgainTextXLocation, playAgainTextYLocation, paint3);
        canvas.drawText("Main Menu", mainMenuTextXLocation, mainMenuTextYLocation, paint4);


    }




}