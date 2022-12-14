package com.StickOrDie;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;



public class MainActivity extends AppCompatActivity {
    private MediaPlayer gameStartMusic;
    private long initTime=0;
    private int rotateCount = 0;
    private final int RC_LEADERBOARD_UI = 9004;
    GamesSignInClient gamesSignInClient;
    boolean isAuthenticated = false;
    private PlayersClient playersClient;
    public String playerID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        PlayGamesSdk.initialize(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_main);
        gameStartMusic = MediaPlayer.create(MainActivity.this, R.raw.startscreenmusic);
        gameStartMusic.setLooping(true);
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
                gamesSignInClient = PlayGames.getGamesSignInClient(MainActivity.this);

                gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
                    isAuthenticated =
                            (isAuthenticatedTask.isSuccessful() &&
                                    isAuthenticatedTask.getResult().isAuthenticated());

                    if (isAuthenticated) { //we verified that the user signed in
                        Toast.makeText(MainActivity.this, "Succesful!", Toast.LENGTH_SHORT).show(); //google play sign in successful
                        PlayGames.getPlayersClient(MainActivity.this).getCurrentPlayer().addOnCompleteListener(mTask -> {
                                    playerID = mTask.getResult().getPlayerId(); //gettingplayerID to identify user
                                }
                        );
                        showLeaderboard();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        Log.e("Connection", "Unable to connect", isAuthenticatedTask.getException());

                    }
                });
            }
        });
    }

    private void showLeaderboard(){
        PlayGames.getLeaderboardsClient(MainActivity.this)
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    @SuppressWarnings("deprecation")
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

    public void addScores(int score){
        try {
            PlayGames.getLeaderboardsClient(MainActivity.this).submitScore(getString(R.string.leaderboard_id), score);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}