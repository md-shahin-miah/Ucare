package com.project.ucare.models;

import java.io.Serializable;

public class Schedule implements Serializable {
    String id;
    String userId;
    String medicineName;
    String medicineType;
    String medicineUnit;
    String startDate;
    String duration;
    String intake;
    String enable;
    Long updatedTime;
    Alarm alarm;


    public Schedule() {
    }

    public Schedule(String id, String userId, String medicineName, String medicineType, String medicineUnit, String startDate, String duration, String intake, String enable, Long updatedTime, Alarm alarm) {
        this.id = id;
        this.userId = userId;
        this.medicineName = medicineName;
        this.medicineType = medicineType;
        this.medicineUnit = medicineUnit;
        this.startDate = startDate;
        this.duration = duration;
        this.intake = intake;
        this.enable = enable;
        this.updatedTime = updatedTime;
        this.alarm = alarm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getMedicineUnit() {
        return medicineUnit;
    }

    public void setMedicineUnit(String medicineUnit) {
        this.medicineUnit = medicineUnit;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String isEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }
}

