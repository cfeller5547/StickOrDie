package com.StickOrDie;
import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
    Timer timer = new Timer();

    TimerTask timerTask;
    int time = 0;

    public void startTimer(){
        timerTask = new TimerTask()
        {
            @Override
            public void run(){
                time++;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public int getTimerSeconds(){
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 5000;
        return seconds;
    }

    public void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    public void resetTimer(){
        if(timerTask!= null){
            timerTask.cancel();
            time = 0;
        }
    }

}
