package com.StickOrDie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;


public class MainActivity extends AppCompatActivity {
    private MediaPlayer gameStartMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayGamesSdk.initialize(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        final int RC_LEADERBOARD_UI = 9004;

        setContentView(R.layout.activity_main);
        gameStartMusic = MediaPlayer.create(MainActivity.this, R.raw.startscreenmusic);
        gameStartMusic.start();




        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameStartMusic.stop();
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        findViewById(R.id.leaderboards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(MainActivity.this);

                gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
                    boolean isAuthenticated =
                            (isAuthenticatedTask.isSuccessful() &&
                                    isAuthenticatedTask.getResult().isAuthenticated());

                    if (isAuthenticated) {
                        Toast.makeText(MainActivity.this, "Succesful!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        Log.e("Connection", "Unable to connect", isAuthenticatedTask.getException());

                    }
                });
            }
        });
    }
}