package com.project.ucare.models;


import java.io.Serializable;
import java.util.List;

public class Alarm  implements Serializable {

    private String title;
    private int hour;
    private int minute;
    private int id;
    List<String> days;

    public Alarm() {
    }


    public Alarm(String title, int hour, int minute, int id, List<String> days) {
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.id = id;
        this.days = days;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
