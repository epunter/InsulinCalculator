package com.ethanpunter.insulincalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView greeting;
    private TextView dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.greeting = findViewById(R.id.greeting);
        this.dateTime = findViewById(R.id.dateTime);

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour < 12) {
            greeting.setText(R.string.good_morning);
        } else if (hour >= 12 && hour < 17) {
            greeting.setText(R.string.good_afternoon);
        } else if (hour >= 17 && hour < 20) {
            greeting.setText(R.string.good_evening);
        } else if (hour >= 20 && hour < 24) {
            greeting.setText(R.string.good_night);
        }
    }

    public void openCarbCalculator(View view) {
        Intent carbCalcIntent = new Intent(this, CarbCalculator.class);
        startActivity(carbCalcIntent);
    }

    public void openInsulinCorrectionDoseCalculator(View view) {
        Intent correctionDoseCalc = new Intent(this, CorrectionDoseCalculator.class);
        startActivity(correctionDoseCalc);
    }
}