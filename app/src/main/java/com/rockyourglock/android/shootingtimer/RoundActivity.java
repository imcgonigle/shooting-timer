package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private TextToSpeech textToSpeech;

    private int timeInSeconds;
    private int millisecondsLeft;
    private int startInSeconds;
    private int timeBetweenItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        initializeVariables();
    }

    public void backToSettings(View view){
        Intent intent = new Intent(this, RoundSettingsActivity.class);
        startActivity(intent);
    }

    public void startRound(View view) {
        CountDownTimer startTimer;
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
                int timeLength = timeInSeconds * 1000;
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
        countDownTimer.cancel();
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
        Intent intent = getIntent();
        timeInSeconds  = intent.getIntExtra(RoundSettingsActivity.EXTRA_DURATION, 0);
        startInSeconds = intent.getIntExtra(RoundSettingsActivity.EXTRA_STARTIN, 0);
        timeBetweenItems = intent.getIntExtra(RoundSettingsActivity.EXTRA_TIME_BETWEEN, 0);

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
}
