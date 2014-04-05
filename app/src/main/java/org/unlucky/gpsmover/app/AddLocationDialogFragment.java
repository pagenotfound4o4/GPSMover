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

/**
 * A dialog that add the location to your favourite.
 */
public class AddLocationDialogFragment extends DialogFragment {

    public interface AddLocationDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    AddLocationDialogListener mListener;

    private EditText name;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddLocationDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddLocationDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // set view
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_add_location, null);
        name = (EditText)view.findViewById(R.id.add_location_edt);
        builder.setTitle(R.string.dialog_add_location_title)
                .setIcon(android.R.drawable.ic_menu_add)
                .setView(view);

        // set listener
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(AddLocationDialogFragment.this);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(AddLocationDialogFragment.this);
            }
        });

        return builder.create();
    }

    /**
     * obtain location name
     * @return location name
     */
    public String getEditText() {
        return name.getText().toString();
    }
}
