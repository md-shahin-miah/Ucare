package com.project.ucare.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ucare.R;
import com.project.ucare.common.Utils;
import com.project.ucare.db.ProfileHandler;
import com.project.ucare.db.ScheduleHandler;
import com.project.ucare.models.Profile;
import com.project.ucare.models.Schedule;
import com.project.ucare.screens.auth.LoginActivity;
import com.project.ucare.screens.main.MainActivity;
import com.xihad.androidutils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SplashActivity extends AppCompatActivity {

    ProfileHandler profileHandler;
    ScheduleHandler scheduleHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        AndroidUtils.Companion.init(SplashActivity.this);
        AndroidUtils.Companion.setStatusBarColor(R.color.white);

        profileHandler = new ProfileHandler(this);
        scheduleHandler = new ScheduleHandler(this);


        if (AndroidUtils.Companion.isInternetAvailable()) {
            sync();
        } else {

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNext();

                }
            }, 1000);

        }

    }

    private void startNext() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        finish();

    }


    private void sync() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNext();
                }
            }, 1000);

        } else {
            getProfile();
            syncProfiles();
            syncSchedules();
        }

    }

    private void getProfile() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (profile != null)
                        profileHandler.addProfile(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void syncProfiles() {

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseDatabase.getInstance().getReference().child("Profile").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Profile> profileList = new ArrayList<>();
                List<Profile> profileListLocal;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Profile profile = ds.getValue(Profile.class);
                    profileList.add(profile);
                }

                profileListLocal = profileHandler.getProfileList(Utils.firebaseUserId());
                boolean flg = true;
                for (Profile profile : profileList) {
                    flg = true;
                    for (Profile profileLocal : profileListLocal) {
                        if (profile.getId().equalsIgnoreCase(profileLocal.getId())) {
                            flg = false;
                            break;
                        }
                    }

                    if (flg) {
                        profileHandler.addProfile(profile);
                        Log.d("aaa", "onDataChange:  local");
                    }

                }


                for (Profile profileLocal : profileListLocal) {
                    flg = true;
                    for (Profile profile : profileList) {
                        if (profileLocal.getId().equalsIgnoreCase(profile.getId())) {
                            flg = false;
                            break;
                        }
                    }
                    if (flg) {
                        FirebaseDatabase.getInstance().getReference().child("Profile").child(userId).child(profileLocal.getId()).setValue(profileLocal);
                        Log.d("aaa", "onDataChange:  firebase");
                    }

                }

                profileListLocal = profileHandler.getProfileList(Utils.firebaseUserId());

                for (Profile profileLocal : profileListLocal) {
                    flg = true;
                    for (Profile profile : profileList) {
                        if (profileLocal.getId().equalsIgnoreCase(profile.getId())) {
                            if (profileLocal.getUpdatedTime() > profile.getUpdatedTime()) {
                                FirebaseDatabase.getInstance().getReference().child("Profile").child(userId).child(profileLocal.getId()).setValue(profileLocal);
                            }
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void syncSchedules() {


        FirebaseDatabase.getInstance().getReference().child("Schedule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Schedule> scheduleList = new ArrayList<>();
                List<Schedule> scheduleListLocal;


                for (DataSnapshot ds : snapshot.getChildren()) {
                    Schedule schedule = ds.getValue(Schedule.class);
                    scheduleList.add(schedule);
                }

                scheduleListLocal = scheduleHandler.getAllSchedules();


                boolean flg = true;
                for (Schedule schedule : scheduleList) {
                    flg = true;
                    for (Schedule scheduleLocal : scheduleListLocal) {
                        if (schedule.getId().equalsIgnoreCase(scheduleLocal.getId())) {
                            flg = false;
                            break;
                        }

                    }

                    if (flg) {
                        scheduleHandler.addSchedule(schedule);
                    }
                }

                for (Schedule scheduleLocal : scheduleListLocal) {
                    flg = true;
                    for (Schedule schedule : scheduleList) {
                        if (scheduleLocal.getId().equalsIgnoreCase(schedule.getId())) {
                            flg = false;
                            break;
                        }
                    }

                    if (flg) {
                        FirebaseDatabase.getInstance().getReference().child("Schedule").child(scheduleLocal.getId()).setValue(scheduleLocal);
                        Log.d("aaa", "onDataChange:  firebase");
                    }
                }

                scheduleListLocal = scheduleHandler.getAllSchedules();

                for (Schedule scheduleLocal : scheduleListLocal) {
                    flg = true;
                    for (Schedule schedule : scheduleList) {
                        if (scheduleLocal.getId().equalsIgnoreCase(schedule.getId())) {
                            if (scheduleLocal.getUpdatedTime() > scheduleLocal.getUpdatedTime()) {
                                FirebaseDatabase.getInstance().getReference().child("Schedule").child(scheduleLocal.getId()).setValue(scheduleList);
                            }
                            break;
                        }
                    }
                }

                startNext();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                startNext();
            }
        });

    }


}