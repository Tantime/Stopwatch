package com.example.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    private Button startPause;
    private Button reset;
    private Chronometer timer;
    private boolean toggled;
    private long newBase = 0;
    private long currentTime = 0;
    private String startPauseText;

    public static final String KEY_CHRONOMETER_BASE = "chronometer base";
    public static final String KEY_CHRONOMETER_NEWBASE = "chronometer new base";
    public static final String KEY_CHRONOMETER_IFRUNNING = "is chronometer running";
    public static final String KEY_CHRONOMETER_STARTPAUSEBUTTON = "what start/pause button says";
    public static final String TAG = MainActivity.class.getSimpleName();
    // Look up the Log class for Android
    // List all the levels of logging and when they're used
    // lowest to highest priority list below
    // wtf: What a Terrible Failure
    // v: Verbose (lowest priority)
    // d: Debug
    // i: Info
    // w: Warning
    // e: Error
    // a: Assert (highest priority)

    // launched app --> onCreate, onStart, onResume
    // rotate --> onStop, onDestroy, onCreate, onStart, onResume
    // hit the square button --> onPause, onStop
    // click back on the app from the square button --> onStop, onStart, onResume
    // hit the circle button --> onPause, onStop
    // relaunch the app from the phone navigation (not play button) --> onStop, onStart, onResume
    // hit the back button --> onPause, onStop, onDestroy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
        setListeners();

        // if the savedInstanceState isn't null
            // pull out the value of the base that we saved from the Bundle
            // set the chronometer's base to that value
            // start the chronometer
        if(savedInstanceState != null) {
            currentTime = savedInstanceState.getLong(KEY_CHRONOMETER_BASE);
            newBase = savedInstanceState.getLong(KEY_CHRONOMETER_NEWBASE);
            toggled = savedInstanceState.getBoolean(KEY_CHRONOMETER_IFRUNNING);
            startPauseText = savedInstanceState.getString(KEY_CHRONOMETER_STARTPAUSEBUTTON);

            if(toggled) {
                timer.setBase(currentTime - newBase);

                timer.start();
                startPause.setText(getString(R.string.main_pause));
                toggled = true;
            }
            else {
                timer.setBase(SystemClock.uptimeMillis() - newBase);

                startPause.setText(getString(R.string.main_start));
                toggled = false;
            }
        }

        // next functionality would be to also store in the bundle
        // whether it was running or stopped to decide if you should
        // start it or not in onCreate

    }

    private void wireWidgets() {
        startPause = findViewById(R.id.button_main_startPause);
        reset = findViewById(R.id.button_main_reset);
        timer = findViewById(R.id.chronometer_main_timer);
    }

    // start/stop works & doesn't skip time
    // maintain state through orientation change
        // if it were running, stay running & stay

    private void setListeners() {
        startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!toggled)
                {
                    timer.setBase(currentTime - newBase);

                    timer.start();
                    startPause.setText(getString(R.string.main_pause));
                    startPauseText = startPause.getText().toString(); // added
                    toggled = true;
                }
                else {
                    timer.stop();
                    startPause.setText(getString(R.string.main_start));
                    startPauseText = startPause.getText().toString(); // added
                    toggled = false;

                    currentTime = SystemClock.uptimeMillis(); // added
                    newBase = SystemClock.uptimeMillis() - timer.getBase();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();
                startPause.setText(getString(R.string.main_start));
                toggled = false;

                timer.setBase(SystemClock.elapsedRealtime());
                currentTime = SystemClock.uptimeMillis(); // added
                newBase = SystemClock.elapsedRealtime() - timer.getBase();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    // when activity is finished
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_CHRONOMETER_BASE, currentTime);
        outState.putLong(KEY_CHRONOMETER_NEWBASE, newBase);
        outState.putBoolean(KEY_CHRONOMETER_IFRUNNING, toggled);
        outState.putString(KEY_CHRONOMETER_STARTPAUSEBUTTON, startPauseText);
    }
}