package com.example.superherorun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {


    private Button StartGameAgain;
    private TextView DisplayScore;
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        score = getIntent().getExtras().get("score").toString();

        StartGameAgain = (Button) findViewById(R.id.play_again_btn);
        DisplayScore =  (TextView) findViewById(R.id.displayScore);

        StartGameAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(MainIntent);
            }
        });

        DisplayScore.setText("Score: " + score);
    }

}