package com.project.ucare.screens.alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.project.ucare.R;
import com.project.ucare.common.Utils;
import com.project.ucare.models.Schedule;

import java.util.Timer;
import java.util.TimerTask;

import static com.project.ucare.common.Utils.playRing;

public class MyBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "MyBroadCastReceiver";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        Schedule schedule = new Gson().fromJson(intent.getStringExtra("schedule"), Schedule.class);

        if (schedule != null) {

            NotificationHelper notificationHelper = new NotificationHelper(context, schedule);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            notificationHelper.getManager().notify(schedule.getAlarm().getId(), nb.build());
            Utils.playRing(context);


          //  playAlarm(context, schedule);
            Log.d(TAG, "onReceive: ");
        }

        Log.d(TAG, "onReceive: called");

    }


    void playAlarm(Context context, Schedule schedule) {
        String endDate = Utils.dateToString(Utils.incrementDate(Utils.stringToDate(schedule.getStartDate()), Integer.parseInt(schedule.getDuration())));

        if (Utils.isDateValid(endDate)) {
            // TODO: 7/25/202 cancel
            AlarmHandler handler = new AlarmHandler(context, schedule);
            handler.cancelAlarm();

            Log.d(TAG, "playAlarm:  cancelAlarm");

        } else {
            if (Utils.isDateValid(schedule.getStartDate())) {

                Log.d(TAG, "playAlarm: valid");

                boolean flag = false;
                for (String day : schedule.getAlarm().getDays()) {
                    if (Utils.getToday().equalsIgnoreCase(day)) {
                        flag = true;
                    }
                }
                Log.d(TAG, "playAlarm: "+flag);
                if (flag) {

                    NotificationHelper notificationHelper = new NotificationHelper(context, schedule);
                    NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                    notificationHelper.getManager().notify(schedule.getAlarm().getId(), nb.build());
                    Utils.playRing(context);
                }

            }

            Log.d(TAG, "playAlarm: not valid");

        }


    }


}

