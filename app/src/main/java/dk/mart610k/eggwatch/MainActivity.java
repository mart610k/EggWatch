package dk.mart610k.eggwatch;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.mart610k.eggwatch.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    int timer = 0;
    int lastTimerSet = 0;
    boolean timerInProgress = false;

    Handler eggWatchHandler = new Handler();

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.mixkitwarningalarmbuzzer991);
    }

    /**
     * Helper method for enable multiple buttons and disable a button.
     * @param toDisable the button to disable
     * @param toEnable the buttons to enable
     */
    private void enableAndDisableButtons(int toDisable, int[] toEnable){
        for (int i = 0; i < toEnable.length; i++){
            enableButton(toEnable[i]);
        }
        disableButton(toDisable);
    }

    /**
     * Starts or stops the timer at the current location. also stops the media player if it is playing.
     * @param view the caller on the GUI.
     */
    public void start_stopTimerButton(View view){
        if(timerInProgress) {
            eggWatchHandler.removeCallbacksAndMessages(null);
            ((TextView) this.findViewById(R.id.current_status_textview)).setText(R.string.stopped_timer);
            ((Button)view).setText(R.string.timer_resume_text);

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        else {
            ((Button)view).setText(R.string.timer_pause_text);
            ((TextView) this.findViewById(R.id.current_status_textview)).setText(R.string.starter_timer);
            eggWatchHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (timer <= 0) {
                        startAlarm();
                    } else {
                        eggWatchHandler.postDelayed(this, 1000);
                        timer--;
                        updateTextInTimer();
                    }
                }
            });
        }
        if (timerInProgress) {
            enableButton(R.id.reset_button);
        } else {
            disableButton(R.id.reset_button);
        }

        timerInProgress = !timerInProgress;
    }

    /**
     * Disables the button that are passed in as the variable.
     * @param buttonID the ID of the button to disable
     */
    private void disableButton(int buttonID){
        this.findViewById(buttonID).setEnabled(false);
        switch (buttonID) {
            case R.id.start_stop_button:
            case R.id.reset_button:
                this.findViewById(buttonID).setBackground(getDrawable(R.drawable.roundbottondisabled));
                break;
            default:
                this.findViewById(buttonID).setAlpha(0.5f);
                break;
        }
    }

    /**
     * Enables button and updates any related settings to button
     * @param buttonID the ID of the button to enable
     */
    private void enableButton(int buttonID){
        this.findViewById(buttonID).setEnabled(true);
        switch (buttonID){
            case R.id.start_stop_button:
            case R.id.reset_button:
                this.findViewById(buttonID).setBackground(getDrawable(R.drawable.roundbutton));
                break;
            default:
                this.findViewById(buttonID).setAlpha(1.0f);
                break;
        }
    }

    /**
     * Starts the media player for the alarm
     */
    private void startAlarm(){
        eggWatchHandler.removeCallbacksAndMessages(null);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator == null){

        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{500, 200, 500, 200, 500, 200, 500, 200, 500},new int[]{255, 0, 255, 0, 255, 0, 255, 0, 255} ,-1));
        } else {
            vibrator.vibrate(2000);
        }
        mediaPlayer.start();
    }

    /**
     * Updates the text view for the view on the with the amount of seconds remaining
     */
    private void updateTextInTimer(){
        ((TextView)this.findViewById(R.id.Current_Timer_TextView)).setText(String.format(getString(R.string.egg_watch_time_format), calculateMinutesFromSeconds(timer),getSecondsRemainderInFromSeconds(timer)));
    }

    /**
     * Helper method for getting the remaining minutes without the floating number
     * @param seconds time in seconds to calculate
     * @return a whole number for how many minutes its resulted in.
     */
    private int calculateMinutesFromSeconds(int seconds){
        return (int)Math.floor((seconds / 60));
    }

    /**
     * Gets the second remainder from using modulus and returns the value.
     * @param seconds time in seconds to find the remainder of
     * @return amount of seconds left.
     */
    private int getSecondsRemainderInFromSeconds(int seconds){
        return (int)seconds % 60;
    }

    /**
     * sets the value for the Soft boiled eggs.
     * @param view the calling object.
     */
    public void onButtonSoftBoiledClicked(View view){
        int[] enablebuttons = new int[]{R.id.hard_boiled_button,R.id.smiling_button,R.id.start_stop_button};
        ((TextView) this.findViewById(R.id.current_status_textview)).setText(R.string.ready_timer);
        enableAndDisableButtons(view.getId(),enablebuttons);
        setTimers(60 * 4);
        updateTextInTimer();
    }

    /**
     * sets the value for the Hard boiled eggs.
     * @param view the calling object.
     */
    public void onButtonHardBoiledClicked(View view){
        int[] enablebuttons = new int[]{R.id.smiling_button,R.id.soft_boiled_button,R.id.start_stop_button};
        ((TextView) this.findViewById(R.id.current_status_textview)).setText(R.string.ready_timer);

        setTimers(60 * 8);
        updateTextInTimer();

        enableAndDisableButtons(view.getId(),enablebuttons);
    }

    /**
     * sets the value for the Smiling eggs.
     * @param view the calling object.
     */
    public void onButtonSmilingClicked(View view){
        int[] enablebuttons = new int[]{R.id.hard_boiled_button,R.id.soft_boiled_button,R.id.start_stop_button};
        ((TextView) this.findViewById(R.id.current_status_textview)).setText(R.string.ready_timer);
        enableAndDisableButtons(view.getId(),enablebuttons);
        setTimers(60 * 6);
        updateTextInTimer();
    }

    /**
     * Sets the timer variables one for last set and one for the current timer.
     * @param timerToSet the time in seconds to set.
     */
    private void setTimers(int timerToSet){
        lastTimerSet = timerToSet;
        timer = timerToSet;
    }

    /**
     * Reset button which resets the current selected time for starting.
     * @param view the calling object
     */
    public void resetButton(View view){
        timer = lastTimerSet;
        updateTextInTimer();
        disableButton(view.getId());
    }
}