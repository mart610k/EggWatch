package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableAndDisableButtons(int toDisable, int[] toEnable){
        for (int i = 0; i < toEnable.length; i++){
            this.findViewById((toEnable[i])).setEnabled(true);
        }

        this.findViewById(toDisable).setEnabled(false);
    }

    private void updateTextInTimer(){
        ((TextView)this.findViewById(R.id.Current_Timer_TextView)).setText("Hello world");
    }

    public void onButtonSoftBoiledClicked(View view){
        int[] enablebuttons = new int[]{R.id.hard_boiled_button,R.id.smiling_button,R.id.start_stop_button};
        enableAndDisableButtons(view.getId(),enablebuttons);
        updateTextInTimer();
    }

    public void onButtonHardBoiledClicked(View view){
        int[] enablebuttons = new int[]{R.id.smiling_button,R.id.soft_boiled_button,R.id.start_stop_button};
        enableAndDisableButtons(view.getId(),enablebuttons);
    }

    public void onButtonSmilingClicked(View view){
        int[] enablebuttons = new int[]{R.id.hard_boiled_button,R.id.soft_boiled_button,R.id.start_stop_button};
        enableAndDisableButtons(view.getId(),enablebuttons);
    }
}