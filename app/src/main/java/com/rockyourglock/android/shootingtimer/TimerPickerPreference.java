package com.rockyourglock.android.shootingtimer;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Ian McGonigle on 1/15/2017.
 */

public class TimerPickerPreference extends DialogPreference {

    // enable or disable the 'circular behavior'
    private static final boolean WRAP_SELECTOR_WHEEL = false;
    private static final int DEFAULT_VALUE = 60;

    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private final int DEFAULT_BACKGROUND = ContextCompat.getColor(getContext(), R.color.colorBackground);
    private final int DEFAULT_TEXT_COLOR = ContextCompat.getColor(getContext(), R.color.colorLighter);

    private boolean bMinutes;
    private boolean bSeconds;
    private int minutesMax;
    private float minutesMin;
    private int secondsMax;
    private float secondsMin;
    private int background;
    private int textColor;
    private float incrementsSeconds;
    private float incrementsMinutes;

    int currentValue;

    public TimerPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimerPreferenceDialog,
                0, 0);
        try {
            background = attributes.getColor(
                    R.styleable.TimerPreferenceDialog_backgroundColor, DEFAULT_BACKGROUND);
            textColor = attributes.getColor(
                    R.styleable.TimerPreferenceDialog_textColor, DEFAULT_TEXT_COLOR);
            bMinutes = attributes.getBoolean(R.styleable.TimerPreferenceDialog_minutes, true);
            bSeconds = attributes.getBoolean(R.styleable.TimerPreferenceDialog_seconds, true);
            minutesMax = (bMinutes) ? attributes.getInt(R.styleable.TimerPreferenceDialog_minutesMax, 99) : 0;
            minutesMin = attributes.getFloat(R.styleable.TimerPreferenceDialog_minutesMin, 0);
            minutesMin = (minutesMin >= 0) ? minutesMin : 0;
            secondsMax = (bSeconds) ? attributes.getInt(R.styleable.TimerPreferenceDialog_secondsMax, 59) : 0;
            secondsMin = attributes.getFloat(R.styleable.TimerPreferenceDialog_secondsMin, 0);
            secondsMin = (secondsMin >= 0) ? secondsMin : 0;
            incrementsMinutes = attributes.getFloat(R.styleable.TimerPreferenceDialog_incrementsMinutes, 1);
            incrementsSeconds = attributes.getFloat(R.styleable.TimerPreferenceDialog_incrementsSeconds, 1);

        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected View onCreateDialogView() {

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        LinearLayout pickers = new LinearLayout(getContext());
        LinearLayout secondPicker;
        LinearLayout minutePicker;

        if(bMinutes) {
            minutesPicker = new NumberPicker(getContext());
            TextView minutesLabel = new TextView(getContext());
            minutesLabel.setText("Min");
            minutesLabel.setTextSize(20);
            minutesLabel.setTextColor(textColor);
            minutesLabel.setGravity(Gravity.CENTER_HORIZONTAL);

            minutePicker = new LinearLayout(getContext());
            minutePicker.setOrientation(LinearLayout.VERTICAL);
            minutePicker.addView(minutesLabel);
            minutePicker.addView(minutesPicker);
            pickers.addView(minutePicker);

            if(bSeconds){
                minutePicker.setPadding(0,0,40,0);
            }
        }

        if(bSeconds) {
            secondsPicker = new NumberPicker(getContext());
            TextView secondsLabel = new TextView(getContext());
            secondsLabel.setText("Sec");
            secondsLabel.setTextSize(20);
            secondsLabel.setTextColor(textColor);
            secondsLabel.setGravity(Gravity.CENTER_HORIZONTAL);

            secondPicker = new LinearLayout(getContext());
            secondPicker.setOrientation(LinearLayout.VERTICAL);
            secondPicker.addView(secondsLabel);
            secondPicker.addView(secondsPicker);
            pickers.addView(secondPicker);
            if(bMinutes) {
                secondPicker.setPadding(40, 0, 0, 0);
            }
        }
        pickers.setPadding(0,100,0,0);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.setBackgroundColor(background);
        dialogView.addView(pickers, layoutParams);

        return dialogView;
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        if (bSeconds) {
            int secondsMinVal = (int) (secondsMin / incrementsSeconds);
            int secondsValue = (int) (secondsMax / incrementsSeconds - (bMinutes ? currentValue % 60 : currentValue));
            int secondsArrayLength = (int) (secondsMax / incrementsSeconds + 1) - secondsMinVal;
            float n = secondsMax;
            String[] secondsArray = new String[secondsArrayLength];

            for(int i= 0; i < secondsArrayLength; i++){
                secondsArray[i] = String.format(Locale.US, "%.1f", n);
                n -= incrementsSeconds;
            }

            secondsPicker.setMinValue(0);
            secondsPicker.setMaxValue(secondsArray.length-1);
            secondsPicker.setValue(secondsValue);
            secondsPicker.setDisplayedValues(secondsArray);
            secondsPicker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        }

        if (bMinutes) {
            int minutesMinVal = (int) (minutesMin / incrementsMinutes);
            int minutesValue = (int) ((minutesMax - currentValue / 60) + minutesMin);
            int minutesArrayLength = (int) (minutesMax / incrementsMinutes + 1) - minutesMinVal;
            float n = minutesMax;
            String[] minutesArray = new String[minutesArrayLength];

            for(int i=0; i<minutesArrayLength; i++) {
                minutesArray[i] = String.format(Locale.US, "%.1f", n);
                n -= incrementsMinutes;
            }

            minutesPicker.setMinValue(0);
            minutesPicker.setMaxValue(minutesArray.length-1);
            minutesPicker.setValue(minutesValue);
            minutesPicker.setDisplayedValues(minutesArray);
            minutesPicker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        }

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int maxValue = (int) (60 * (minutesMax / incrementsMinutes) + (secondsMax /incrementsSeconds));
            int newMinutesValue = (((bMinutes) ? minutesPicker.getValue() : 0));
            int newSecondsValue = (((bSeconds) ? secondsPicker.getValue() : 0));
            int newValue = maxValue - (newMinutesValue * 60 + newSecondsValue);

            persistInt(newValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue) {
            currentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            currentValue = (Integer) defaultValue;
            persistFloat(currentValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

}
