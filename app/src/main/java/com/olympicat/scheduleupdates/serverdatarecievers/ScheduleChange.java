package com.olympicat.scheduleupdates.serverdatarecievers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Avishay
 */
public class ScheduleChange implements Serializable {

    public static final long serialVersionUID = 1l;

    private String hour;
    private String date;
    private String teacherName;

    private ChangeType type;

    public ScheduleChange(String date, String hour, String teacherName, ChangeType type) {
        this.date = date;
        this.hour = hour;
        this.teacherName = teacherName;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleChange that = (ScheduleChange) o;

        if (!hour.equals(that.hour)) return false;
        if (!date.equals(that.date)) return false;
        if (!teacherName.equals(that.teacherName)) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = hour.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + teacherName.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}