package com.biggerbytes.scheduleupdates;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.biggerbytes.scheduleupdate.R;
import com.biggerbytes.scheduleupdates.serverdatarecievers.ScheduleChange;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@inheritDoc}
 * The main activity of the application
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<ScheduleChange> changes;
    private ScheduleChangeAdapter adapter;
    private RecyclerView rvChanges;
    private com.biggerbytes.scheduleupdates.serverdatarecievers.DataFetcher df;
    private ProgressBar progressBar;
    private AdView adView;
    // X1
    private RelativeLayout emptyView;
    //X2
    private SharedPreferences sharedPreferences;
    private int userClass;
    private ChooseClassDialog chooseClassDialog;

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!FileDataManager.isReady())
            FileDataManager.setArguments(getFilesDir(), com.biggerbytes.scheduleupdates.serverdatarecievers.Constants.FILE_NAME);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // force rtl layout
        forceRtlIfSupported();

        // init progress bar
        progressBar = (ProgressBar) findViewById(R.id.pbLoading);

        // check if user has selected class
        // if not, show him the dialog of selecting a class
        // if so, continue to loading the data for his class
        sharedPreferences = getSharedPreferences(getString(R.string.sp_school_class_choice), MODE_PRIVATE);
        userClass = sharedPreferences.getInt(getString(R.string.key_school_class_choice), -1);
        if (userClass == -1) {
            Log.v(TAG, "first time huh?");
            // user has not made a choice yet
            // open the dialog
            chooseClassDialog = new ChooseClassDialog();
            chooseClassDialog.show(getFragmentManager(), "Choose Class Dialog");
            chooseClassDialog.setOnDialogFinishListener(new ChooseClassDialog.OnDialogFinishListener() {
                @Override
                public void onFinish(int id) {
                    userClass = id;
                    Log.v(TAG, "got class id: " + id);
                    loadChanges();
                }
            });
        } else {
            // user has made a choice
            Log.v(TAG, "Success, choice: " + userClass);
            loadChanges();
        }

        // init empty view
        emptyView = (RelativeLayout) findViewById(R.id.emptyView);

        // init recycler view
        rvChanges = (RecyclerView) findViewById(R.id.rvChanges);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvChanges.setLayoutManager(llm);

        changes = new ArrayList<>();
        adapter = new ScheduleChangeAdapter(this, changes);
        rvChanges.setAdapter(adapter);

        com.biggerbytes.scheduleupdates.AutomaticDataRefresher.setServiceAlarm(this, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DIED!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_school_class:
                chooseClassDialog = new ChooseClassDialog();
                chooseClassDialog.show(getFragmentManager(), "Choose Class Dialog");
                chooseClassDialog.setOnDialogFinishListener(new ChooseClassDialog.OnDialogFinishListener() {
                    @Override
                    public void onFinish(int id) {
                        userClass = id;
                        Log.v(TAG, "got class id: " + id);
                        loadChanges();
                    }
                });
                break;

            case R.id.about:
                Toast.makeText(this, "אמ.. מה רושמים כאן? הא כן, תודה לאורן ♥", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRtlIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    private void loadChanges() {
        initDataFetcher();
        if (rvChanges != null) {
            rvChanges.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rvChanges.setLayoutManager(llm);

            changes = new ArrayList<>();
            adapter = new ScheduleChangeAdapter(this, changes);
            rvChanges.setAdapter(adapter);
        }

        progressBar.setVisibility(View.VISIBLE);

        if (userClass != -1) {
            Log.v(TAG, "AsyncTask status: " + df.getStatus());
            df.execute(userClass);
            Log.v(TAG, "fetching data for class " + userClass + "...");
        }

    }


    /**
     * creates a new data fetcher and sets up its listeners
     */
    private void initDataFetcher() {
        df = new com.biggerbytes.scheduleupdates.serverdatarecievers.DataFetcher(new com.biggerbytes.scheduleupdates.serverdatarecievers.DataFetcher.OnChangesReceivedListener() {
            @Override
            public void onChangesReceived(ArrayList<ScheduleChange> data) {
                Log.v(TAG, "d is null: " + (data == null));
                if (data != null && data.size() > 0) {

                    // data is received and there is data
                    Log.v(TAG, "Data is not null");
                    changes.clear();
                    changes.addAll(data);
                    removeDuplicates(changes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvChanges.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            Log.v(TAG, "updated adapter");
                            if (sharedPreferences.getBoolean(getString(R.string.key_has_changed), false)) {
                                Snackbar.make(findViewById(android.R.id.content), changes.size() + " עדכונים חדשים", Snackbar.LENGTH_SHORT).show();
                                sharedPreferences.edit().putBoolean(getString(R.string.key_has_changed), false);
                            }
                        }
                    });

                } else if (data != null) {
                    // empty view
                    Log.v(TAG, "Showing empty view!");
                    // needs to be from the ui thread apparently
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvChanges.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    // error
                    Log.v(TAG, "Error. Cannot read server.");
                }
            }
        });
    }

    /**
     * removes the duplicates from the changes array list
     */
    public void removeDuplicates(ArrayList<ScheduleChange> changes) {
        Collections.sort(changes, new Comparator<ScheduleChange>() {
            @Override
            public int compare(ScheduleChange lhs, ScheduleChange rhs) {
                return lhs.getTeacherName().compareTo(rhs.getTeacherName());
            }
        });
        Log.v(TAG, "Changes has " + changes.size());
        List<ScheduleChange> removeList = new ArrayList<ScheduleChange>();
        for (int i = 0; i < changes.size() - 1; ++i) {
            String hour = changes.get(i).getHour();
            for (int j = i + 1; j < changes.size(); ++j) {
                if (changes.get(i).getTeacherName().equals(changes.get(j).getTeacherName()) && changes.get(i).getDate().equals(changes.get(j).getDate())) {
                    hour = changes.get(i).getHour() + "-" + changes.get(j).getHour();
                    Log.v(TAG, "Added index " + j + " to list");
                    removeList.add(changes.get(j));
                } else {
                    break;
                }
            }
            changes.get(i).setHour(hour);
        }
        for (int i = 0; i < removeList.size(); ++i) {
            changes.remove(removeList.get(i));
            Log.v(TAG, "Changes has " + changes.size());
        }
        Collections.sort(changes, new Comparator<ScheduleChange>() {
            @Override
            public int compare(ScheduleChange lhs, ScheduleChange rhs) {
                String[][] props = new String[][] {lhs.getDate().split("\\."), rhs.getDate().split("\\.")};
                Log.d(TAG, lhs.getDate());
                Log.d(TAG, rhs.getDate());


                for (int i = 2; i > 0; i--) {
                    Log.d(TAG, "Checking between " + props[0][i] + " and " + props[1][i] + ", result is " + props[0][i].compareTo(props[1][i]));
                    int diff = props[0][i].compareTo(props[1][i]);
                    if (diff > 0)
                        return 1;
                    else if (diff < 0)
                        return -1;
                }

                return props[0][0].compareTo(props[1][0]);
            }


        });
    }
}
