package com.ethanpunter.insulincalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ethanpunter.insulincalculator.RatioReader.readRatioFile;

public class CorrectionDoseCalculator extends AppCompatActivity {

    private TextClock dateTime;
    private TextView currentConversion;
    private EditText desiredBloodGlucose;
    private EditText bloodGlucose;
    private TextView dosage;

    private int currentHour;
    private List<TimedRatio> ratios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction_dose_calculator);

        this.ratios = readRatioFile(getAssets(), "correctionDoseRatio.csv");
        initialiseViews();

        // TextWatcher for observing edit text inputs
        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                updateDosage();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        this.bloodGlucose.addTextChangedListener(tw);
        this.desiredBloodGlucose.addTextChangedListener(tw);

        // If time changes, need to update the current hour and the texts that depends on the time
        this.dateTime.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCurrentTime();
                updateTimeSensitiveViews();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initialiseViews() {
        this.dateTime = findViewById(R.id.correctionDateTime);
        this.currentConversion = findViewById(R.id.currentCorrectionConversion);
        this.desiredBloodGlucose = findViewById(R.id.txtDesiredBloodGlucose);
        this.bloodGlucose = findViewById(R.id.txtBloodGlucose);
        this.dosage = findViewById(R.id.correctionInsulinDose);
        updateCurrentTime();
        updateTimeSensitiveViews();
    }

    private void updateTimeSensitiveViews() {
        currentConversion.setText(String.format(Locale.getDefault(),
                getString(R.string.current_blood_glucose_conversion), getCurrentRatio(currentHour)));
        updateDosage();
    }

    private void updateCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        this.currentHour = cal.get(Calendar.HOUR_OF_DAY);
    }

    private void updateDosage() {
        float units = 0.0f;
        if (!bloodGlucose.getText().toString().isEmpty()
                && !desiredBloodGlucose.getText().toString().isEmpty()) {
            units = getCorrectionConversion(currentHour,
                    Float.parseFloat(bloodGlucose.getText().toString()));
        }
        dosage.setText(String.format(Locale.getDefault(), getString(R.string.insulin_amount), units));
    }

    private float getCorrectionConversion(int hour, float bloodGlucose) {
        float desiredGlucose = Float.parseFloat(desiredBloodGlucose.getText().toString());
        if (bloodGlucose <= desiredGlucose) {
            return 0;
        } else {
            return (bloodGlucose - desiredGlucose) / getCurrentRatio(hour);
        }
    }

    private float getCurrentRatio(int currentHour) {
        float currentRatio = 0;
        for (TimedRatio timedRatio : ratios) {
            if (currentHour >= timedRatio.getFrom() && currentHour < timedRatio.getTo()) {
                currentRatio = timedRatio.getRatio();
            }
        }
        return currentRatio;
    }

}