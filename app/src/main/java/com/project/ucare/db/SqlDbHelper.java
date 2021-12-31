package com.project.ucare.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.ucare.models.Alarm;

public class SqlDbHelper extends SQLiteOpenHelper {

    // profile create
    public static final String TABLE_PROFILE = "table_profile";
    public static final String USER_NAME = "user_name";
    public static final String BIRTH_DATE = "birth_date";
    public static final String USER_GENDER = "user_gender";
    public static final String KEY_ID = "id";
    public static final String PARENT_ID = "parent_id";
    public static final String UPDATED_TIME = "updated_time";


    public static final String TABLE_SCHEDULE = "table_schedule";
    public static final String SCHEDULE_KEY_ID = "s_id";
    public static final String SCHEDULE_USER_ID = "s_userId";
    public static final String SCHEDULE_MEDICINE_NAME = "s_medicineName";
    public static final String SCHEDULE_MEDICINE_TYPE = "s_medicineType";
    public static final String SCHEDULE_MEDICINE_UNIT = "s_medicineUnit";
    public static final String SCHEDULE_START_DATE = "s_startDate";
    public static final String SCHEDULE_DURATION = "s_duration";
    public static final String SCHEDULE_INTAKE = "s_intake";
    public static final String SCHEDULE_IS_ENABLE = "s_isEnable";
    public static final String SCHEDULE_UPDATED_TIME = "s_updatedTime";
    public static final String SCHEDULE_ALARM = "s_alarm";


    private static final String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + " ("
            + KEY_ID + " TEXT PRIMARY KEY, "
            + PARENT_ID + " TEXT, "
            + USER_NAME + " TEXT, "
            + BIRTH_DATE + " TEXT, "
            + USER_GENDER + " TEXT, "
            + UPDATED_TIME + " TEXT)";


    private static final String SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + " ("
            + SCHEDULE_KEY_ID + " TEXT PRIMARY KEY, "
            + SCHEDULE_USER_ID + " TEXT, "
            + SCHEDULE_MEDICINE_NAME + " TEXT, "
            + SCHEDULE_MEDICINE_TYPE + " TEXT, "
            + SCHEDULE_MEDICINE_UNIT + " TEXT, "
            + SCHEDULE_START_DATE + " TEXT, "
            + SCHEDULE_DURATION + " TEXT, "
            + SCHEDULE_INTAKE + " TEXT, "
            + SCHEDULE_IS_ENABLE + " TEXT, "
            + SCHEDULE_UPDATED_TIME + " TEXT, "
            + SCHEDULE_ALARM + " TEXT)";


    public SqlDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PROFILE_TABLE);
        db.execSQL(SCHEDULE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        onCreate(db);
    }

}
