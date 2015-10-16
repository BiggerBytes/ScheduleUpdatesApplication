package com.olympicat.scheduleupdates;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.olympicat.scheduleupdate.R;
import com.olympicat.scheduleupdates.serverdatarecievers.Constants;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleChange;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleDataFactory;

import java.util.ArrayList;


/**
 * Downloads the school data on a regular basis, saving it
 * and triggering notifications to the users when needed.
 * Created by shachamginat on 10/10/2015.
 */
public class AutomaticDataRefresher extends IntentService {

    private static final String TAG = "AutomaticDataRefresher";
    private static final long DELAY_TIME = 1000l * 60l * 20l; // 20 mins refresh interval

    private FileDataManager manager;
    private static SharedPreferences sp;
    private static Context context;


    public AutomaticDataRefresher() {
        super(TAG);
    }

    public void notifyUserOverScheduleChanges() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("שינויי מערכת")
                .setContentText("יש עדכוני מערכת חדשים")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);

        Notification notification = builder.build();
        NotificationManagerCompat.from(this).notify(0, notification);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Doing a data refreshment!");
        sp = getSharedPreferences(getString(R.string.sp_school_class_choice), MODE_PRIVATE);
        int classId = sp.getInt(getString(R.string.key_school_class_choice), -1);
        try {
            Log.d(TAG, sp.getInt(getString(R.string.key_school_class_choice), -1) + "");
            if (!FileDataManager.isReady())
                FileDataManager.setArguments(getFilesDir(), Constants.FILE_NAME);
            manager = FileDataManager.getInstance();
            Log.d(TAG, "1");
            ArrayList<ScheduleChange> oldList = manager.readScheduleChange();
            Log.d(TAG, "2");
            ArrayList<ScheduleChange> newList = new ScheduleDataFactory(classId).getData();
            Log.d(TAG, "3");
            //  Uses a temporary list that will remove all reoccurring schedule changes, to spot new ones.
            ArrayList<ScheduleChange> tempNewList = new ArrayList<ScheduleChange>();
            Log.d(TAG, "4");
            tempNewList.addAll(newList);
            tempNewList.removeAll(oldList);

            if (tempNewList.size() > 0) {
                notifyUserOverScheduleChanges();
                sp.edit().putBoolean(getString(R.string.key_has_changed), true);
            }
            manager.writeScheduleChange(newList, classId);
            Log.d(TAG, "tempNewList.size() = " + tempNewList.size());


        } catch (Exception e) {
            Log.e(TAG, "CRASH");
        }
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Log.d(TAG, "Fuggin message");

        Intent i = new Intent(context, AutomaticDataRefresher.class);

        AutomaticDataRefresher.context = context;
        sp = context.getSharedPreferences(context.getString(R.string.sp_school_class_choice), MODE_PRIVATE);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DELAY_TIME, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
