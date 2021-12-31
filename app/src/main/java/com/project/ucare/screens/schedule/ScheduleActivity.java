package com.project.ucare.screens.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Log;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ucare.R;
import com.project.ucare.common.Utils;
import com.project.ucare.db.ScheduleHandler;
import com.project.ucare.screens.alarm.AlarmHandler;
import com.project.ucare.screens.main.createprofile.CreateProfileActivity;
import com.project.ucare.screens.medicine.AddMedicineActivity;
import com.project.ucare.models.Profile;
import com.project.ucare.models.Schedule;
import com.xihad.androidutils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity implements ScheduleAdapter.ScheduleListener {

    RecyclerView recyclerView;

    ScheduleAdapter adapter;
    ProgressBar progressBar;
    TextView noData, iconText, labelName;

    private List<Schedule> scheduleList = new ArrayList<>();

    Profile profile;

    FloatingActionButton floatingActionButton;
    ScheduleHandler scheduleHandler;
    String userId = "";

    private List<Schedule> scheduleListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        AndroidUtils.Companion.init(this);


        try {
            profile = (Profile) getIntent().getSerializableExtra("profile");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AndroidUtils.sharePrefSettings.getStringValue("pro").equalsIgnoreCase("")) {
            userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        } else {
            userId = AndroidUtils.sharePrefSettings.getStringValue("pro");
        }

        scheduleHandler = new ScheduleHandler(this);

        floatingActionButton = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.profileList);
        noData = findViewById(R.id.noData);
        iconText = findViewById(R.id.iconText);
        labelName = findViewById(R.id.labelName);


        if (profile != null) {
            labelName.setText(profile.getName());
            iconText.setText(AndroidUtils.Companion.splitString(profile.getName(), 1));
        }


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ScheduleActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Set Adapter
        adapter = new ScheduleAdapter(ScheduleActivity.this);
        adapter.setScheduleListener(ScheduleActivity.this);
        recyclerView.setAdapter(adapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, AddMedicineActivity.class);
                intent.putExtra("schedule", "null");
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocalData();
    }

    private void getLocalData() {

        scheduleListData = scheduleHandler.getSchedules(userId);
        progressBar.setVisibility(View.GONE);

        if (scheduleListData.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }

        for (Schedule schedule : scheduleListData) {

            Log.d("TAG", "getLocalData: " + schedule.isEnable());
        }

        adapter.setList(scheduleListData);

    }


    @Override
    public void onScheduleLongClick(Schedule schedule, View view) {

        PopupMenu popup = new PopupMenu(this, view, Gravity.RIGHT);
        popup.inflate(R.menu.menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.edit) {
                    Intent intent = new Intent(ScheduleActivity.this, AddMedicineActivity.class);
                    intent.putExtra("schedule", schedule);
                    startActivity(intent);

                    return true;
                } else if (itemId == R.id.delete) {
                    askToDelete(schedule);
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onSwitchClick(Schedule schedule, int position) {

        android.util.Log.d("TAG", "onSwitchClick: called ");

        if (schedule.isEnable().equalsIgnoreCase("true")) {
            AlarmHandler handler = new AlarmHandler(this, schedule);
            handler.startAlarm(schedule.getAlarm().getHour(), schedule.getAlarm().getMinute());
            scheduleHandler.addSchedule(schedule);
        } else {
            AlarmHandler handler = new AlarmHandler(this, schedule);
            handler.cancelAlarm();
            scheduleHandler.addSchedule(schedule);
        }

    }

    private void askToDelete(Schedule schedule) {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)

                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_24)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        scheduleHandler.deleteSchedule(schedule.getId());
                        deleteSchedule(schedule);
                        getLocalData();

                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        myQuittingDialogBox.show();

    }


    private void deleteSchedule(Schedule schedule) {

        FirebaseDatabase.getInstance().getReference().child("Schedule")
                .child(schedule.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ScheduleActivity.this, "Deleted Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleActivity.this, "Deleted Unsuccessful, Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}