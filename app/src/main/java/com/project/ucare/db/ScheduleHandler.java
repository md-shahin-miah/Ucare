package com.project.ucare.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.ucare.models.Alarm;
import com.project.ucare.models.Profile;
import com.project.ucare.models.Schedule;

import java.util.ArrayList;
import java.util.List;

import static com.project.ucare.db.SqlDbHelper.BIRTH_DATE;
import static com.project.ucare.db.SqlDbHelper.KEY_ID;
import static com.project.ucare.db.SqlDbHelper.PARENT_ID;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_ALARM;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_DURATION;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_INTAKE;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_IS_ENABLE;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_KEY_ID;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_MEDICINE_NAME;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_MEDICINE_TYPE;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_MEDICINE_UNIT;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_START_DATE;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_UPDATED_TIME;
import static com.project.ucare.db.SqlDbHelper.SCHEDULE_USER_ID;
import static com.project.ucare.db.SqlDbHelper.TABLE_PROFILE;
import static com.project.ucare.db.SqlDbHelper.TABLE_SCHEDULE;
import static com.project.ucare.db.SqlDbHelper.UPDATED_TIME;
import static com.project.ucare.db.SqlDbHelper.USER_GENDER;
import static com.project.ucare.db.SqlDbHelper.USER_NAME;

public class ScheduleHandler {

    Context context;
    SqlHandler sqlHandler;

    public ScheduleHandler(Context context) {
        this.context = context;
        sqlHandler = new SqlHandler(context);
    }

    public long addSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(SCHEDULE_KEY_ID, schedule.getId());
        values.put(SCHEDULE_USER_ID, schedule.getUserId());
        values.put(SCHEDULE_MEDICINE_NAME, schedule.getMedicineName());
        values.put(SCHEDULE_MEDICINE_TYPE, schedule.getMedicineType());
        values.put(SCHEDULE_MEDICINE_UNIT, schedule.getMedicineUnit());
        values.put(SCHEDULE_START_DATE, schedule.getStartDate());
        values.put(SCHEDULE_DURATION, schedule.getDuration());
        values.put(SCHEDULE_INTAKE, schedule.getIntake());
        values.put(SCHEDULE_IS_ENABLE, schedule.isEnable());
        values.put(SCHEDULE_UPDATED_TIME, schedule.getUpdatedTime());
        values.put(SCHEDULE_ALARM, new Gson().toJson(schedule.getAlarm()));

        return sqlHandler.upsert(TABLE_SCHEDULE, values);
    }

    String userId;

    public List<Schedule> getSchedules(String id) {
        userId = id;

        List<Schedule> scheduleList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SCHEDULE + " where " + SCHEDULE_USER_ID + "= '"
                + userId + "' ORDER BY " + SCHEDULE_USER_ID + " DESC";

        Cursor cursor = sqlHandler.selectQuery(query);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Schedule schedule = new Schedule();
                    schedule.setId(cursor.getString(cursor.getColumnIndex(SCHEDULE_KEY_ID)));
                    schedule.setUserId(cursor.getString(cursor.getColumnIndex(SCHEDULE_USER_ID)));
                    schedule.setMedicineName(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_NAME)));
                    schedule.setMedicineType(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_TYPE)));
                    schedule.setMedicineUnit(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_UNIT)));
                    schedule.setStartDate(cursor.getString(cursor.getColumnIndex(SCHEDULE_START_DATE)));
                    schedule.setDuration(cursor.getString(cursor.getColumnIndex(SCHEDULE_DURATION)));
                    schedule.setIntake(cursor.getString(cursor.getColumnIndex(SCHEDULE_INTAKE)));
                    schedule.setEnable(cursor.getString(cursor.getColumnIndex(SCHEDULE_IS_ENABLE)));
                    schedule.setUpdatedTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(SCHEDULE_UPDATED_TIME))));

                    Alarm alarm = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SCHEDULE_ALARM)), Alarm.class);
                    schedule.setAlarm(alarm);
                    scheduleList.add(schedule);

                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();

        return scheduleList;

    }

    public List<Schedule> getAllSchedules() {

        List<Schedule> scheduleList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SCHEDULE;

        Cursor cursor = sqlHandler.selectQuery(query);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Schedule schedule = new Schedule();

                    schedule.setId(cursor.getString(cursor.getColumnIndex(SCHEDULE_KEY_ID)));
                    schedule.setUserId(cursor.getString(cursor.getColumnIndex(SCHEDULE_USER_ID)));
                    schedule.setMedicineName(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_NAME)));
                    schedule.setMedicineType(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_TYPE)));
                    schedule.setMedicineUnit(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_UNIT)));
                    schedule.setStartDate(cursor.getString(cursor.getColumnIndex(SCHEDULE_START_DATE)));
                    schedule.setDuration(cursor.getString(cursor.getColumnIndex(SCHEDULE_DURATION)));
                    schedule.setIntake(cursor.getString(cursor.getColumnIndex(SCHEDULE_INTAKE)));
                    schedule.setEnable(cursor.getString(cursor.getColumnIndex(SCHEDULE_IS_ENABLE)));
                    schedule.setUpdatedTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(SCHEDULE_UPDATED_TIME))));

                    Alarm alarm = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SCHEDULE_ALARM)), Alarm.class);
                    schedule.setAlarm(alarm);
                    scheduleList.add(schedule);

                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();

        return scheduleList;

    }


    public Schedule getScheduleByID(String id) {

        List<Schedule> scheduleList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SCHEDULE + " where " + SCHEDULE_KEY_ID + "= '"
                + userId + "' ORDER BY " + SCHEDULE_KEY_ID + " DESC";

        Cursor cursor = sqlHandler.selectQuery(query);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    Schedule schedule = new Schedule();
                    schedule.setMedicineName(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_NAME)));
                    schedule.setMedicineType(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_TYPE)));
                    schedule.setMedicineUnit(cursor.getString(cursor.getColumnIndex(SCHEDULE_MEDICINE_UNIT)));
                    schedule.setStartDate(cursor.getString(cursor.getColumnIndex(SCHEDULE_START_DATE)));
                    schedule.setDuration(cursor.getString(cursor.getColumnIndex(SCHEDULE_DURATION)));
                    schedule.setIntake(cursor.getString(cursor.getColumnIndex(SCHEDULE_INTAKE)));
                    schedule.setEnable(cursor.getString(cursor.getColumnIndex(SCHEDULE_IS_ENABLE)));
                    schedule.setUpdatedTime(Long.valueOf(cursor.getString(cursor.getColumnIndex(SCHEDULE_UPDATED_TIME))));

                    Alarm alarm = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(SCHEDULE_ALARM)), Alarm.class);
                    schedule.setAlarm(alarm);

                    scheduleList.add(schedule);

                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();

        if (scheduleList.isEmpty())
            return null;
        else
            return scheduleList.get(0);

    }

    public void deleteSchedule(String id) {
        String query = "DELETE FROM " + TABLE_SCHEDULE + " where " + SCHEDULE_KEY_ID + "= '" + id + "'";
        sqlHandler.executeQuery(query);
    }


}
