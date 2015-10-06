package com.olympicat.scheduleupdates;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.olympicat.scheduleupdate.R;
import com.olympicat.scheduleupdates.serverdatarecievers.DataFetcher;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleChange;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ScheduleChange> changes;
    private ScheduleChangeAdapter adapter;
    private RecyclerView rvChanges;

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // force rtl layout
        forceRtlIfSupported();

        // init recycler view
        rvChanges = (RecyclerView) findViewById(R.id.rvChanges);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvChanges.setLayoutManager(llm);

        // locally testing app
        /*
        changes = new ArrayList<>();
        changes.add(new ScheduleChange(27, 2, "הילה כרמי", ScheduleChange.ChangeType.CANCELLED));
        changes.add(new ScheduleChange(27, 4, "לאו קירשנבאום", ScheduleChange.ChangeType.CANCELLED));
        changes.add(new ScheduleChange(26, 2, "וולאדי הגבר", ScheduleChange.ChangeType.CANCELLED));
        */

        DataFetcher df = new DataFetcher();
        Log.v(TAG, "created data fetcher");

        try {
            changes = df.execute(24).get();
            Log.v(TAG, "ordered schedule change");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Snackbar.make(findViewById(android.R.id.content), changes.size() + " עדכונים חדשים", Snackbar.LENGTH_SHORT).show();

        adapter = new ScheduleChangeAdapter(MainActivity.this, changes);
        rvChanges.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRtlIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
