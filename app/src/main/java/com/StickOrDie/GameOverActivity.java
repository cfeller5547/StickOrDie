package com.StickOrDie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private Button StartGameAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_over);

        StartGameAgain = (Button) findViewById(R.id.play_again_btn);

        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);
        TextView scoreLabel = (TextView) findViewById(R.id.displayScore);
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);
        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText("Score: " + score);

        StartGameAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(MainIntent);
            }
        });



        if(score > highScore){
            highScoreLabel.setText("High Score: " + score);

            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }
        else{
            highScoreLabel.setText("High Score: " + highScore);
        }
    }

}