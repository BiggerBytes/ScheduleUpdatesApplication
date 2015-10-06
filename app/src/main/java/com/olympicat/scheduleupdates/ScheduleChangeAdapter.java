package com.olympicat.scheduleupdates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        ScheduleChangeViewHolder.tvHour.setText("" + change.getHour());
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


    public String formatDate(Calendar given) {
        // result will be constructed here
        StringBuffer sb = new StringBuffer();

        sb.append("יום ");
        sb.append(getDayInHebrew(given.get(Calendar.DAY_OF_WEEK)));
        sb.append(" - ");
        sb.append(given.get(Calendar.DAY_OF_MONTH) + "/" + given.get(Calendar.MONTH));

        return sb.toString();
    }

    public String getDayInHebrew(int day) {
        HashMap<Integer, String> hm = new HashMap<>();
        hm.put(1, "ראשון");
        hm.put(2, "שני");
        hm.put(3, "שלישי");
        hm.put(4, "רביעי");
        hm.put(5, "חמישי");
        hm.put(6, "שישי");
        // no saturday because there is no school on saturday

        return hm.get(day);
    }
}
