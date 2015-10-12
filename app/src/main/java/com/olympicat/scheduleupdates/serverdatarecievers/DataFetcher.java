package com.olympicat.scheduleupdates.serverdatarecievers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by OrenUrbach on 10/2/15.
 * Fetches the data from Avishay's server
 */
public class DataFetcher extends AsyncTask<Integer, Void, ArrayList<ScheduleChange>> {

    private final static String TAG = "DataFetcher";

    private OnChangesReceivedListener listener;

    public DataFetcher() {

    }

    public DataFetcher(OnChangesReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<ScheduleChange> doInBackground(Integer... params) {
        int classId = params[0];
        try {
            ScheduleDataFactory factory = new ScheduleDataFactory(classId);
            ArrayList<ScheduleChange> data = factory.getData();
            if (this.listener != null)
                this.listener.onChangesReceived(data);
            return data;
        } catch (Exception e) {
            Log.v(TAG, "array is empty.");
            e.printStackTrace();

            if (this.listener != null)
                this.listener.onChangesReceived(new ArrayList<ScheduleChange>());
            return null;
        }


    }

    /**
     * a way to communicate with the main activity
     */
    public interface OnChangesReceivedListener {
        void onChangesReceived(ArrayList<ScheduleChange> data);
    }
}
