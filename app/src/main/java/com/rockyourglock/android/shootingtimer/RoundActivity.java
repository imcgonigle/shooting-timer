package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class RoundActivity extends AppCompatActivity {

    String[] sounds = {"Red", "Orange", "Yellow", "Blue", "Brown",
                        "Grey", "Green", "Black", "Purple", "Triangle",
                        "Square", "Star", "Arrow", "Diamond", "Oval",
                        "Circle", "Rectangle", "Heart"};

    Button startButton;
    Button pauseButton;
    Button resumeButton;

    TextView timerValue;
    TextView targetValue;

    CountDownTimer countDownTimer;

    int timeInSeconds;
    int millisecondsLeft;
    int startInSeconds;
    int timeBetweenItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        Intent intent = getIntent();
        timeInSeconds  = Integer.parseInt(intent
                .getStringExtra(RoundSettingsActivity.EXTRA_DURATION_VALUE));
        startInSeconds = Integer.parseInt(intent
                .getStringExtra(RoundSettingsActivity.EXTRA_STARTIN_VALUE));
        timeBetweenItems = (int) (Double.parseDouble(intent
                        .getStringExtra(RoundSettingsActivity.EXTRA_TIME_BETWEEN_VALUE)) * 1000);

        startButton = (Button) findViewById(R.id.btn_start_round);
        pauseButton = (Button) findViewById(R.id.btn_stop);
        resumeButton = (Button) findViewById(R.id.btn_resume);
        timerValue = (TextView) findViewById(R.id.tv_timer);
        targetValue = (TextView) findViewById(R.id.tv_target);

        timerValue.setVisibility(View.VISIBLE);
        timerValue.setText(Integer.toString(timeBetweenItems));
    }

    public void backToSettings(View view){
        Intent intent = new Intent(this, RoundSettingsActivity.class);
        startActivity(intent);
    }

    public void startRound(View view) {
        int startLength = startInSeconds * 1000;

        CountDownTimer startTimer;

        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        timerValue.setVisibility(View.VISIBLE);
        timerValue.setText(Integer.toString(startInSeconds + 1));
        startTimer = new CountDownTimer(startLength, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsUntilStart = (int) millisUntilFinished / 1000;
                timerValue.setText(Integer.toString(secondsUntilStart + 1));
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

    public void setUpTimer(int timerLength) {
        countDownTimer = new CountDownTimer(timerLength, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisecondsLeft = (int) (millisUntilFinished);
                if(millisecondsLeft % timeBetweenItems <= 10){
                    int rnd = new Random().nextInt(sounds.length);
                    String targetItem = sounds[rnd];
                    targetValue.setText(targetItem);
                }
                int seconds = millisecondsLeft / 1000;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int milliseconds = millisecondsLeft % 1000;

                timerValue.setText("" + minutes + ":"
                        + String.format("%02d", seconds) + ":"
                        + String.format("%03d", milliseconds));
            }

            @Override
            public void onFinish() {
                pauseButton.setVisibility(View.INVISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                targetValue.setVisibility(View.INVISIBLE);
                timerValue.setText("Finished");
                targetValue.setText("Ready?");
                startButton.setText("New Round");
                startButton.setVisibility(View.VISIBLE);
            }
        };
    }
}
