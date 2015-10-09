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

        Log.d(TAG, "doing in background...");
        try {

            //  Opening socket to server
            Socket socket = null;
            socket = new Socket(Constants.ADDRESS, Constants.PORT);
            Log.d(TAG, "Opened the socket");


            ObjectOutputStream out = null;
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new Integer(classId));

            //  Opening the streams and sending the ClassID parameter
            ObjectInputStream in = null;
            Log.d(TAG, "Creating input stream...");
            in = new ObjectInputStream(socket.getInputStream());
            Log.d(TAG, "Created input stream");


            Log.d(TAG, "in.avilable = " + in.available());

            //  Obtaining the ScheduleChange objects
            Object abstData = null;
            ArrayList<ScheduleChange> data = null;

            Log.d(TAG, "is input stream closed = " + socket.isInputShutdown());
            Log.d(TAG, "in.available() = " + in.available());
            abstData = in.readObject();
            Log.d(TAG, "available it is.");

            if (abstData instanceof ScheduleChange[]) {
                data = new ArrayList();
                data.addAll(Arrays.asList((ScheduleChange[]) abstData));
            }

            //  Closing the socket and streams
            socket.close();
            out.close();
            in.close();

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
