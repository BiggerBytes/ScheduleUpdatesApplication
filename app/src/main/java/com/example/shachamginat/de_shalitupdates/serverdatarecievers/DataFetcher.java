package com.example.shachamginat.de_shalitupdates.serverdatarecievers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

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

        Log.v(TAG, "doing in background...");

        //  Opening socket to server
        Socket socket = null;
        try {
            socket = new Socket(Constants.ADDRESS, Constants.PORT);
            Log.v(TAG, "Opened the socket");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  Opening the streams and sending the ClassID parameter
        PrintStream out = null;
        try {
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(classId);
        ObjectInputStream in = null;
        Log.v(TAG, "Something with input stream");
        try {
            in = new ObjectInputStream(socket.getInputStream()) {
                @Override
                protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
                    ObjectStreamClass clazz = super.readClassDescriptor();
                    if (clazz.getName().contains("ScheduleChange"))
                        return ObjectStreamClass.lookup(ScheduleChange.class);
                    return clazz;
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  Obtaining the ScheduleChange objects
        Object abst_data = null;
        try {
            abst_data = in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ScheduleChange> data = null;
        data = (ArrayList<ScheduleChange>) abst_data;


        //  Closing the socket and streams
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
