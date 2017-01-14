package com.rockyourglock.android.shootingtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
    }

    public void backToSettings(View view){
        Intent intent = new Intent(this, RoundSettingsActivity.class);
        startActivity(intent);
    }
}
