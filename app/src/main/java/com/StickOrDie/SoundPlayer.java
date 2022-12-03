package com.StickOrDie;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {
    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;
    private static SoundPool soundPool;
    private static int jumpSound;
    private static int dyingScreamSound;
    private static int phaseChangeSound;
    private static int playerUnderSpikesSound;


    public SoundPlayer(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder().
                    setUsage(AudioAttributes.USAGE_GAME).
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }
        else{
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        jumpSound = soundPool.load(context, R.raw.playerjump, 1);
        dyingScreamSound = soundPool.load(context, R.raw.dyingscream, 1);
        phaseChangeSound = soundPool.load(context, R.raw.phasechangesound, 1);
        playerUnderSpikesSound = soundPool.load(context, R.raw.playerunderspikessound, 1);

    }


    public void playDyingScreamSound(){
        soundPool.play(dyingScreamSound, 1.0f, 1.0f,1, 0,1.0f);
    }

    public void stopDyingScreamSound()
    {
        if ((soundPool != null) && (dyingScreamSound != 0))
        {
            soundPool.stop(dyingScreamSound);
        }
    }
    public void playJumpSound(){
        soundPool.play(jumpSound, 1.0f, 1.0f,1, 0,1.0f);
    }
    public void playPhaseChangeSound(){
        soundPool.play(phaseChangeSound, 1.0f, 1.0f,1, 0,1.0f);
    }
    public void playPlayerUnderSpikesSound(){
        soundPool.play(playerUnderSpikesSound, 1.0f, 1.0f,1, 0,1.0f);
    }

}
