package com.olympicat.scheduleupdates;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.olympicat.scheduleupdate.R;
import com.olympicat.scheduleupdates.serverdatarecievers.ScheduleDataFactory;

/**
 * Created by OrenUrbach on 10/9/15.
 */
public class ChooseClassDialog extends DialogFragment {
    private OnDialogFinishListener listener;

    private final static String TAG = "ChooseClassDialog";

    public ChooseClassDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get shared preferences
        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.sp_school_class_choice), Context.MODE_PRIVATE);

        final String key = getString(R.string.key_school_class_choice);
        int currentSelection = SchoolClasses.getIndexByClassId(sharedPref.getInt(key, 0));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_choose_class)
                .setSingleChoiceItems(getResources().getStringArray(R.array.classes), currentSelection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "Index : " + which);
                        int classId = SchoolClasses.getScoolClassId(which);
                        Log.v(TAG, "classId: " + classId);
                        sharedPref.edit().putInt(key, classId).commit();


                        ChooseClassDialog.this.dismiss();
                        if (ChooseClassDialog.this.listener != null)
                            ChooseClassDialog.this.listener.onFinish(classId);


                    }
                });

        return builder.create();
    }

    /**
     * listener
     */
    public void setOnDialogFinishListener(OnDialogFinishListener listener) {
        this.listener = listener;
    }

    public interface OnDialogFinishListener {
        void onFinish(int classId);
    }
}
