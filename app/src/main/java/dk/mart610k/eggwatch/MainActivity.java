package dk.mart610k.eggwatch;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.mart610k.eggwatch.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import android.widget.Button;
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
     * Helper method for enable and disable different buttons.
     * @param toDisable the button to disable
     * @param toEnable the buttons to enable
     */
    private void enableAndDisableButtons(int toDisable, int[] toEnable){
        for (int i = 0; i < toEnable.length; i++){
            this.findViewById((toEnable[i])).setEnabled(true);
        }

        this.findViewById(toDisable).setEnabled(false);
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
        this.findViewById(R.id.reset_button).setEnabled(timerInProgress);
        timerInProgress = !timerInProgress;
    }

    /**
     * Starts the media player for the alarm
     */
    private void startAlarm(){
        eggWatchHandler.removeCallbacksAndMessages(null);
        mediaPlayer.start();
    }

    /**
     * Updates the text view for the view on the with the amount of seconds remaining
     */
    private void updateTextInTimer(){

        ((TextView)this.findViewById(R.id.Current_Timer_TextView)).setText(String.format("%02d:%02d", calculateMinutesFromSeconds(timer),getSecondsRemainderInFromSeconds(timer)));
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
        view.setEnabled(false);
    }
}