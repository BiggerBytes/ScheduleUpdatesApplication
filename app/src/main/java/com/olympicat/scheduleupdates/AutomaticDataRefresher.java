package com.olympicat.scheduleupdates;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.olympicat.scheduleupdate.R;
import com.olympicat.scheduleupdates.serverdatarecievers.Constants;
import com.olympicat.scheduleupdates.serverdatarecievers.DataFetcher;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleChange;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleDataFactory;

import java.util.ArrayList;


/**
 * Downloads the school data on a regular basis, saving it
 * and triggering notifications to the users when needed.
 * Created by shachamginat on 10/10/2015.
 */
public class AutomaticDataRefresher extends IntentService {

    private static final String TAG = "NotificationService";
    private static final long DELAY_TIME = 1000l*60l*60l;

    private FileDataManager manager;
    private static SharedPreferences sp;
    private static Context parent;
    private static Context context;



    public AutomaticDataRefresher(){
        super(TAG);
    }

    public void alertChangesChanged() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("שינויי מערכת")
            .setContentText("יש עדכוני מערכת חדשים");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);

        Notification notification = builder.build();
        NotificationManagerCompat.from(this).notify(0, notification);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d (TAG, "LOOPY LOOPS!");
        if (!FileDataManager.isClassReady()) FileDataManager.setArguments(getFilesDir(), Constants.FILE_NAME);
        if (manager == null) manager = FileDataManager.getInstance();

        try {
            Log.d(TAG, sp.getInt(parent.getString(R.string.key_school_class_choice), -1) + "");
            ScheduleDataFactory factory = new ScheduleDataFactory(sp.getInt(getString(R.string.key_school_class_choice), -1));
            ArrayList<ScheduleChange> data = factory.getData(),
                formerData = manager.readScheduleChange();
            if (!data.equals(formerData) && data.size()>formerData.size())
                alertChangesChanged();



        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    public static void SetServiceAlarm (Context context, boolean isOn){
        Log.d(TAG, "Fuggin message");
        if (!FileDataManager.isClassReady()) {
            FileDataManager.setArguments(context.getFilesDir(), Constants.FILE_NAME);
        }
        Intent i = new Intent(context, AutomaticDataRefresher.class);

        AutomaticDataRefresher.context = context;
        AutomaticDataRefresher.parent = context;
        sp = context.getSharedPreferences(parent.getString(R.string.sp_school_class_choice), MODE_PRIVATE);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), DELAY_TIME, pi);

        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
