package com.biggerbytes.scheduleupdates.serverdatarecievers;

import android.os.AsyncTask;
import android.util.Log;

import com.biggerbytes.scheduleupdates.FileDataManager;

import java.util.ArrayList;

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
        ArrayList<ScheduleChange> data = null;
        FileDataManager manager = FileDataManager.getInstance();
        try {
            Log.d(TAG, "classId = " + classId);
            Integer latestId = FileDataManager.getLatestClassId();
            if (latestId == null || latestId != classId) {
                com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleDataFactory factory = new ScheduleDataFactory(classId);
                manager.writeScheduleChange(factory.getData(), classId);
            }

            data = manager.readScheduleChange();
            if (this.listener != null)
                this.listener.onChangesReceived(data);
            return data;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
