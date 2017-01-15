package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class RoundSettingsActivity extends AppCompatActivity {
    public final static String EXTRA_DURATION_VALUE = "com.rockyourglock.android.shootingtimer.DURATION";
    public final static String EXTRA_STARTIN_VALUE = "com.rockyourglock.android.shootingtimer.STARTIN";
    public final static String EXTRA_TIME_BETWEEN_VALUE = "com.rockyourglock.android.shootingtimer.TIME_BETWEEN";

    private SeekBar roundDuratoin;
    private TextView durationValueTextView;

    private SeekBar startIn;
    private TextView startInTextView;

    private SeekBar timeBetweenItems;
    private TextView timeBetweenItemsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_settings);
        initializeVariables();
        int seconds = roundDuratoin.getProgress() * 15 + 15;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        // Initialize the durationValue with '15 sec'.
        durationValueTextView.setText("min " + String.format("%02d", minutes)
                + " sec " + String.format("%02d", seconds));
        startInTextView.setText("1 sec");
        timeBetweenItemsTextView.setText(".4 sec");

        roundDuratoin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                Toast.makeText(getApplicationContext(), "Changing seekbars progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seconds = progress * 15 + 15;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                durationValueTextView.setText("min " + String.format("%02d", minutes)
                        + " sec " + Integer.toString(seconds) );
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

        startIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                Toast.makeText(getApplicationContext(), "Changing seekbars progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seconds = progress + 1;
                startInTextView.setText("" + String.format("%02d", seconds) + " sec");
            }
        });

        timeBetweenItems.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double time  = progress * .1 + .4;
                timeBetweenItemsTextView.setText("" + String.format("%.1f", time) + " sec");
            }
        });
    }

    public void toRound(View view) {
        String secondsString = String.valueOf(roundDuratoin.getProgress() * 15 + 15);
        String startInExtra = String.valueOf(startIn.getProgress() + 1);
        String timeBetweenExtra = String.valueOf(timeBetweenItems.getProgress() * .1 + .4);

        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra(EXTRA_DURATION_VALUE, secondsString);
        intent.putExtra(EXTRA_STARTIN_VALUE, startInExtra);
        intent.putExtra(EXTRA_TIME_BETWEEN_VALUE, timeBetweenExtra);
        startActivity(intent);
    }

    private void initializeVariables() {
        roundDuratoin = (SeekBar) findViewById(R.id.sb_round_duration);
        durationValueTextView = (TextView) findViewById(R.id.tv_duration_value);

        startIn = (SeekBar) findViewById(R.id.sb_start_in);
        startInTextView = (TextView) findViewById(R.id.tv_start_in_value);

        timeBetweenItems = (SeekBar) findViewById(R.id.sb_time_to_value);
        timeBetweenItemsTextView = (TextView) findViewById(R.id.tv_time_to_item_value);
    }
}
