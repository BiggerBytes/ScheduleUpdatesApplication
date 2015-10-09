package com.olympicat.scheduleupdates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleChange;
import com.olympicat.scheduleupdate.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by OrenUrbach on 10/1/15.
 * adapter for schedule changes recyclerview
 */
public class ScheduleChangeAdapter extends RecyclerView.Adapter<ScheduleChangeAdapter.ScheduleChangeViewHolder> {

    private ArrayList<ScheduleChange> changes;

    private final static String TAG = "ScheduleChangeAdapter";

    public ScheduleChangeAdapter(ArrayList<ScheduleChange> changes) {
        this.changes = changes;
    }

    @Override
    public ScheduleChangeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_change, viewGroup, false);
        ScheduleChangeViewHolder mvh = new ScheduleChangeViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(ScheduleChangeViewHolder ScheduleChangeViewHolder, int index) {
        final ScheduleChange ScheduleChange = changes.get(index);

        ScheduleChange change = this.changes.get(index);

        ScheduleChangeViewHolder.tvTeacherName.setText(change.getTeacherName());
        ScheduleChangeViewHolder.tvHour.setText(change.getHour());
        ScheduleChangeViewHolder.tvDate.setText("" + formatDate(change.getDate()));
    }

    @Override
    public int getItemCount() {
        return this.changes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class ScheduleChangeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTeacherName, tvHour, tvDate;

        public ScheduleChangeViewHolder(View itemView) {
            super(itemView);
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            tvHour = (TextView) itemView.findViewById(R.id.tvHours);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }


    public String formatDate(String given) {
        // result will be constructed here
        StringBuffer sb = new StringBuffer();

        sb.append("יום ");
        sb.append(getDayInHebrew(given));
        sb.append(" - ");
//        sb.append(given.get(Calendar.DAY_OF_MONTH) + "/" + given.get(Calendar.MONTH));
        sb.append(given.substring(0, 5).replace("\\.", "g"));

        return sb.toString();
    }

    public String getDayInHebrew(String date) {
        Log.v(TAG, "date: " + date);
        HashMap<Integer, String> hm = new HashMap<>();
        hm.put(2, "ראשון");
        hm.put(3, "שני");
        hm.put(4, "שלישי");
        hm.put(5, "רביעי");
        hm.put(6, "חמישי");
        hm.put(7, "שישי");
        // no saturday because there is no school on saturday

        String[] dateArr = date.split("\\.");
//        int day = Integer.parseInt(date.substring(0,2));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[0]));
        c.set(Calendar.MONTH, Integer.parseInt(dateArr[1]));
        c.set(Calendar.YEAR, Integer.parseInt(dateArr[2]));

        Log.v(TAG, "day of the week: " + c.get(Calendar.DAY_OF_WEEK));

        return hm.get(c.get(Calendar.DAY_OF_WEEK));
    }

}
