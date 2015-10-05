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

    public DataFetcher() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.v(TAG, "before?");
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
            Log.d(TAG, "Something with input stream");
            Log.d(TAG, "Creating inputstream");
            in = new ObjectInputStream(socket.getInputStream());
            Log.d(TAG, "Created inputstream");


            Log.d(TAG, "in.avilable = " + in.available());

            //  Obtaining the ScheduleChange objects
            Object abstData = null;
            ArrayList<ScheduleChange> data = null;

            //   data = (ArrayList<ScheduleChange>) abst_data;
            Log.d(TAG, "is inputstream closed = " + socket.isInputShutdown());
//            data = (ArrayList<ScheduleChange>) in.readObject();
            Log.d(TAG, "in.available() = " + in.available());
            abstData = in.readObject();
            Log.d(TAG, "available it is");

            if (abstData instanceof ScheduleChange[]) {
                data = new ArrayList();
                data.addAll(Arrays.asList((ScheduleChange[]) abstData));
            }
//        ArrayList<ScheduleChange> data = null;
//        data = (ArrayList<ScheduleChange>) abst_data;


            //  Closing the socket and streams
            socket.close();
            out.close();
            in.close();

            return data;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }


    }
}
