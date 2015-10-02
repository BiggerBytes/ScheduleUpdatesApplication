package com.example.shachamginat.de_shalitupdates;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.example.shachamginat.de_shalitupdates.serverdatarecievers.ScheduleChange;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ScheduleChange> changes;
    private ScheduleChangeAdapter adapter;
    private RecyclerView rvChanges;

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

        // init changes list
        changes = new ArrayList<>();
        changes.add(new ScheduleChange(27, 2, "הילה כרמי", ScheduleChange.ChangeType.CANCELLED));
        changes.add(new ScheduleChange(27, 4, "לאו קירשנבאום", ScheduleChange.ChangeType.CANCELLED));
        changes.add(new ScheduleChange(26, 2, "וולאדי הגבר", ScheduleChange.ChangeType.CANCELLED));

        adapter = new ScheduleChangeAdapter(MainActivity.this, changes);
        rvChanges.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRtlIfSupported() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
