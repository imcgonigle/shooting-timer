package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RoundSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_settings);
    }

    public void toRound(View view) {
        Intent intent = new Intent(this, RoundActivity.class);
        startActivity(intent);
    }
}
