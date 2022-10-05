package com.example.superherorun;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {


    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);


        gameView = new GameView(this,size.x, size.y);

        setContentView(gameView);

    }
}
