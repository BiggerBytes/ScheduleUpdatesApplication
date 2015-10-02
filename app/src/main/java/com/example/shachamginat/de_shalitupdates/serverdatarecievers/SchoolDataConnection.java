package com.example.shachamginat.de_shalitupdates.serverdatarecievers;

import com.example.shachamginat.de_shalitupdates.serverdatarecievers.ScheduleChange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by shachamginat on 9/28/15.
 */
public class SchoolDataConnection {

    private final String ADDRESS;
    private final Integer PORT;

    public SchoolDataConnection(String ADDRESS, Integer PORT) {
        this.ADDRESS = ADDRESS;
        this.PORT = PORT;
    }

    public ArrayList<ScheduleChange> orderScheduleChange(int classID) throws UnknownHostException, IOException, ClassNotFoundException {
        //  Opening socket to server
        Socket socket = new Socket(ADDRESS, PORT);

        //  Opening the streams and sending the ClassID parameter
        PrintStream out = new PrintStream(socket.getOutputStream());
        out.println(classID);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream()) {
            @Override
            protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
                ObjectStreamClass clazz = super.readClassDescriptor();
                if (clazz.getName().contains("ScheduleChange"))
                    return ObjectStreamClass.lookup(ScheduleChange.class);
                return clazz;
            }
        };

        //  Obtaining the ScheduleChange objects
        Object abst_data = in.readObject();
        ArrayList<ScheduleChange> data = null;
        data = (ArrayList<ScheduleChange>) abst_data;


        //  Closing the socket and streams
        socket.close();
        out.close();
        in.close();

        return data;
    }

}
