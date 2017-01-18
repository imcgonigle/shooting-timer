package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RoundActivity extends AppCompatActivity {
    final String[] TARGETS = {"Red", "Orange", "Yellow", "Blue", "Brown",
                        "Grey", "Green", "Black", "Purple", "Triangle",
                        "Square", "Star", "Arrow", "Diamond", "Oval",
                        "Circle", "Rectangle", "Heart"};

    List<String> results = new ArrayList<>();

    private Button startButton;
    private Button pauseButton;
    private Button resumeButton;

    private TextView timerValue;
    private TextView targetValue;

    private CountDownTimer countDownTimer;
    private CountDownTimer startTimer;

    private TextToSpeech textToSpeech;

    private int millisecondsLeft;
    private int startInSeconds;
    private int timeBetweenItems;

    private int roundDurationInMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        initializeVariables();
        setTitle("Round 1");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                settings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void settings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startRound(View view) {
        int startLength = startInSeconds * 1000;

        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        timerValue.setVisibility(View.VISIBLE);
        timerValue.setText(String.format(Locale.US, "%01d", startInSeconds + 1));
        startTimer = new CountDownTimer(startLength, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsUntilStart = (int) millisUntilFinished / 1000;
                timerValue.setText(String.format(Locale.US, "%01d", secondsUntilStart + 1));
            }

            @Override
            public void onFinish() {
                int timeLength = roundDurationInMilliseconds;
                targetValue.setVisibility(View.VISIBLE);
                setUpTimer(timeLength);
                countDownTimer.start();
            }
        };
        startTimer.start();
    }

    public void pauseRound(View view) {
        pauseButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.VISIBLE);
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }else if(startTimer != null) {
            startTimer.cancel();
        }
    }

    public void resumeRound(View view) {
        resumeButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        setUpTimer(millisecondsLeft);
        countDownTimer.start();
    }

    public void onDestroy(){
        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void setUpTimer(int timerLength) {
        countDownTimer = new CountDownTimer(timerLength, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisecondsLeft = (int) (millisUntilFinished);
                int seconds = (millisecondsLeft / 1000) % 60;
                int minutes = millisecondsLeft / 60000;

                if(millisecondsLeft % timeBetweenItems < 100){
                    int rnd = new Random().nextInt(TARGETS.length);
                    String target = TARGETS[rnd];
                    targetValue.setText(target);
                    results.add(target);
                    speak(target);
                }
                timerValue.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                pauseButton.setVisibility(View.INVISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                targetValue.setVisibility(View.INVISIBLE);
                timerValue.setText(R.string.finished);
                targetValue.setText(R.string.ready);
                startButton.setText(R.string.new_round);
                startButton.setVisibility(View.VISIBLE);
            }
        };
    }

    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    private void initializeVariables() {
        getAndSetPreferences();

        startButton = (Button) findViewById(R.id.btn_start_round);
        pauseButton = (Button) findViewById(R.id.btn_stop);
        resumeButton = (Button) findViewById(R.id.btn_resume);
        timerValue = (TextView) findViewById(R.id.tv_timer);
        targetValue = (TextView) findViewById(R.id.tv_target);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            float speechRate = (float) ((timeBetweenItems > 800) ? 1.0 : 2.0);
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(speechRate);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAndSetPreferences();
        targetValue.setText(R.string.ready);
        targetValue.setVisibility(View.INVISIBLE);
        timerValue.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        millisecondsLeft = roundDurationInMilliseconds;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseRound(findViewById(android.R.id.content));
    }

    private void getAndSetPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        roundDurationInMilliseconds = sharedPreferences.getInt(SettingsActivity.KEY_ROUND_DURATION, 15) * 1000;
        startInSeconds = sharedPreferences.getInt(SettingsActivity.KEY_START_TIME, 10);
        timeBetweenItems = (int) (sharedPreferences.getInt(SettingsActivity.KEY_TIME_BETWEEN, 6) * 100);
    }
}
