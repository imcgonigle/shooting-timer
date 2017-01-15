package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RoundActivity extends AppCompatActivity {

    Button startButton;
    Button pauseButton;
    Button resumeButton;

    TextView timerValue;

    CountDownTimer countDownTimer;

    final int timeInSeconds = 13;
    int millisecondsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        startButton = (Button) findViewById(R.id.btn_start_round);
        pauseButton = (Button) findViewById(R.id.btn_stop);
        resumeButton = (Button) findViewById(R.id.btn_resume);
        timerValue = (TextView) findViewById(R.id.tv_timer);
    }

    public void backToSettings(View view){
        Intent intent = new Intent(this, RoundSettingsActivity.class);
        startActivity(intent);
    }

    public void startRound(View view) {
        int timeLength = timeInSeconds * 1000;

        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        timerValue.setVisibility(View.VISIBLE);
        setUpTimer(timeLength);
        countDownTimer.start();
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
                timerValue.setText("Finished");
                startButton.setText("New Round");
                startButton.setVisibility(View.VISIBLE);
            }
        };
    }
}
