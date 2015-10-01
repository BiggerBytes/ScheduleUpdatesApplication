package com.example.shachamginat.de_shalitupdates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shachamginat.de_shalitupdates.serverdatarecievers.ScheduleChange;

import java.util.ArrayList;

/**
 * Created by OrenUrbach on 10/1/15.
 */
public class ScheduleChangeAdapter extends RecyclerView.Adapter<ScheduleChangeAdapter.ScheduleChangeViewHolder> {

    private ArrayList<ScheduleChange> changes;
    private Context context;

    private final static String TAG = "ScheduleChangeAdapter";

    public ScheduleChangeAdapter(Context context, ArrayList<ScheduleChange> changes) {
        this.changes = changes;
        this.context = context;
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
//        ScheduleChangeViewHolder.tvHour.setText("3");
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
        private TextView tvTeacherName, tvHour;

        public ScheduleChangeViewHolder(View itemView) {
            super(itemView);
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            tvHour = (TextView) itemView.findViewById(R.id.tvHours);
        }
    }
}
