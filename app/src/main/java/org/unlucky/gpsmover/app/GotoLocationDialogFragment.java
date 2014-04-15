package org.unlucky.gpsmover.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.unlucky.gpsmover.app.util.Common;

/**
 * A dialog that goto the position you input.
 */
public class GotoLocationDialogFragment extends DialogFragment {

    public interface GotoLocationDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    GotoLocationDialogListener mListener;

    private EditText location;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GotoLocationDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GotoLocationDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // set view
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_goto_location, null);
        location = (EditText)view.findViewById(R.id.goto_location_edt);
        builder.setTitle(R.string.dialog_goto_location_title)
                .setIcon(android.R.drawable.ic_menu_myplaces)
                .setView(view);

        // set listener
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(GotoLocationDialogFragment.this);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(GotoLocationDialogFragment.this);
            }
        });

        return builder.create();
    }

    /**
     * Get latitude and longitude user input
     * @return latitude and longitude raw string
     */
    public String getEditText() {
        return location.getText().toString();
    }
}
