package com.project.ucare.screens.schedule;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ucare.R;
import com.project.ucare.models.Profile;
import com.project.ucare.models.Schedule;
import com.project.ucare.screens.main.home.ProfileAdapter;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    private List<Schedule> data;

    private Activity context;

    public ScheduleAdapter(Activity context) {
        this.context = context;

    }

    public void setList(List<Schedule> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    interface ScheduleListener {
        void onScheduleLongClick(Schedule schedule, View view);

        void onSwitchClick(Schedule schedule, int position);
    }

    ScheduleAdapter.ScheduleListener scheduleListener;

    public void setScheduleListener(ScheduleAdapter.ScheduleListener scheduleListener) {
        this.scheduleListener = scheduleListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = data.get(position);


        holder.labelName.setText(schedule.getMedicineName());
        holder.labelIntake.setText(schedule.getIntake());
        holder.labelUnit.setText("Unit : " + schedule.getMedicineUnit());
        holder.labelReminder.setText(schedule.getAlarm().getTitle());

        Log.d("TAG", "onBindViewHolder: " + schedule.isEnable());

        if (schedule.isEnable().equalsIgnoreCase("true")) {
            holder.switchId.setChecked(true);

        } else {
            holder.switchId.setChecked(false);

        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (scheduleListener != null) {
                    scheduleListener.onScheduleLongClick(schedule, v);
                }
                return true;
            }
        });

        holder.switchId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String enable = "";
                if (isChecked) enable = "true";
                else  enable = "false";
                schedule.setEnable(enable);
              //  notifyItemChanged(position);
                scheduleListener.onSwitchClick(schedule,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView labelName;
        private TextView labelIntake;
        private TextView labelUnit;
        private TextView labelReminder;
        private SwitchCompat switchId;

        public ViewHolder(View view) {
            super(view);

            labelName = view.findViewById(R.id.labelName);
            labelIntake = view.findViewById(R.id.labelIntake);
            labelUnit = view.findViewById(R.id.labelUnit);
            labelReminder = view.findViewById(R.id.labelReminder);
            switchId = view.findViewById(R.id.switchId);

        }


    }
}
