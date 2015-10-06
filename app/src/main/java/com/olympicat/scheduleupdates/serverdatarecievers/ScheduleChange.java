package com.olympicat.scheduleupdates.serverdatarecievers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Avishay
 */
public class ScheduleChange implements Serializable {

    private Integer hour;
    private Calendar date;
    private String teacherName;

    private ChangeType type;

    public ScheduleChange(Calendar date, Integer hour, String teacherName, ChangeType type) {
        this.date = date;
        this.hour = hour;
        this.teacherName = teacherName;
        this.type = type;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public ChangeType getType() {
        return type;
    }

    public void setType(ChangeType type) {
        this.type = type;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    /**
     * describes the type of a change:
     * either lesson cancelled
     * or a sub teacher is placed
     */
    public enum ChangeType {
        CANCELLED, SUB
    }

}