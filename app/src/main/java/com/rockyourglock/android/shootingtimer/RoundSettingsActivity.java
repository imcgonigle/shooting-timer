package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class RoundSettingsActivity extends AppCompatActivity {
    public final static String EXTRA_DURATION = "com.rockyourglock.android.shootingtimer.DURATION";
    public final static String EXTRA_STARTIN = "com.rockyourglock.android.shootingtimer.STARTIN";
    public final static String EXTRA_TIME_BETWEEN = "com.rockyourglock.android.shootingtimer.TIME_BETWEEN";

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
        durationValueTextView.setText(String.format(Locale.US, "%02d min %02d sec", minutes, seconds));
        startInTextView.setText("1 sec");
        timeBetweenItemsTextView.setText(".5 sec");

        roundDuratoin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                int seconds = progress * 15 + 15;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                durationValueTextView.setText(String.format(Locale.US, "%02d min %02d sec", minutes, seconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Changing Round Length", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seconds = progress * 15 + 15;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                durationValueTextView.setText(String.format(Locale.US, "%02d min %02d sec", minutes, seconds));
            }
        });

        startIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                int seconds = progress + 1;
                startInTextView.setText(String.format(Locale.US, "%02d sec", seconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Changing start in time", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seconds = progress + 1;
                startInTextView.setText(String.format(Locale.US, "%02d sec", seconds));
            }
        });

        timeBetweenItems.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                double time = progress * .1 + .5;
                timeBetweenItemsTextView.setText(String.format(Locale.US, "%.1f sec", time));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Changing Time Between Items", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double time  = progress * .1 + .5;
                timeBetweenItemsTextView.setText(String.format(Locale.US, "%.1f sec", time));
            }
        });
    }

    public void toRound(View view) {
        int durationExtra = roundDuratoin.getProgress() * 15 + 15;
        int startInExtra = startIn.getProgress() + 1;
        int timeBetweenExtra = (timeBetweenItems.getProgress() + 5) * 100;

        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra(EXTRA_DURATION, durationExtra);
        intent.putExtra(EXTRA_STARTIN, startInExtra);
        intent.putExtra(EXTRA_TIME_BETWEEN, timeBetweenExtra);
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
