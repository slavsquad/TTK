package com.slavsquad.TTK;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class <>TimerLabel</> implements timer in JLabel component
 * Timer count seconds and displays passing seconds and minutes
 */
public class TimerLabel extends JLabel
{
    private Timer timer;
    TimerTask timerTask;
    private int seconds;
    private volatile boolean pause;

    /**
     * Construct new object and initialize some field*/
    public TimerLabel ()
    {
        timer = new Timer();
        //restart();
    }

    /**
     * Method restart timer and paint on JLabel zero seconds.*/
    public void restart()
    {
        stop();
        pause=false;
        timerTask = new TimerTask() {
            private volatile int time = -1;
            @Override
            public void run() {
                if (!pause) {
                    time++;
                    ///SwingUtilities.invokeLater(() -> TimerLabel.this.setText(String.format("%02d:%02d", time / 60, time % 60)));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            TimerLabel.this.setText(String.format("%02d:%02d", time / 60, time % 60));
                        }
                    });
                    seconds = time;
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * Method stops timer*/
    public void stop ()
    {
        if (timerTask != null){
            timerTask.cancel();
        }
    }

    /**
     * Method paused timer*/
    public void pause(){
        pause = true;
    }

    /**
     * Method resumes work of timer*/
    public void proceed(){
        pause=false;
    }

    /**
     * Method gets quantity seconds of timer*/
    public int getSeconds() {
        return seconds;
    }
}