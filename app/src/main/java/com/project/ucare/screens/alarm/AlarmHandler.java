package com.project.ucare.screens.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.ucare.models.Schedule;

import java.util.Calendar;


public class AlarmHandler {


    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    Context context;

    public AlarmHandler(Context context, Schedule schedule) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MyBroadCastReceiver.class);
        //  alarmIntent.setAction(Intent.ACTION_BOOT_COMPLETED);
        alarmIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        alarmIntent.putExtra("schedule", new Gson().toJson(schedule));
        pendingIntent = PendingIntent.getBroadcast(context, schedule.getAlarm().getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void startAlarm(int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        if (Calendar.getInstance().after(calendar)) {
            // Move to tomorrow
            calendar.add(Calendar.DATE, 1);
        }

        Log.d("qqq", "startAlarm: " + calendar.getTimeInMillis() + " " + hour + " " + min + calendar.getTime());

        long time = calendar.getTimeInMillis() - 20000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, time, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, time, pendingIntent);
        }
    }

//    public void startNextAlarm(int hour, int min) {
//        Calendar calendar = Calendar.getInstance();
//        // calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, min);
//        calendar.add(Calendar.DATE, 1);
//
//        Log.d("qqq", "startAlarm: " + calendar.getTimeInMillis() + " " + hour + " " + min);
//
//        long time = calendar.getTimeInMillis() - 36000;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        }
//    }


    public void cancelAlarm() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }


}
