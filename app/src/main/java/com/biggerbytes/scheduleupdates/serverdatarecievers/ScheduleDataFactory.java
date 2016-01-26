package com.biggerbytes.scheduleupdates.serverdatarecievers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shachamginat on 10/10/2015.
 * @author Shacham Ginat The Wise - pussySlay3r_742
 *
 * Pulls all the latest schedule-changes data over from the server.
 */
public class ScheduleDataFactory {


    private int classId;

    public ScheduleDataFactory(int classId) {
        this.classId = classId;
    }


    /**
     * Fetches the data from the server.
     *
     * @return A list which contains all the schedule changes data
     * @throws Exception
     */
    public ArrayList<ScheduleChange> getData() throws Exception {
        //  Opening socket to server
        Socket socket = null;
        socket = new Socket(Constants.ADDRESS, Constants.PORT);


        ObjectOutputStream out = null;
        out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(new Integer(classId));

        //  Opening the streams and sending the ClassID parameter
        ObjectInputStream in = null;
        in = new ObjectInputStream(socket.getInputStream());

        //  Obtaining the ScheduleChange objects
        Object abstData = null;
        ArrayList<ScheduleChange> data = null;

        abstData = in.readObject();

        if (abstData instanceof ScheduleChange[]) {
            data = new ArrayList();
            data.addAll(Arrays.asList((ScheduleChange[]) abstData));
        }

        //  Closing the socket and streams
        socket.close();
        out.close();
        in.close();


        return data;
    }
}
