package com.olympicat.scheduleupdates.serverdatarecievers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Avishay
 */
public class ScheduleChange implements Serializable {

    public static final long serialVersionUID = 1l;

    private String hour;
    private String date;
    private String teacherName;
    private String subTeacher = null; //hooray for being lazy, location not included

    public ScheduleChange(String date, String hour, String teacherName) {
        this.date = date;
        this.hour = hour;
        this.teacherName = teacherName;
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

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    public String getSubTeacher() {
        return subTeacher;
    }

    public void setSubTeacher(String subTeacher) {
        this.subTeacher = subTeacher;
    }
}