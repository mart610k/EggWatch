package dk.mart610k.eggwatch.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.mart610k.eggwatch.R;


import static android.content.Context.VIBRATOR_SERVICE;

public class EggTimer extends Thread {

    int seconds;
    boolean isRunning;
    private MediaPlayer mediaPlayer;
    private Context context;
    IEggTimerListener iEggTimerListener;

    public EggTimer(int secondsToSet, Context context){
        seconds = secondsToSet;
        this.context = context;
        mediaPlayer = MediaPlayer.create(context, R.raw.mixkitwarningalarmbuzzer991);
    }

    public void setIEggTimeListener(IEggTimerListener iEggTimerListener){
        this.iEggTimerListener = iEggTimerListener;
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    @Override
    public void  run(){
        isRunning = true;

        try{
            while(!this.currentThread().isInterrupted()) {

                while (seconds >= 0) {

                    this.sleep(1000);

                    if (iEggTimerListener != null) {
                        iEggTimerListener.onCountDown(seconds);
                    }
                    seconds--;
                }
                startAlarm();
            }
        }
        catch (InterruptedException interruptedException){
            releaseAndStopResources();
            isRunning = false;
        }

    }

    /**
     * Stops the Vibrator and media player interupts the thread.
     */
    public void stopTimer(){
        this.interrupt();
        isRunning = false;
    }

    public void releaseAndStopResources(){
        mediaPlayer.stop();
        mediaPlayer.release();
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.cancel();
    }

    /**
     * Starts the media player and vibrator for the alarm
     */
    private void startAlarm(){
        if(iEggTimerListener != null){
            iEggTimerListener.onEggTimerStopped();
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vibrator == null){

        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{500, 200, 500, 200, 500, 200, 500, 200, 500},new int[]{255, 0, 255, 0, 255, 0, 255, 0, 255} ,-1));
        } else {
            vibrator.vibrate(2000);
        }
        mediaPlayer.start();
    }
}
