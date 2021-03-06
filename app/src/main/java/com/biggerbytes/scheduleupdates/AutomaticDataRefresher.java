package com.biggerbytes.scheduleupdates;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.biggerbytes.scheduleupdate.R;

import java.util.ArrayList;


/**
 * Downloads the school data on a regular basis, saving it
 * and triggering notifications to the users when needed.
 * Created by shachamginat on 10/10/2015.
 */
public class AutomaticDataRefresher extends IntentService {

    private static final String TAG = "AutomaticDataRefresher";
    private static final long DELAY_TIME = 1000l * 60l * 20l; // 20 mins refresh interval

    private com.biggerbytes.scheduleupdates.FileDataManager manager;
    private static SharedPreferences sp;
    private static Context context;


    public AutomaticDataRefresher() {
        super(TAG);
    }


    /**
     * Notifies the user over a submitted scheduele change.
     *
     */
    public void notifyUserOverScheduleChanges() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_schedule_white_48dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.happy))
                .setContentTitle("שינויי מערכת")
                .setContentText("יש עדכוני מערכת חדשים")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        Intent intent = new Intent(this, com.biggerbytes.scheduleupdates.MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);

        Notification notification = builder.build();
        NotificationManagerCompat.from(this).notify(0, notification);

    }

    /**
     *
     * Runs in the background and re-downloads all the schedule changes in a constant interval.<br>
     *               notifies user over a new schedule change.
     * @param intent The service intent - allows the method to run in an interval in the background.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Doing a data refreshment!");
        sp = getSharedPreferences(getString(R.string.sp_school_class_choice), MODE_PRIVATE);
        int classId = sp.getInt(getString(R.string.key_school_class_choice), -1);
        try {
            Log.d(TAG, sp.getInt(getString(R.string.key_school_class_choice), -1) + "");
            if (!com.biggerbytes.scheduleupdates.FileDataManager.isReady())
                com.biggerbytes.scheduleupdates.FileDataManager.setArguments(getFilesDir(), com.biggerbytes.scheduleupdates.serverdatarecievers.Constants.FILE_NAME);
            manager = com.biggerbytes.scheduleupdates.FileDataManager.getInstance();
            Log.d(TAG, "1");
            ArrayList<com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange> oldList = manager.readScheduleChange();
            Log.d(TAG, "2");
            ArrayList<com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange> newList = new com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleDataFactory(classId).getData();
            Log.d(TAG, "3");
            //  Uses a temporary list that will remove all reoccurring schedule changes, to spot new ones.
            ArrayList<com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange> tempNewList = new ArrayList<com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange>();
            Log.d(TAG, "4");
            tempNewList.addAll(newList);
            for (com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange chng : tempNewList) {
                Log.d(TAG, "NEW: " + chng.getHour());
            }
            for (com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange chng : oldList) {
                Log.d(TAG, "OLD: " + chng.getHour());
            }
            tempNewList.removeAll(oldList);

            if (tempNewList.size() > 0) {
                notifyUserOverScheduleChanges();
                sp.edit().putBoolean(getString(R.string.key_has_changed), true);
            }
            manager.writeScheduleChange(newList, classId);

            Log.d(TAG, "tempNewList.size() = " + tempNewList.size());


        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    /**
     *  A method used to kick-start the service, to have it working in an interval.
     * @param context
     * @param isOn determines whether or not to turn on the notifications over schedule changes
     */
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