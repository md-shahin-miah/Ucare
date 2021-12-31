package com.project.ucare.screens.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.gson.Gson;
import com.project.ucare.R;
import com.project.ucare.common.Utils;
import com.project.ucare.models.Schedule;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Schedule schedule = new Gson().fromJson(getIntent().getStringExtra("schedule"), Schedule.class);

        if (schedule != null) {
            Utils.cancelNotification(this, schedule.getAlarm().getId());
            Utils.stopRing();

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.cancel();

        }

    }
}