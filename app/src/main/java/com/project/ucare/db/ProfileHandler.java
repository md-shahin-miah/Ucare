package com.project.ucare.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.project.ucare.models.Profile;

import java.util.ArrayList;
import java.util.List;

import static com.project.ucare.db.SqlDbHelper.*;

public class ProfileHandler {


    Context context;
    SqlHandler sqlHandler;

    public ProfileHandler(Context context) {
        this.context = context;
        sqlHandler = new SqlHandler(context);
    }

    //adding content to table
    public long addProfile(Profile profile) {

        ContentValues values = new ContentValues();
        values.put(KEY_ID, profile.getId());
        values.put(PARENT_ID, profile.getParent_id());
        values.put(USER_NAME, profile.getName());
        values.put(USER_GENDER, profile.getGender());
        values.put(BIRTH_DATE, profile.getBirth_date());
        values.put(UPDATED_TIME, profile.getUpdatedTime().toString());

        return sqlHandler.upsert(TABLE_PROFILE, values);
    }

    String parent_Id;

    public List<Profile> getProfileList(String parent_id) {

        parent_Id = parent_id;

        List<Profile> profileList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PROFILE + " where " + PARENT_ID + "= '"
                + parent_id + "' ORDER BY " + KEY_ID + " DESC";

        Cursor cursor = sqlHandler.selectQuery(query);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    Profile profile = new Profile();
                    profile.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                    profile.setBirth_date(cursor.getString(cursor.getColumnIndex(BIRTH_DATE)));
                    profile.setGender(cursor.getString(cursor.getColumnIndex(USER_GENDER)));
                    profile.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    profile.setParent_id(cursor.getString(cursor.getColumnIndex(PARENT_ID)));
                    profile.setUpdatedTime(cursor.getLong(cursor.getColumnIndex(UPDATED_TIME)));

                    profileList.add(profile);

                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();

        return profileList;

    }


    public Profile getProfileByID(String id) {

        List<Profile> profileList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PROFILE + " where " + KEY_ID + "= '"
                + id + "' ORDER BY " + KEY_ID + " DESC";

        Cursor cursor = sqlHandler.selectQuery(query);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Profile profile = new Profile();
                    profile.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                    profile.setBirth_date(cursor.getString(cursor.getColumnIndex(BIRTH_DATE)));
                    profile.setGender(cursor.getString(cursor.getColumnIndex(USER_GENDER)));
                    profile.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    profile.setParent_id(cursor.getString(cursor.getColumnIndex(PARENT_ID)));
                    profile.setUpdatedTime(cursor.getLong(cursor.getColumnIndex(UPDATED_TIME)));

                    profileList.add(profile);

                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();

        if (profileList.isEmpty())
            return null;
        else
            return profileList.get(0);
    }

    public void deleteProfile(String id) {
        String query = "DELETE FROM " + TABLE_PROFILE + " where " + KEY_ID + "= '" + id + "'";
        sqlHandler.executeQuery(query);
    }

}
