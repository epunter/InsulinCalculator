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

public class CarbCalculator extends AppCompatActivity {

    private TextClock dateTime;
    private TextView currentConversion;
    private EditText carbAmount;
    private TextView dosage;

    private int currentHour;
    private List<TimedRatio> ratios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carb_calculator);

        this.ratios = readRatioFile(getAssets(), "carbRatio.csv");

        initialiseViews();

        // Listener to calculate dosage based on carbs
        carbAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                updateDosage();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // If time changes, need to update the current hour and the texts that depends on the time
        dateTime.addTextChangedListener(new TextWatcher() {
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
        this.dateTime = findViewById(R.id.carbDateTime);
        this.currentConversion = findViewById(R.id.currentCarbConversion);
        this.carbAmount = findViewById(R.id.txtCarbAmount);
        this.dosage = findViewById(R.id.insulinDose);
        updateCurrentTime();
        updateTimeSensitiveViews();
    }

    private void updateTimeSensitiveViews() {
        currentConversion.setText(String.format(Locale.getDefault(),
                getString(R.string.current_carb_conversion), getCurrentRatio(currentHour)));
        updateDosage();
    }

    private void updateCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        this.currentHour = cal.get(Calendar.HOUR_OF_DAY);
    }

    private void updateDosage() {
        if (!carbAmount.getText().toString().isEmpty()) {
            dosage.setText(String.format(Locale.getDefault(), getString(R.string.insulin_amount),
                    getCarbConversion(currentHour, Integer.parseInt(carbAmount.getText().toString()))));
        } else {
            dosage.setText(R.string.zero_units);
        }
    }

    private float getCarbConversion(int hour, int carbs) {
        return carbs / getCurrentRatio(hour);
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