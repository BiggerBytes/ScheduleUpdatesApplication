package com.olympicat.scheduleupdates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olympicat.scheduleupdate.R;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleChange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by OrenUrbach on 10/1/15.
 * adapter for schedule changes recyclerview
 */
public class ScheduleChangeAdapter extends RecyclerView.Adapter<ScheduleChangeAdapter.ScheduleChangeViewHolder> {

    private Context context;
    private ArrayList<ScheduleChange> changes;

    private final static String TAG = "ScheduleChangeAdapter";

    public ScheduleChangeAdapter(Context c, ArrayList<ScheduleChange> changes) {
        this.context = c;
        this.changes = changes;
    }

    @Override
    public ScheduleChangeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_change, viewGroup, false);
        ScheduleChangeViewHolder mvh = new ScheduleChangeViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(ScheduleChangeViewHolder ScheduleChangeViewHolder, int index) {
        final ScheduleChange change = this.changes.get(index);
        String date = change.getDate();
        String dayOfMonth = getDayOfMonth(date);

        ScheduleChangeViewHolder.tvTeacherName.setText(change.getTeacherName());
        ScheduleChangeViewHolder.tvHours.setText(change.getHour());
        ScheduleChangeViewHolder.tvDayOfMonth.setText(dayOfMonth);
        ScheduleChangeViewHolder.tvDayOfWeek.setText(getDayInHebrew(date));
        // TODO: decide if this is a cancel or sub
        ScheduleChangeViewHolder.tvChangeType.setText(context.getString(R.string.label_cancel));

        // if it's the same day, don't show the day again
        if (index != 0) {
            final ScheduleChange prev = this.changes.get(index - 1);
            Log.v(TAG, "this day of the month: " + dayOfMonth);
            Log.v(TAG, "previous day of month: " + getDayOfMonth(prev.getDate()));
            if (getDayOfMonth(prev.getDate()).equals(dayOfMonth)) {
                ScheduleChangeViewHolder.tvDayOfMonth.setVisibility(View.INVISIBLE);
                ScheduleChangeViewHolder.tvDayOfWeek.setVisibility(View.INVISIBLE);
            } else {
                  // TODO: make the margin bigger between days
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ScheduleChangeViewHolder.item.getLayoutParams();
//                params.setMargins(0, 16, 0, 0);
//                ScheduleChangeViewHolder.item.setLayoutParams(params);
            }

        }

        // if it's today, color it green
        int primary = context.getResources().getColor(R.color.primary);
        if (Integer.parseInt(dayOfMonth) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            ScheduleChangeViewHolder.tvDayOfMonth.setTextColor(primary);
            ScheduleChangeViewHolder.tvDayOfWeek.setTextColor(primary);
        }
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
        private RelativeLayout item;
        private TextView tvTeacherName, tvHours, tvDayOfMonth, tvDayOfWeek, tvChangeType;

        public ScheduleChangeViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.item_change);
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            tvHours = (TextView) itemView.findViewById(R.id.tvHours);
            tvDayOfMonth = (TextView) itemView.findViewById(R.id.tvDayOfMonth);
            tvDayOfWeek = (TextView) itemView.findViewById(R.id.tvDayOfWeek);
            tvChangeType = (TextView) itemView.findViewById(R.id.tvChangeType);
        }
    }

    /**
     * returns the day of month from the given string date
     * @param given
     * @return
     */
    public String getDayOfMonth(String given) {
        return given.substring(0, 2);
    }

    public String getDayInHebrew(String date) {
        Log.v(TAG, "date: " + date);
        HashMap<Integer, String> hm = new HashMap<>();
        hm.put(4, "ראשון");
        hm.put(5, "שני");
        hm.put(6, "שלישי");
        hm.put(7, "רביעי");
        hm.put(1, "חמישי");
        hm.put(2, "שישי");
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
