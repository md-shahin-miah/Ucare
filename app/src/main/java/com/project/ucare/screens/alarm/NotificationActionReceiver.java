package com.project.ucare.screens.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.project.ucare.common.Utils;
import com.project.ucare.models.Schedule;

public class NotificationActionReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String type = intent.getStringExtra("type");


        Schedule schedule = new Gson().fromJson(intent.getStringExtra("schedule"), Schedule.class);

        if (schedule != null) {
            Utils.cancelNotification(context, schedule.getAlarm().getId());
            Utils.stopRing();

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.cancel();


        }


        if (type.equalsIgnoreCase("snooze")){

        }

        Log.d("qqq", "onReceive: "+schedule.getMedicineName());

        Log.d("qqq", "onReceive: "+type);


    }
}
