package com.example.shachamginat.de_shalitupdates.serverdatarecievers;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Avishay
 */
public class ScheduleChange implements Serializable {

    private Integer hour;
    private String teacherName;
    private Integer dayInMonth;

    protected ScheduleChange(Integer dayInMonth, Integer hour, String teacherName) {
        this.dayInMonth = dayInMonth;
        this.hour = hour;
        this.teacherName = teacherName;
    }

    public Integer getDayInMonth() {
        return dayInMonth;
    }

    public void setDayInMonth(Integer dayInMonth) {
        this.dayInMonth = dayInMonth;
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

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

}