package com.olympicat.scheduleupdates;

import android.content.Context;
import android.util.Log;

import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleChange;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Manager of the saved schdulechange data saved on a file.
 * Created by shachamginat on 10/10/2015.
 */


public class FileDataManager {
    private static final String TAG = "FileDataManager";

    private static File file;
    private static FileDataManager manager;


    private FileDataManager(){
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeScheduleChange(new ArrayList<ScheduleChange>());
        }
    }
    /**
     * A must used method to initiate static object 'file'
     *
     * @param dir The File object representing the directory of the file
     * @param name The name of the file including type [xxx.txt, xxx.dat ...]
     */
    public static void setArguments(File dir, String name){
        if (file == null) file = new File(dir, name);
    }

    /**
     *
     * @return <b>true</b> if the needed arguments were initialized. <b>false</b> otherwise.
     */
    public static boolean isClassReady() {
        return !(file==null);
    }

    public static FileDataManager getInstance() {
        return manager == null ? manager = new FileDataManager() : manager;
    }


    /**
     *
     * @param change The ScheduleData object to write inside the file
     * @return <b>true</b> if the writing was successful, <b>false</b> otherwise.
     */
    public boolean writeScheduleChange(ArrayList<ScheduleChange> change) {
        synchronized (file) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(change);
                return true;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
    }

    /**
     * Returns the ScheduleChange data found in the file
     * @return The ScheduleChange data. Returns 'null' if the
     * data is empty or broken.
     */
    public ArrayList<ScheduleChange> readScheduleChange() {
        synchronized (file) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Object absData = ois.readObject();
                if (absData == null)
                    return new ArrayList<ScheduleChange>(0);
                return (ArrayList<ScheduleChange>) absData;
            } catch (IOException | ClassNotFoundException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }
    }
}
