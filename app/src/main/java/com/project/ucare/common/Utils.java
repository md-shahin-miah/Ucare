package com.project.ucare.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    public static void cancelNotification(Context context, int id) {
        NotificationManager notificacaoManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificacaoManager.cancel(id);
    }

    private static Ringtone ringtone;

    public static void playRing(Context context) {

        //  Uri alarmUri = Uri.parse("content://media/internal/audio/media");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                ringtone.stop();
                t.cancel();
                vibrator.cancel();
            }
        }, 30000);
    }

    public static void stopRing() {
        if (ringtone != null) {
            ringtone.stop();
        }
    }

    public static String getToday() {
        Calendar date = Calendar.getInstance();
        return android.text.format.DateFormat.format("EEE", date).toString();
    }

    public static Date stringToDate(String dtStart) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        try {
            return format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return sdf.format(c.getTime());
    }

    public static Date incrementDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    public static boolean isDateValid(String date) {
        return System.currentTimeMillis() >= stringToDate(date).getTime();
    }


    public static String firebaseUserId() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }


}
